package org.example.capstone.chat.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    private String username;
}
