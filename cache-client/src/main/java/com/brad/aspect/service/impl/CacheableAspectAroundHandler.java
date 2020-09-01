package com.brad.aspect.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.brad.anno.CacheParam;
import com.brad.anno.Cacheable;
import com.brad.aspect.service.AbstractAspectAroundHandler;
import com.brad.bean.AspectAroundHandleResult;
import com.brad.bean.Cache;
import com.brad.bean.CacheMap;
import com.brad.constants.Constants;
import com.brad.handle.CacheableHandle;
import com.brad.util.CacheUtils;
import com.brad.util.ReflectUtils;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 缓存查询的AOP处理
 * 1. 获取Method以及方法上的注解，并且组装key
 * 2. 判断是否在缓存内存中存在 如果不在的话，则说明该方法不支持缓存
 * 3. 根据@CacheParam 注解组装查询缓存的paramKey
 * 4. 查询是否在缓存中存在
 * 5. 如果不存在则需要执行方法获取数据
 * 6. 如果存在则直接返回
 * 7. 方法执行之后，看看是否需要存储到redis缓存中
 */
public class CacheableAspectAroundHandler extends AbstractAspectAroundHandler {
    public static final Logger logger = LoggerFactory.getLogger(CacheableAspectAroundHandler.class);

    /**
     * 缓存内存的对象
     */
    private Cache cache;
    /**
     * 方法的参数上添加 注解 @RxCacheParam 的值
     * key 是参数名称
     * value 是参数值
     */
    private LinkedHashMap<String, Object> args;
    private JoinPoint joinPoint;
    /**
     * 是否支持缓存
     * true 支持缓存 false 不支持缓存
     */
    private boolean isSupportCache;
    /**
     * 缓存的key
     */
    private String key;

    /**
     * 缓存处理的结果
     */
    private AspectAroundHandleResult result;

    public CacheableAspectAroundHandler(JoinPoint joinPoint) {
        super(joinPoint);
        this.joinPoint = joinPoint;
    }

    /**
     * 查看是否存在缓存内存中
     *
     * @return true 存在 false 不存在
     */
    @Override
    public boolean isExistsInMem() {
        this.cache = CacheMap.cacheMap.get(this.getKey());
        if (StringUtils.isEmpty(cache)) {
            logger.info("put rxcache index key to local memory....");
            this.parseForRxCacheable();
            this.cache = CacheMap.cacheMap.get(this.getKey());
            if (StringUtils.isEmpty(this.cache)) {
                logger.info(this.getKey() + " not support rxcache....");
                return false;
            }
        }
        return true;
    }

    /**
     * 解析注解中含有@RxCacheable的方法，并且将对应的注解元素解析成RxCache对象
     */
    private void parseForRxCacheable() {
        List<Method> rxCacheables = ReflectUtils.getMethodsByAnnotation(super.method.getDeclaringClass(), Cacheable.class);
        if (!CollectionUtils.isEmpty(rxCacheables)) {
            logger.info("rxCacheables:{}", JSONArray.toJSONString(rxCacheables));
            for (Method method : rxCacheables) {
                CacheableHandle handle = new CacheableHandle(method);
                handle.handle();
            }
        }
    }

    /**
     * 获取方法的参数上添加注解 @RxCacheParam 的参数值
     * <p>
     * 如果参数上没有添加注解，则返回false 不走缓存系统
     * <p>
     * 如果多个参数上都添加注解，则将每个注解对应的值都缓存
     */
    public void initParameterValue() {
        // 判断方法的参数是否添加了 @RxCacheParam 注解
        // 如果没有添加注解，则不走缓存系统
        isSupportCache = ReflectUtils.isParamAnnotationExist(super.getMethod(), CacheParam.class);

        if (isSupportCache) {
            args = ReflectUtils.getParamAnnotationValue(super.getMethod(), CacheParam.class, this.joinPoint.getArgs());
            logger.info(this.getKey() + "'s parameter map : {}", JSONObject.toJSONString(args));
        } else {
            logger.info(this.getKey() + " 's parameter not add annotation : @RxCacheParam ...");
        }
    }

    /**
     * 组装缓存内存中的key
     *
     * @return
     */
    @Override
    public String getKey() {
        return CacheUtils.getCacheableKeyByMethod(super.getMethod());
    }

    /**
     * 1. 看看执行的方法是否需要走缓存路径 看看方法参数上是否有添加注解，如果没有添加注解，则执行方法
     * 2. 查询缓存的key 再添加参数条件的限制，获取新的key
     * 3. 根据最新的key 查询缓存中有值
     *
     * @return
     */
    @Override
    public AspectAroundHandleResult beforeProcess() {
        result = new AspectAroundHandleResult();
        result.setMethod(super.getMethod());
        // 判断是否支持缓存系统 不支持直接返回
        if (!this.isExistsInMem()) {
            result.setSupportCache(false);
            return result;
        }
        result.setSupportCache(true);
        // 组装最新的key
        this.initParameterValue();
        String paramKey = CacheUtils.parseParameterValueForKey(this.args);
        if (!StringUtils.isEmpty(paramKey)) {
            this.key = this.getKey() + CacheMap.CACHE_SEPARATOR + paramKey;
        } else {
            this.key = this.getKey();
        }
        // 将key缓存起来
        logger.info(this.getKey() + " 's lastest redis cache key : {}", this.key);
        // 根据key 到redis查询
        String value = super.redisService.get(this.key);
        logger.info("depend on the key: {} get the redis cache data : {}", this.key, value);
        if (StringUtils.isEmpty(value)) {
            // 查询不到需要执行方法来查询
            result.setExecute(true);
        } else {
            result.setExecute(false);
            result.setCacheData(value);
        }

        return result;
    }

    /**
     * 方法执行后的处理
     *
     * @param data
     */
    @Override
    public void afterProcess(Object data) {

        // 如果不支持缓存，或者不执行方法 则不需要将数据再次缓存到redis分布式缓存中
        if (!result.isSupportCache() || !result.isExecute()) {
            return;
        }
        logger.info("save redis key: {}", this.key);
        // 如果查询出来的是null 也需要对空进行缓存，反之缓存穿透现象
        if (StringUtils.isEmpty(data)) {
            logger.info("the key: {} ,save null value to redis ", this.key);
            // 空值的缓存 需要设置定义的空值失效时间
            this.redisService.save(this.key, "null", Constants.NULL_EXPIRETIME);
        } else {
            // 针对数据类型是集合类型的做处理
            if (data instanceof Collection) {
                String jsonArray = JSONArray.toJSONString(data, SerializerFeature.WriteMapNullValue,
                        SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteDateUseDateFormat);
                this.redisService.save(this.key, jsonArray, cache.getExpireTime());
            } else {
                String jsonObject = JSONObject.toJSONString(data, SerializerFeature.WriteMapNullValue,
                        SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteDateUseDateFormat);
                this.redisService.save(this.key, jsonObject, cache.getExpireTime());
            }
        }
    }
}
