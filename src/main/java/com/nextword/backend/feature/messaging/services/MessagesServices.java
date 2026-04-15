package com.nextword.backend.feature.messaging.services;

import com.nextword.backend.feature.messaging.dto.InboxDto;
import com.nextword.backend.feature.messaging.dto.MessagesDto;
import com.nextword.backend.feature.messaging.entity.Message;
import com.nextword.backend.feature.messaging.repository.MessagesRepository;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessagesServices{

    private final MessagesRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessagesServices(MessagesRepository messageRepository,
                           UserRepository userRepository,
                           SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional(readOnly = true)
    public List<Message> getChatHistory(String user1, String user2) {
        return messageRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderBySentAtAsc(
                user1, user2, user2, user1
        );
    }

    @Transactional
    public Message processNewMessage(MessagesDto incomingDto) {
        System.out.println("✉️ Nuevo mensaje interceptado de: " + incomingDto.senderId());

        Message newMessage = new Message();
        newMessage.setSenderId(incomingDto.senderId());
        newMessage.setReceiverId(incomingDto.receiverId());
        newMessage.setBody(incomingDto.body());
        newMessage.setRead("0"); // Por defecto no leído

        return messageRepository.save(newMessage);
    }

    @Transactional
    public Message markMessageAsRead(String idMensaje) {
        return messageRepository.findById(idMensaje)
                .map(mensajeEncontrado -> {
                    mensajeEncontrado.setRead("1");
                    Message mensajeActualizado = messageRepository.save(mensajeEncontrado);

                    // Avisamos por WebSocket que este mensaje ya se leyó (Palomitas azules)
                    messagingTemplate.convertAndSend("/topic/messages", mensajeActualizado);

                    return mensajeActualizado;
                })
                .orElse(null); // Retorna null si no se encuentra
    }


    @Transactional(readOnly = true)
    public List<InboxDto> getInbox(String userEmail) {
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String myId = currentUser.getId();

        List<Message> allMessages = messageRepository.findAllMessagesForUser(myId);
        List<Message> latestMessages = new java.util.ArrayList<>();
        java.util.Set<String> processedContacts = new java.util.HashSet<>();

        for (Message msg : allMessages) {
            String contactId = msg.getSenderId().equals(myId) ? msg.getReceiverId() : msg.getSenderId();

            if (!processedContacts.contains(contactId)) {
                latestMessages.add(msg);
                processedContacts.add(contactId);
            }
        }

        return latestMessages.stream().map(msg -> {
            boolean sentByMe = msg.getSenderId().equals(myId);
            String contactId = sentByMe ? msg.getReceiverId() : msg.getSenderId();

            User contact = userRepository.findById(contactId).orElse(null);
            String contactName = contact != null ? contact.getFullName() : "Usuario Desconocido";
            String contactPhoto = contact != null ? contact.getProfilePicture() : "";

            int unreadCount = 0;
            if (!sentByMe) {
                unreadCount = messageRepository.countUnreadMessages(myId, contactId);
            }

            return new InboxDto(
                    contactId,
                    contactPhoto,
                    contactName,
                    msg.getBody(),
                    msg.getSentAt(),
                    sentByMe,
                    unreadCount,
                    msg.getRead()

            );
        }).collect(Collectors.toList());
    }
}
