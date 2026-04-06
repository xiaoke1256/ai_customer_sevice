//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.xiaoke_1256.customerservice.common;

import com.alibaba.cloud.ai.memory.redis.serializer.MessageDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisChatMemoryRepository implements ChatMemoryRepository, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(RedisChatMemoryRepository.class);
    private static final String DEFAULT_KEY_PREFIX = "spring_ai_alibaba_chat_memory:";
    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;

    private RedisChatMemoryRepository(JedisPool jedisPool) {
        Assert.notNull(jedisPool, "jedisPool cannot be null");
        this.jedisPool = jedisPool;
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Message.class, new MessageDeserializer());
        this.objectMapper.registerModule(module);
    }

    public static RedisBuilder builder() {
        return new RedisBuilder();
    }

    @Override
    public List<String> findConversationIds() {
        Jedis jedis = this.jedisPool.getResource();

        try {
            List<String> keys = new ArrayList<>(jedis.keys("spring_ai_alibaba_chat_memory:*"));
            return keys.stream().map((key) -> key.substring("spring_ai_alibaba_chat_memory:".length())
            ).toList();
        } catch (Throwable t) {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Throwable ex) {
                    t.addSuppressed(ex);
                }
            }

            throw t;
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        Jedis jedis = this.jedisPool.getResource();

        try {
            String key = "spring_ai_alibaba_chat_memory:" + conversationId;
            List<String> messageStrings = jedis.lrange(key, 0L, -1L);
            List<Message> messages = new ArrayList();
            Iterator iterator = messageStrings.iterator();

            while(iterator.hasNext()) {
                String messageString = (String)iterator.next();

                try {
                    Message message = (Message)this.objectMapper.readValue(messageString, Message.class);
                    messages.add(message);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error deserializing message", e);
                }
            }

            return messages;
        } catch (Throwable t1) {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Throwable t2) {
                    t1.addSuppressed(t2);
                }
            }

            throw t1;
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }



    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        Assert.notNull(messages, "messages cannot be null");
        Assert.noNullElements(messages, "messages cannot contain null elements");
        Jedis jedis = this.jedisPool.getResource();

        try {
            String key = "spring_ai_alibaba_chat_memory:" + conversationId;
            this.deleteByConversationId(conversationId);
            Iterator iterator = messages.iterator();

            while(iterator.hasNext()) {
                Message message = (Message)iterator.next();

                try {
                    String messageJson = this.objectMapper.writeValueAsString(message);
                    jedis.rpush(key, new String[]{messageJson});
                } catch (JsonProcessingException var9) {
                    JsonProcessingException e = var9;
                    throw new RuntimeException("Error serializing message", e);
                }
            }
        } catch (Throwable t1) {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Throwable t2) {
                    t1.addSuppressed(t2);
                }
            }

            throw t1;
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    @Override
    public void deleteByConversationId(String conversationId) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        Jedis jedis = this.jedisPool.getResource();

        try {
            String key = "spring_ai_alibaba_chat_memory:" + conversationId;
            jedis.del(key);
        } catch (Throwable t1) {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Throwable t2) {
                    t1.addSuppressed(t2);
                }
            }

            throw t1;
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    public void clearOverLimit(String conversationId, int maxLimit, int deleteSize) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        Jedis jedis = this.jedisPool.getResource();

        try {
            String key = "spring_ai_alibaba_chat_memory:" + conversationId;
            List<String> all = jedis.lrange(key, 0L, -1L);
            if (all.size() >= maxLimit) {
                all = all.stream().skip((long)Math.max(0, deleteSize)).toList();
                this.deleteByConversationId(conversationId);
                Iterator iterator = all.iterator();

                while(iterator.hasNext()) {
                    String message = (String)iterator.next();
                    jedis.rpush(key, new String[]{message});
                }
            }
        } catch (Throwable t1) {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Throwable t2) {
                    t1.addSuppressed(t2);
                }
            }

            throw t1;
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    public void close() {
        if (this.jedisPool != null) {
            this.jedisPool.close();
            logger.info("Redis connection pool closed");
        }

    }

    public static class RedisBuilder {
        private String host = "127.0.0.1";
        private int port = 6379;
        private String password;
        private int timeout = 2000;
        private JedisPoolConfig poolConfig;

        public RedisBuilder() {
        }

        public RedisBuilder host(String host) {
            this.host = host;
            return this;
        }

        public RedisBuilder port(int port) {
            this.port = port;
            return this;
        }

        public RedisBuilder password(String password) {
            this.password = password;
            return this;
        }

        public RedisBuilder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public RedisBuilder poolConfig(JedisPoolConfig poolConfig) {
            this.poolConfig = poolConfig;
            return this;
        }

        public RedisChatMemoryRepository build() {
            if (this.poolConfig == null) {
                this.poolConfig = new JedisPoolConfig();
            }

            JedisPool jedisPool = new JedisPool(this.poolConfig, this.host, this.port, this.timeout, this.password);
            return new RedisChatMemoryRepository(jedisPool);
        }
    }
}
