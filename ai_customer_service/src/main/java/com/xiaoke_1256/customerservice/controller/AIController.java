package com.xiaoke_1256.customerservice.controller;

import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value ="/openSseChat",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter openSseChat(@RequestParam("userPrompt") String userPrompt, @RequestHeader(value = "sessionId", required = false) String sessionId) throws IOException {
        String conversantId = sessionId==null? UUID.randomUUID().toString() :sessionId;
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitter.onCompletion(() -> {
            LOG.info("SSE connection closed");
            emitterMap.remove(conversantId);
        });
        emitter.onError(t -> {
            LOG.error(t.getMessage(),t);
            emitterMap.remove(conversantId);
        });
        emitter.onTimeout(() -> {
            LOG.info("SSE connection timed out");
            emitterMap.remove(conversantId);
        });
        emitterMap.put(conversantId, emitter);
        emitter.send(SseEmitter.event().name("start" ).data("start"));

        new Thread(() -> {
            try {
                LOG.info("sessionId:"+conversantId);
                LOG.info("start thread.");
                ChatClient.CallResponseSpec response = chatClient.prompt()
                        .system(p -> p.param("username", "游客").param("account", "null"))
                        .user(userPrompt)
                        .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, conversantId))
                        .call();

                emitter.send(response.content());
                Thread.sleep(10);
                emitter.send(SseEmitter.event().name("complete" ).data("complete"));
                emitter.complete();
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }
}
