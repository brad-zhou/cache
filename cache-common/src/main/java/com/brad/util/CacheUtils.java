package com.brad.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.brad.anno.CacheEvict;
import com.brad.anno.Cacheable;
import com.brad.bean.CacheMap;
import com.brad.utils.string.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CacheUtils
 */
public class CacheUtils {

    public static final Map<String, String> CACHE_PREFIX_MAP = new HashMap<>();
    public static final String CACHE_KEY = "sceo";

    /**
     * 初始化缓存的前缀
     *
     * @param prefix
     */
    public static void initCachePrefix(String prefix) {
        CACHE_PREFIX_MAP.put(CACHE_KEY, prefix);
    }

    /**
     * 获取缓存的前缀
     *
     * @return
     */
    public static String getCachePrefix() {
        return CACHE_PREFIX_MAP.get(CACHE_KEY);
    }

    /**
     * 返回方法上 @Cacheable 指定的索引值来组装 缓存键值
     *
     * @param method 指定的方法
     * @return
     */
    public static <T extends Annotation> String getCacheableKeyByMethod(Method method) {
        Cacheable Cacheable = method.getAnnotation(Cacheable.class);
        if (StringUtil.isNullOrEmpty(Cacheable)) {
            return null;
        }

        return method.getDeclaringClass().getName() + CacheMap.CACHE_SEPARATOR + getCacheIndex(method, Cacheable.cacheIndex());
    }

    /**
     * 返回方法上 @CacheEvict 指定的索引值来组装 缓存键值
     *
     * @param method 指定的方法
     * @return
     */
    public static <T extends Annotation> String getCacheEvictKeyByMethod(Method method) {
        CacheEvict CacheEvict = method.getAnnotation(CacheEvict.class);
        if (StringUtil.isNullOrEmpty(CacheEvict)) {
            return null;
        }
        return method.getDeclaringClass().getName() + CacheMap.CACHE_SEPARATOR + getCacheIndex(method, CacheEvict.cacheIndex());
    }

    /**
     * 获取缓存索引值
     * 如果索引值为空，则默认是方法名
     *
     * @param method
     * @param cacheIndex
     * @return
     */
    public static String getCacheIndex(Method method, String cacheIndex) {
        if (StringUtil.isNullOrEmpty(cacheIndex)) {
            cacheIndex = method.getName();
        }
        return cacheIndex;
    }

    /**
     * 将参数Map格式转换成字符串，过滤其中为null的对象
     *
     * @param paramsMap
     * @return
     */
    public static String parseParameterValueForKey(LinkedHashMap<String, Object> paramsMap) {
        if (null == paramsMap) {
            return null;
        }
        StringBuffer sb = new StringBuffer("");
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            /**
             * 为了减少redis中对应key的大小，将参数为空的去处掉，保证key的简洁性
             */
            sb.append(entry.getKey()).append(CacheMap.CACHE_SEPARATOR).append(JSON.toJSONString(entry.getValue(),
                    new SerializerFeature[]{SerializerFeature.WriteNullStringAsEmpty,
                            SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty}))
                    .append(CacheMap.CACHE_SEPARATOR);
        }
        if (!StringUtil.isNullOrEmpty(sb)) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
