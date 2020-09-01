package com.brad.runner;

import com.brad.service.CachePrefix;
import com.brad.service.impl.RedisServiceImpl;
import com.brad.util.CacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * CacheRunner
 */
@Component
@ConditionalOnBean(value = CachePrefix.class)
public class CacheRunner implements ApplicationRunner {

    public static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    CachePrefix cachePrefix;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        String prefix = cachePrefix.getCachePrefix();
        logger.info("++++++++++++++++++++++++++:{}", prefix);
        CacheUtils.initCachePrefix(prefix);
    }
}
