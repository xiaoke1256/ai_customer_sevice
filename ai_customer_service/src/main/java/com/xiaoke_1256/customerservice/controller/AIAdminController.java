package com.xiaoke_1256.customerservice.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

@RestController
@RequestMapping("/aiAdmin")
public class AIAdminController {

    @Autowired
    private VectorStore vectorStore;

    @PostMapping("/importProductTypes")
    public String getVectorStore(){
        InputStream inputStream = AIAdminController.class.getResourceAsStream("/product_types.md");
        String content = "";
        try {
            content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Document doc = new Document("productTypes", content, new HashMap<String, Object>());
        vectorStore.add(Arrays.asList(doc));
        return "success!";
    }
}
