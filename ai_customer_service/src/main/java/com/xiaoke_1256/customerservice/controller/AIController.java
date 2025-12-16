package com.xiaoke_1256.customerservice.controller;

import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
