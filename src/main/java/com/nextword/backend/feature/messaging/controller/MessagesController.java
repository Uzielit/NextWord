package com.nextword.backend.feature.messaging.controller;


import com.nextword.backend.feature.messaging.dto.InboxDto;
import com.nextword.backend.feature.messaging.dto.MessagesDto;
import com.nextword.backend.feature.messaging.entity.Message;
import com.nextword.backend.feature.messaging.repository.MessagesRepository;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class MessagesController {


    private final MessagesRepository messageRepository;
    private final UserRepository userRepository;


    public MessagesController(MessagesRepository messageRepository,
    UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository=userRepository;
    }

    @GetMapping("/api/messages/history/{user1}/{user2}")
    public ResponseEntity<List<Message>> getChatHistory(
            @PathVariable String user1,
            @PathVariable String user2) {
        List<Message> history = messageRepository
                .findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderBySentAtAsc(
                        user1, user2, user2, user1
                );
        return ResponseEntity.ok(history);
    }
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message receiveAndBroadcastMessage(MessagesDto incomingDto) {

        System.out.println("✉️ Nuevo mensaje interceptado de: " + incomingDto.senderId());

        Message newMessage = new Message();
        newMessage.setSenderId(incomingDto.senderId());
        newMessage.setReceiverId(incomingDto.receiverId());
        newMessage.setBody(incomingDto.body());
        Message savedMessage = messageRepository.save(newMessage);

        return savedMessage;
    }

    @PutMapping("/api/messages/{idMensaje}/read")
    public ResponseEntity<Message> markMessageAsRead(@PathVariable String idMensaje) {

        return messageRepository.findById(idMensaje)
                .map(mensajeEncontrado -> {
                    mensajeEncontrado.setRead("1");
                    Message mensajeActualizado = messageRepository.save(mensajeEncontrado);
                    return ResponseEntity.ok(mensajeActualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/api/messages/inbox")
    public ResponseEntity<List<InboxDto>> getInbox(Principal principal) {
        // 1. ¿Quién soy yo?
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String myId = currentUser.getId();

        // 2. Traer TODOS los mensajes ordenados del más nuevo al más viejo
        List<Message> allMessages = messageRepository.findAllMessagesForUser(myId);

        // 3. 🌟 AGRUPAR EN JAVA (Adiós errores de Oracle)
        List<Message> latestMessages = new java.util.ArrayList<>();
        java.util.Set<String> processedContacts = new java.util.HashSet<>();

        for (Message msg : allMessages) {
            // Identificar con quién es este mensaje
            String contactId = msg.getSenderId().equals(myId) ? msg.getReceiverId() : msg.getSenderId();

            // Si es la primera vez que vemos a este contacto en la lista, guardamos el mensaje
            if (!processedContacts.contains(contactId)) {
                latestMessages.add(msg);
                processedContacts.add(contactId);
            }
        }

        // 4. Mapear los datos para el celular
        List<InboxDto> inbox = latestMessages.stream().map(msg -> {
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
                    unreadCount
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(inbox);
    }

}
