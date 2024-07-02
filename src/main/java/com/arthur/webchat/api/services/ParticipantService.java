package com.arthur.webchat.api.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ParticipantService {

    public void handleSubscription(String sessionId, String participantId, String chatId) {
        
    }

}
