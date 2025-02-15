package com.arthur.webchat.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedisConfig {

    @Value("${redis.database:-1}")
    Integer redisDatabase;
    @Value("${redis.password:}")
    String password;
    @Value("${redis.host:localhost}")
    String host;
    @Value("${redis.port:6379}")
    Integer port;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);

        if (!password.trim().isEmpty()) {
            redisStandaloneConfiguration.setPassword(password);
        }
        if(redisDatabase>0){
            redisStandaloneConfiguration.setDatabase(redisDatabase);
        }

        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public <F, S> RedisTemplate<F, S> redisTemplate() {
        final RedisTemplate<F, S> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public <F, S> HashOperations<F, String, S> hashOperations(RedisTemplate<F, S> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    public <F, S> SetOperations<F, S> setOperations(RedisTemplate<F, S> redisTemplate) {
        return redisTemplate.opsForSet();
    }
}
