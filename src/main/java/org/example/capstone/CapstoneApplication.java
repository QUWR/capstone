package org.example.capstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;


@SpringBootApplication
@EnableWebSocketMessageBroker
public class CapstoneApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapstoneApplication.class, args);
    }

}
