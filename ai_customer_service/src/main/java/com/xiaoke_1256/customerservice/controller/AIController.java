package com.xiaoke_1256.customerservice.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIController {

    @Autowired
    private ChatClient chatClient;

    /**
     * 与ai聊天
     * @return
     */
    @GetMapping("/chat")
    public String chat(@RequestParam("userPrompt") String userPrompt){
        System.out.println("userPrompt:"+userPrompt);
        ChatClient.CallResponseSpec response = chatClient.prompt()
                .user(userPrompt).call();
        String content = response.content();
        return content;
    }
}
