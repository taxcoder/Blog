package com.tanx.blog.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @description: redis配置类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/28 11:04
 */
@Configuration
public class RedisConfiguration {

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(factory);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        //使用Jackson2JsonRedisSerialize替换默认序列化
        Jackson2JsonRedisSerializer<?> JsonSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        //转义完才能使用

        //创建string的序列化
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        //key采用string序列化方式
        template.setKeySerializer(stringSerializer);
        //hash的key采用string序列化方式
        template.setHashKeySerializer(stringSerializer);

        //value采用Jackson序列化方式
        template.setValueSerializer(JsonSerializer);
        //hash的value采用Jackson序列化方式
        template.setHashValueSerializer(JsonSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
