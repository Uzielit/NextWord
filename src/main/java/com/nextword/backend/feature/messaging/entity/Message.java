package com.nextword.backend.feature.messaging.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name="Mensaje")
@Getter
@Setter
@NoArgsConstructor
public class Message {
    @Id
    @Column(name = "id_mensaje", length = 36, nullable = false)
    private String id;
    @Column(name = "id_remitente", length = 36, nullable = false)
    private String senderId;
    @Column(name = "id_destinatario", length = 36, nullable = false)
    private String receiverId;
    @Column(name = "asunto", length = 150)
    private String subject;
    @Column(name = "cuerpo", length = 2000, nullable = false)
    private String body;
    @Column(name = "url_adjunto", length = 500)
    private String attachmentUrl;
    @Column(name = "nombre_archivo_adjunto", length = 255)
    private String attachmentFileName;
    @Column(name = "leido", length = 1, nullable = false)
    private String Read = "0";
    @Column(name = "fecha_envio", nullable = false)
    private ZonedDateTime sentAt;
}
