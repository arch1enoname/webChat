package com.arthur.webchat.api.services;

import com.arthur.webchat.api.domains.Participant;
import com.arthur.webchat.api.dto.ParticipantDto;
import com.arthur.webchat.api.factories.ParticipantDtoFactory;
import com.arthur.webchat.api.ws.ChatWSController;
import com.arthur.webchat.api.ws.ParticipantWSController;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.hibernate.event.spi.AbstractEvent;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ParticipantService {

    private static final Map<String, Participant> sessionIdToParticipantMap = new ConcurrentHashMap<>();
    private final ParticipantDtoFactory participantDtoFactory;

    SetOperations<String, Participant> setOperations;

    SimpMessagingTemplate messagingTemplate;

    public void handleJoinChat(String sessionId, String participantId, String chatId) {

        log.info(String.format("Participant \"%s\" join in chat \"%s\".", sessionId, chatId));

        Participant participant = Participant.builder()
                .sessionId(sessionId)
                .id(participantId)
                .chatId(chatId)
                .build();

        sessionIdToParticipantMap.put(participant.getSessionId(), participant);

        setOperations.add(ParticipantKeyHelper.makeKey(chatId), participant);

        messagingTemplate.convertAndSend(
                ParticipantWSController.getFetchParticipantJoinInChatDestination(chatId),
                participantDtoFactory.makeParticipantDto(participant)
        );
    }


    public Set<Participant> getParticipants(String chatId) {
        return Optional
                .ofNullable(setOperations.members(ParticipantKeyHelper.makeKey(chatId)))
                .orElseGet(HashSet::new);
    }

    public void handleLeaveChat(AbstractSubProtocolEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        Optional.ofNullable(headerAccessor.getSessionAttributes())
                .map(sessionIdToParticipantMap::remove)
                .ifPresent(participant -> {
                    String chatId = participant.getChatId();

                    log.info(String.format(
                            "Participant \"%s\" left in chat \"%s\".",
                            participant.getSessionId(),
                            chatId
                    ));
                    String key = ParticipantKeyHelper.makeKey(chatId);
                    setOperations.remove(key, participant);

                    Optional
                            .ofNullable(setOperations.size(key))
                            .filter(size -> size > 0L)
                            .ifPresent(seize -> chatService.delete(chatId));

                    messagingTemplate.convertAndSend(
                            key,
                            participantDtoFactory.makeParticipantDto(participant)
                    );


                });
    }

    private static class ParticipantKeyHelper {

        private static final String KEY = "arthur:webchat:chats:{chat_id}:participants";

        public static String makeKey(String chatId) {
            return KEY.replace("{chat_id}", chatId);
        }
    }

}
