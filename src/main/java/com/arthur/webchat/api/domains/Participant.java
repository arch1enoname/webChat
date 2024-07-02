package com.arthur.webchat.api.domains;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Participant implements Serializable {
    @Builder.Default
    Instant enterAt = Instant.now();
    String sessionId;
    String participantId;
    String chatId;
}
