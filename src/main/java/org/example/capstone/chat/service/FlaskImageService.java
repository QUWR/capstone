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
import org.example.capstone.chat.dto.ImageUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlaskImageService {

    @Value("${flask.api.endpoints.image}")
    private String flaskImageApiUrl;

    private final ObjectMapper objectMapper;

    public ImageUploadResponse uploadImageToFlask(MultipartFile image, String username) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadFile = new HttpPost(flaskImageApiUrl);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("username", username, ContentType.TEXT_PLAIN);

            // 이미지 파일 추가
            builder.addBinaryBody(
                    "image",
                    image.getInputStream(),
                    ContentType.MULTIPART_FORM_DATA,
                    image.getOriginalFilename()
            );

            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            try (CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);
                log.info("Flask Image API Response: {}", responseString);
                return objectMapper.readValue(responseString, ImageUploadResponse.class);
            }
        }
    }
}
