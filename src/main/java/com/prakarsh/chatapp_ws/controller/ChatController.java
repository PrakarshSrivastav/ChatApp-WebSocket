package com.prakarsh.chatapp_ws.controller;


import com.prakarsh.chatapp_ws.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisTemplate redisTemplate;

    //Send Message to client
    @MessageMapping("/chat.send")
    public ChatMessage sendChatMessage(ChatMessage chatMessage){
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        redisTemplate.convertAndSend("chat", chatMessage);
        return chatMessage;
    }


    //Add user to app
}
