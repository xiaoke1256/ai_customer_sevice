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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, VectorStore vectorStore) {
        ChatClient client = builder
                .defaultOptions( ChatOptions.builder().model("qwen-plus").build())
                .defaultSystem("""
                ## 角色定义
                你是orders平台智能客服。
                ## 行为指南
                如果你不知道就转人工服务，不要编造答案。
                """)
                .defaultAdvisors(Arrays.asList(new SimpleLoggerAdvisor(),new QuestionAnswerAdvisor(vectorStore)))
                .build();
        return client;
    }

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel){
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        //vectorStore.add("orders平台智能客服", "orders平台智能客服，你可以使用orders平台提供的api进行查询。");
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
