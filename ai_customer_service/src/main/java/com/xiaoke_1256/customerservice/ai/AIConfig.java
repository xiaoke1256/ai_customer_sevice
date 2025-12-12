package com.xiaoke_1256.customerservice.ai;

import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.Arrays;

@Configuration
public class AIConfig {

    @Value("classpath:/system_prompt.md")
    private Resource systemPromptResource;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder,
                                 @Autowired VectorStore vectorStore,
                                 @Autowired ChatMemory chatMemory) {

        // 启用混合搜索，包括嵌入和全文搜索
        SearchRequest searchRequest = SearchRequest.builder().
                topK(10)
                .similarityThresholdAll()
                .build();

        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore).searchRequest(searchRequest).build();

        ChatClient client = builder
                .defaultOptions( ChatOptions.builder().model("qwen-plus").build())
                .defaultSystem(systemPromptResource)
                .defaultAdvisors(Arrays.asList(
                        new SimpleLoggerAdvisor(),
                        questionAnswerAdvisor,
                        PromptChatMemoryAdvisor.builder(chatMemory).build()))
                .build();
        return client;
    }

//    @Bean
//    public VectorStore vectorStore(EmbeddingModel embeddingModel){
//        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
//        InputStream inputStream = AIConfig.class.getResourceAsStream("/product_types.md");
//
//        String content = "";
//        try {
//            content =IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        Document doc = new Document("productTypes", content, new HashMap<String, Object>());
//        vectorStore.add(Arrays.asList(doc));
//        return vectorStore;
//    }

    @Bean
    public ChatMemoryRepository chatMemoryRepository(
            @Value("${spring.ai.memory.redis.host}") String host,
            @Value("${spring.ai.memory.redis.port}") int port,
            @Value("${spring.ai.memory.redis.timeout}") int timeout) {
        return RedisChatMemoryRepository.builder().host(host).port(port).timeout(timeout).build();
    }

    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository){
    	return MessageWindowChatMemory.
                builder().
                maxMessages(10).
                chatMemoryRepository(chatMemoryRepository).build();
    }

}
