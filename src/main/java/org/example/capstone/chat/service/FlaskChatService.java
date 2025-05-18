package org.example.capstone.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.capstone.chat.dto.ChatRequest;
import org.example.capstone.chat.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlaskChatService {

    @Value("${flask.api.endpoints.chat}")
    private String flaskChatApiUrl;

    private final ObjectMapper objectMapper;

    public ChatResponse sendChatToFlask(ChatRequest chatRequest) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(flaskChatApiUrl);

            // 텍스트 메시지만 전송
            StringEntity entity = new StringEntity(
                    objectMapper.writeValueAsString(Map.of(
                            "message", chatRequest.getMessage(),
                            "username", chatRequest.getUsername()
                    )),
                    ContentType.APPLICATION_JSON
            );

            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);
                log.info("Flask Chat API Response: {}", responseString);
                return objectMapper.readValue(responseString, ChatResponse.class);
            }
        }
    }
}
