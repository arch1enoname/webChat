package com.arthur.webchat.api.factories;

import com.arthur.webchat.api.domains.Chat;
import com.arthur.webchat.api.dto.ChatDto;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ChatDtoFactory {
    public ChatDto createChatDto(Chat chat) {
        return ChatDto.builder()
                .id(chat.getId())
                .name(chat.getName())
                .createdAt(Instant.ofEpochMilli(chat.getCreatedAt()))
                .build();
    }
}
