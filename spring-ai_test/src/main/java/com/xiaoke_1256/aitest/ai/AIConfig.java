package com.xiaoke_1256.aitest.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        ChatClient client = builder
                .defaultSystem("你是一个客服。")
                .build();
        return client;
    }
}
