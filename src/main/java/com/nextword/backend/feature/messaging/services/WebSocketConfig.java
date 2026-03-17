package com.nextword.backend.feature.messaging.services;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;

@Configuration
@EnableWebSocketSecurity
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
}
