package com.prakarsh.chatapp_ws.listener;


import com.prakarsh.chatapp_ws.dto.ChatMessage;
import com.prakarsh.chatapp_ws.dto.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    public WebSocketEventListener(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        SimpMessageHeaderAccessor headerAccessor= SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username= (String) headerAccessor.getSessionAttributes().get("username");

        if(username != null){
            ChatMessage chatMessage=new ChatMessage();
            chatMessage.setMessageType((MessageType.LEAVE));
            chatMessage.setUserName(username);
            chatMessage.setMessage(username+" has left the chat room");
            logger.info("user disconnected: {}", username);
            redisTemplate.convertAndSend("chat", chatMessage);
        }
    }
}
