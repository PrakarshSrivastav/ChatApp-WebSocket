package com.prakarsh.chatapp_ws.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String message;
    private String timestamp;
    private String userName;
    private MessageType messageType;
}
