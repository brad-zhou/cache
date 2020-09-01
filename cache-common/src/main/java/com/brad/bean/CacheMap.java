package com.brad.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CacheMap
 */
public class CacheMap {

    /**
     * 缓存key的分隔符
     */
    public static final String CACHE_SEPARATOR = ".";
    /**
     * 默认容量大小
     */
    public static Integer DEFAULT_SIZE = 128;
    /**
     * 缓存查询
     */
    public static Map<String, Cache> cacheMap = new ConcurrentHashMap<>(DEFAULT_SIZE);
    /**
     * 缓存清除
     */
    public static Map<String, Cache> evictCacheMap = new ConcurrentHashMap<>(DEFAULT_SIZE);
}
