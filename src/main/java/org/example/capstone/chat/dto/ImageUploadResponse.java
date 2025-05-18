package org.example.capstone.chat.dto;

import lombok.Data;

@Data
public class ImageUploadResponse {
    private String imageUrl;
    private String username;
    private String message;
    private boolean success;
}
