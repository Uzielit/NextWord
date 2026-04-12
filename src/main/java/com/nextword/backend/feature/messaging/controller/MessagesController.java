package com.nextword.backend.feature.messaging.controller;


import com.nextword.backend.feature.messaging.dto.InboxDto;
import com.nextword.backend.feature.messaging.dto.MessagesDto;
import com.nextword.backend.feature.messaging.entity.Message;
import com.nextword.backend.feature.messaging.services.MessagesServices;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;



@RestController
public class MessagesController {


    private final MessagesServices messageService;

    public MessagesController( MessagesServices messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/api/messages/history/{user1}/{user2}")
    public ResponseEntity<List<Message>> getChatHistory(
            @PathVariable String user1,
            @PathVariable String user2) {

        List<Message> history = messageService.getChatHistory(user1, user2);
        return ResponseEntity.ok(history);
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message receiveAndBroadcastMessage(MessagesDto incomingDto) {
        return messageService.processNewMessage(incomingDto);
    }

    @PutMapping("/api/messages/{idMensaje}/read")
    public ResponseEntity<Message> markMessageAsRead(@PathVariable String idMensaje) {

        Message mensajeActualizado = messageService.markMessageAsRead(idMensaje);

        if (mensajeActualizado != null) {
            return ResponseEntity.ok(mensajeActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/messages/inbox")
    public ResponseEntity<List<InboxDto>> getInbox(Principal principal) {

        List<InboxDto> inbox = messageService.getInbox(principal.getName());
        return ResponseEntity.ok(inbox);
    }
}
