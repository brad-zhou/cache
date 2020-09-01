package com.brad.config;

import com.brad.utils.redis.RedisUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis配置类
 */
@Configuration
public class RedisUtilAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public RedisUtils redisUtils() {
        return new RedisUtils();
    }
}
