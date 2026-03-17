package com.nextword.backend.feature.messaging.dto;

public record MessagesDto(
        String senderId,
        String receiverId,
        String body
){
}
