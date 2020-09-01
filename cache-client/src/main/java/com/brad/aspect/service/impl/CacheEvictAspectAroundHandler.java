package com.brad.aspect.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.brad.anno.CacheEvict;
import com.brad.aspect.service.AbstractAspectAroundHandler;
import com.brad.bean.AspectAroundHandleResult;
import com.brad.bean.Cache;
import com.brad.bean.CacheMap;
import com.brad.handle.CacheEvictHandle;
import com.brad.process.RelevanceCacheIndexProcess;
import com.brad.service.RedisService;
import com.brad.util.ApplicationSpringContextUtils;
import com.brad.util.CacheUtils;
import com.brad.util.ReflectUtils;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 缓存删除的AOP处理
 * 1. 获取Method以及方法上的注解，根据注解中索引组装缓存索引key
 * <p>
 * 2. 判断是否在缓存内存中存在 如果不在的话，则说明该方法不支持缓存
 * <p>
 * 3. 根据@CacheEvict 中的relevance 关联，然后组装需要删除的缓存的key
 * 3.1 如果配置项中含有通配符，则根据通配符的规则来删除对应缓存
 * 3.2 如果配置项中不含有通配符，则根据key来删除对应的缓存
 * <p>
 * 4. 删除缓存之后，执行该方法
 **/
public class CacheEvictAspectAroundHandler extends AbstractAspectAroundHandler {
    public static final Logger logger = LoggerFactory.getLogger(CacheEvictAspectAroundHandler.class);

    /**
     * 缓存内存的对象
     */
    private Cache cache;
    /**
     * redis操作
     */
    private RedisService redisService = ApplicationSpringContextUtils.getApplicationContext().getBean(RedisService.class);
    /**
     * AOP拦截的切点
     */
    private JoinPoint joinPoint;

    public CacheEvictAspectAroundHandler(JoinPoint joinPoint) {
        super(joinPoint);
        this.joinPoint = joinPoint;
    }

    /**
     * 判断是否在内存中存在
     *
     * @return
     */
    @Override
    public boolean isExistsInMem() {
        // 根据key在内存中查询是否存在
        this.cache = CacheMap.evictCacheMap.get(this.getKey());
        if (StringUtils.isEmpty(cache)) {
            this.parseForCacheEvict();
            this.cache = CacheMap.evictCacheMap.get(this.getKey());
            if (StringUtils.isEmpty(cache)) {
                logger.info(this.getKey() + " not support cache....");
                return false;
            }
        }
        return true;
    }

    /**
     * 解析注解中含有@Cacheable的方法，并且将对应的注解元素解析成Cache对象
     */
    private void parseForCacheEvict() {
        List<Method> Cacheevicts = ReflectUtils.getMethodsByAnnotation(super.method.getDeclaringClass(), CacheEvict.class);
        if (!CollectionUtils.isEmpty(Cacheevicts)) {
            logger.info("Cacheables:{}", JSONArray.toJSONString(Cacheevicts));
            for (Method method : Cacheevicts) {
                CacheEvictHandle handle = new CacheEvictHandle(method);
                handle.handle();
            }
        }
    }

    /**
     * 根据缓存索引值来获取redis中的key值
     *
     * @return
     */
    @Override
    public String getKey() {
        return CacheUtils.getCacheEvictKeyByMethod(super.getMethod());
    }

    /**
     * 方法执行前，根据注解中关联的索引值来组装对应的key
     *
     * @return
     */
    @Override
    public AspectAroundHandleResult beforeProcess() {
        AspectAroundHandleResult result = new AspectAroundHandleResult();
        result.setMethod(super.getMethod());
        result.setExecute(Boolean.TRUE);
        if (!this.isExistsInMem()) {
            return result;
        }

        RelevanceCacheIndexProcess process = new RelevanceCacheIndexProcess(this.cache);
        List<String> evictKeys = process.handleForEvictKey();

        // 获取需要删除的关联的索引的值
        if (CollectionUtils.isEmpty(evictKeys)) {
            return result;
        }

        evictKeys.forEach(item -> {
            redisService.removeByRegEx(item);
        });
        return result;
    }

    /**
     * 方法执行之后，不做任何处理
     *
     * @param data
     */
    @Override
    public void afterProcess(Object data) {

    }
}
