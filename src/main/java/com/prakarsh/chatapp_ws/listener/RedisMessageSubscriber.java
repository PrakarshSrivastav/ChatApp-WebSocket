package com.prakarsh.chatapp_ws.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prakarsh.chatapp_ws.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RedisMessageSubscriber.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public RedisMessageSubscriber(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, SimpMessageSendingOperations simpMessageSendingOperations) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Deserialize
        String publishedMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());

        if (publishedMessage == null) {
            logger.warn("Received a null or empty message from Redis");
            return;
        }

        try {
            ChatMessage chatMessage = objectMapper.readValue(publishedMessage, ChatMessage.class);
            simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
        } catch (JsonProcessingException e) {
            logger.error("Failed to deserialize Redis message: {}", publishedMessage, e);
        }
    }
}
