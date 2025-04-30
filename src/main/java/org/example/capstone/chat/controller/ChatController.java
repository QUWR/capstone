package org.example.capstone.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.capstone.chat.dto.ChatRequest;
import org.example.capstone.chat.dto.ChatResponse;
import org.example.capstone.chat.service.FlaskService;
import org.example.capstone.user.login.dto.CustomUserDetails;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final FlaskService flaskService;

    @MessageMapping("/api/chat")
    public void sendMessage(@Payload ChatRequest chatRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 인증된 사용자가 없으면 처리하지 않음
        if (userDetails == null) {
            log.warn("인증되지 않은 사용자의 메시지 요청");
            return;
        }

        String username = userDetails.getUsername();
        chatRequest.setUsername(username); // 인증된 사용자명으로 설정

        try {
            // 사용자에게 메시지 수신 알림
            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/messages",
                    "메시지를 처리 중입니다..."
            );

            // Flask 서버에 요청 전송
            ChatResponse response = flaskService.sendRequestToFlask(chatRequest);

            // 응답을 사용자에게 전송
            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/messages",
                    response
            );

        } catch (IOException e) {
            log.error("Flask 서버와 통신 중 오류 발생: {}", e.getMessage(), e);
            ChatResponse errorResponse = new ChatResponse();
            errorResponse.setUsername(username);
            errorResponse.setMessage("서버 오류가 발생했습니다: " + e.getMessage());

            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/messages",
                    errorResponse
            );
        }
    }
}
