package com.nextword.backend.feature.messaging.controller;


import com.nextword.backend.feature.messaging.dto.MessagesDto;
import com.nextword.backend.feature.messaging.entity.Message;
import com.nextword.backend.feature.messaging.repository.MessagesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class MessagesController {


    private final MessagesRepository messageRepository;


    public MessagesController(MessagesRepository messageRepository) {
        this.messageRepository = messageRepository;
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


}
