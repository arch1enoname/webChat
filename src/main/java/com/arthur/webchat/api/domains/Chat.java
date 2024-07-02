package com.arthur.webchat.api.domains;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chat implements Serializable {
    @Builder.Default
    String id = UUID.randomUUID().toString();
    String name;
    @Builder.Default
    Instant createdAt = Instant.now();
}
