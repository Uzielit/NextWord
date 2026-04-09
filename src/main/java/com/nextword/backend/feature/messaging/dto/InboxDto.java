package com.nextword.backend.feature.messaging.dto;


import java.time.ZonedDateTime;

public record InboxDto(
        String contactId,
        String photoPerfil,
        String name,
        String lastMessage,
        ZonedDateTime dateLastMessage,
        boolean sentByMe,
        int unreadCount

) {
}
