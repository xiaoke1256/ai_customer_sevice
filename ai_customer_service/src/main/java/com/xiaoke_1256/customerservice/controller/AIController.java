package com.xiaoke_1256.customerservice.controller;

import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@RestController
public class AIController {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(AIController.class);

    @Autowired
    private ChatClient chatClient;

    /**
     * 与ai聊天
     * @return
     */
    @GetMapping("/chat")
    public String chat(@RequestParam("userPrompt") String userPrompt, @RequestHeader(value = "sessionId", required = false) String sessionId){
        LOG.info("userPrompt:"+userPrompt);
        LOG.info("sessionId:"+sessionId);
        String conversantId = sessionId==null? UUID.randomUUID().toString() :sessionId;
        ChatClient.CallResponseSpec response = chatClient.prompt()
                .system( p -> p.param("username", "游客").param("account", "null") )
                .user(userPrompt)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, conversantId))
                .call();
        String content = response.content();
        return content;
    }

    @GetMapping("/openSseChat")
    public SseEmitter openSseChat(@RequestParam("userPrompt") String userPrompt, @RequestHeader(value = "sessionId", required = false) String sessionId){
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        new Thread(() -> {
            try {
                ChatClient.StreamResponseSpec stream = chatClient.prompt()
                        .system(p -> p.param("username", "游客").param("account", "null"))
                        .user(userPrompt)
                        .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, sessionId))
                        .stream();
                stream.content().all(content -> {
                    try {
                        emitter.send(SseEmitter.event().name("data").data(content));
                        return true;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                        //return false;
                    }
                }).block();
                emitter.complete();
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}
