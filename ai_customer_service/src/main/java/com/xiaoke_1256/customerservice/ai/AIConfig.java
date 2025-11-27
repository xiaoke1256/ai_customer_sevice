package com.xiaoke_1256.customerservice.ai;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        ChatClient client = builder
                .defaultOptions( ChatOptions.builder().model("qwen-plus").build())
                .defaultSystem("""
                ## 角色定义
                你是orders平台智能客服。
                ## 行为指南
                如果你不知道就转人工服务，不要编造答案。
                """)
                .build();
        return client;
    }
}
