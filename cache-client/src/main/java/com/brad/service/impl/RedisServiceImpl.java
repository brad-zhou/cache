package com.brad.service.impl;

import com.brad.anno.Cache;
import com.brad.bean.CacheMap;
import com.brad.constants.Constants;
import com.brad.service.RedisService;
import com.brad.util.CacheUtils;
import com.brad.util.RequestUtils;
import com.brad.utils.redis.RedisUtils;
import com.brad.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * redis操作接口实现类
 */
@Service
@Cache
public class RedisServiceImpl implements RedisService {

    public static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    /**
     * * 号
     */
    public static final String ASTERISK = "*";
    @Value("${saas.enabled:false}")
    private boolean sassEnable;

    /**
     * 根据key获取value
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        key = assemblingKey(key);
        logger.info("redis get key:{}", key);
        return RedisUtils.getValue(key);
    }

    /**
     * 保存 key-value 并且设定过期时间
     *
     * @param key
     * @param value
     * @param expirtTime
     * @return
     */
    @Override
    public boolean save(String key, String value, long expirtTime) {
        logger.info("redis save: key:{}", key);
        return RedisUtils.setValue(assemblingKey(key), value, expirtTime);
    }

    /**
     * 保存 key-value 使用默认过期时间
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean save(String key, String value) {
        key = assemblingKey(key);
        return RedisUtils.setValue(key, value, Constants.DEFAULT_EXPIRETIME);
    }

    /**
     * 是否包含key
     *
     * @param key
     * @return
     */
    @Override
    public boolean isContainsKey(String key) {
        return RedisUtils.existsValue(assemblingKey(key));
    }

    /**
     * 获取多租户标志
     *
     * @return
     */
    public String getTenantId() {
        return RequestUtils.getTenantId(sassEnable);
    }

    /**
     * 结合多租户标志组装key
     *
     * @param key
     * @return
     */
    @Override
    public String assemblingKey(String key) {
        String tenantId = getTenantId();
        if (StringUtil.isNullOrEmpty(tenantId)) {
            // 获取业务方执行的缓存前缀
            tenantId = CacheUtils.getCachePrefix();
            if (StringUtil.isNullOrEmpty(tenantId)) {
                return key;
            }
        }
        return tenantId + CacheMap.CACHE_SEPARATOR + key;
    }

    /**
     * 根据模糊匹配的规则来删除key-value
     *
     * @param pattern
     * @return
     */
    @Override
    public boolean removeByRegEx(String pattern) {
        pattern = this.assemblingKey(pattern);
        // 如果key中没有通配符的话，则直接添加 *
        if (pattern.indexOf(ASTERISK) < 0) {
            pattern += ASTERISK;
        }

        logger.info("redis removeByRegEx key:{}", pattern);
        /**
         * 根据规则获取对应的key
         */
        Set<String> keys = RedisUtils.getKeys(pattern);
        logger.info("according to redis key : {} get the result : {}", pattern, keys);
        if (CollectionUtils.isEmpty(keys)) {
            return Boolean.FALSE;
        }
        String[] keyArr = keys.toArray(new String[keys.size()]);
        RedisUtils.delete(keyArr);
        return Boolean.TRUE;
    }
}
