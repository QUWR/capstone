package org.example.capstone.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.capstone.chat.dto.ChatResponse;
import org.example.capstone.chat.service.FlaskChatService;
import org.example.capstone.user.login.dto.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/chat") // RESTful API 경로 설정
@RequiredArgsConstructor
@Slf4j
public class ChatController {

  private final FlaskChatService flaskChatService;

  /**
   * 텍스트 메시지와 선택적 이미지 파일을 함께 받아 Flask 서버로 전송하는 통합 API
   * @param message 사용자 메시지
   * @param image 이미지 파일 (선택 사항)
   * @param userDetails 인증된 사용자 정보
   * @return Flask 서버의 응답
   */
  @PostMapping("/send")
  public ResponseEntity<?> sendMessage(
      @RequestParam("message") String message,
      @RequestParam(value = "image", required = false) MultipartFile image, // 이미지는 선택 사항
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    if (userDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자");
    }

    try {
      // 통합된 서비스 로직 호출
      ChatResponse response = flaskChatService.sendChatToFlask(message, image, userDetails.getUsername());
      return ResponseEntity.ok(response);
    } catch (IOException e) {
      log.error("Flask 서버와 통신 중 오류 발생: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("메시지 전송 실패: " + e.getMessage());
    }
  }
}