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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value ="/openSseChat",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter openSseChat(@RequestParam("userPrompt") String userPrompt, @RequestHeader(value = "sessionId", required = false) String sessionId) throws IOException {
        String conversantId = sessionId==null? UUID.randomUUID().toString() :sessionId;
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitter.onCompletion(() -> {
            LOG.info("SSE connection closed");
        });
        emitter.onError(t -> {
            LOG.error(t.getMessage(),t);
        });
        emitter.onTimeout(() -> {
            LOG.info("SSE connection timed out");
        });
        emitter.send(SseEmitter.event().name("start" ).data("start"));

        new Thread(() -> {
            try {
                LOG.info("sessionId:"+conversantId);
                LOG.info("start thread.");
                ChatClient.StreamResponseSpec responseStream = chatClient.prompt()
                        .system(p -> p.param("username", "游客").param("account", "null"))
                        .user(userPrompt)
                        .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, conversantId))
                        .stream();

                LOG.info("获取 responseStream.");
                responseStream.content().subscribe((String content)->{
                    LOG.info("content:"+content);
                    try {
                        emitter.send(content);
                    } catch (IOException e) {
                        //TODO 向前端发送错误信息
                        emitter.complete();
                        throw new RuntimeException(e);
                    }
                }, emitter::completeWithError, ()->{
                    try {
                        emitter.send(SseEmitter.event().name("complete" ).data("complete"));
                    } catch (IOException e) {
                        //TODO 向前端发送错误信息
                        emitter.complete();
                        throw new RuntimeException(e);
                    }
                    emitter.complete();
                });
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }
}
