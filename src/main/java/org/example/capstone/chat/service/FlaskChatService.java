package org.example.capstone.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.capstone.chat.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlaskChatService {

  @Value("${flask.api.endpoints.chat}")
  private String flaskChatApiUrl;

  private final ObjectMapper objectMapper;

  /**
   * 텍스트 메시지와 이미지 파일을 Flask 서버로 전송하는 통합 서비스 메서드
   * @param message 사용자 메시지
   * @param image 이미지 파일 (null일 수 있음)
   * @param username 사용자 이름
   * @return Flask 서버의 응답
   * @throws IOException 통신 오류 발생 시
   */
  public ChatResponse sendChatToFlask(String message, MultipartFile image, String username) throws IOException {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost(flaskChatApiUrl);

      // Multipart 빌더 생성
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();

      // 텍스트 데이터 추가 (UTF-8 인코딩 지정)
      builder.addTextBody("username", username, ContentType.create("text/plain", "UTF-8"));
      builder.addTextBody("message", message, ContentType.create("text/plain", "UTF-8"));

      // 이미지 파일이 있는 경우에만 추가
      if (image != null && !image.isEmpty()) {
        builder.addBinaryBody(
            "image",
            image.getInputStream(),
            ContentType.APPLICATION_OCTET_STREAM, // 일반적인 바이너리 타입
            image.getOriginalFilename()
        );
      }

      HttpEntity multipart = builder.build();
      httpPost.setEntity(multipart);

      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        HttpEntity responseEntity = response.getEntity();
        String responseString = EntityUtils.toString(responseEntity);
        log.info("Flask Chat API Response: {}", responseString);
        return objectMapper.readValue(responseString, ChatResponse.class);
      }
    }
  }
}