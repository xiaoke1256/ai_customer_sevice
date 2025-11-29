package com.xiaoke_1256.customerservice.ai;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.apache.commons.io.IOUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.content.Media;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AIConfig {

    @Value("classpath:/system_prompt.md")
    private Resource systemPromptResource;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, VectorStore vectorStore) {
        ChatClient client = builder
                .defaultOptions( ChatOptions.builder().model("qwen-plus").build())
                .defaultSystem(systemPromptResource)
                .defaultAdvisors(Arrays.asList(new SimpleLoggerAdvisor(),new QuestionAnswerAdvisor(vectorStore)))
                .build();
        return client;
    }

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel){
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        InputStream inputStream = AIConfig.class.getResourceAsStream("/product_types.md");

        String content = "";
        try {
            content =IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Document doc = new Document("productTypes", content, new HashMap<String, Object>());
        vectorStore.add(Arrays.asList(doc));
        return vectorStore;
    }
}
