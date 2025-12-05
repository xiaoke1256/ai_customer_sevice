package com.xiaoke_1256.customerservice.controller;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {
    @Autowired
    private RedissonClient redissonClient;

    @PostMapping("/testRedisSet")
    public String testSetRedis() {
        //redissonClient.getBucket("test").set("1111");
        //redissonClient.getBucket("test2").set(222);
        redissonClient.getAtomicLong("test2").set(222);
        return "success";
    }

    @GetMapping("/testRedisGet")
    public String testGetRedis() {
        return String.valueOf(redissonClient.getAtomicLong("test2").get());
    }

}
