package com.arthur.webchat.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantDto {
    @Builder.Default
    Instant enterAt = Instant.now();
    String id;
}
