package com.arthur.webchat.api.factories;

import com.arthur.webchat.api.domains.Participant;
import com.arthur.webchat.api.dto.ParticipantDto;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ParticipantDtoFactory {
    public ParticipantDto makeParticipantDto(Participant participant) {
        return ParticipantDto.builder()
                .id(participant.getId())
                .enterAt(Instant.ofEpochMilli(participant.getEnterAt()))
                .build();
    }
}
