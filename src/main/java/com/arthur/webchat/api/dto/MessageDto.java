package com.arthur.webchat.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    String from;
    String message;
    @Builder.Default
    Instant createdAt = Instant.now();
}
