package com.brad.handle;


import com.brad.anno.CacheParam;
import com.brad.bean.Cache;
import com.brad.bean.CacheMap;
import com.brad.util.ReflectUtils;

import java.lang.reflect.Method;

/**
 * 缓存查询和缓存删除的抽象处理类
 */
public abstract class AbstractCacheHandle {
    /**
     * 含有 @Cacheable 或 @CacheEvict 注解的方法
     */
    private Method method;

    private Class<?> clazz;
    /**
     * 是否是缓存查询
     * true 缓存查询 false 缓存删除
     */
    private Boolean search;

    public AbstractCacheHandle(Method method, Boolean search) {
        this.method = method;
        this.search = search;
        this.clazz = method.getDeclaringClass();
    }

    /**
     * 返回方法的类对象
     *
     * @return
     */
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * 获取索引值
     *
     * @return
     */
    public abstract String getCacheIndex();

    /**
     * 缓存处理之前，各自的缓存进行自主校验
     * 如果校验不通过可以直接抛出异常返回
     * <p>
     * 主要校验内容
     * 1. 索引值与类名是否联合唯一
     */
    public abstract void validate();

    /**
     * 获取缓存的key
     *
     * @return
     */
    public String getCacheKey() {
        return clazz.getName() + CacheMap.CACHE_SEPARATOR + this.getCacheIndex();
    }

    /**
     * 判断方法参数是否含有 @CacheParam 的注解
     *
     * @return
     */
    public boolean isHaveCacheParam() {
        return ReflectUtils.isParamAnnotationExist(method, CacheParam.class);

    }

    /**
     * 处理，将缓存添加到内存中
     */
    public void handle() {
        // 校验 索引值和类名是否联合唯一
        this.validate();
        if (search) {
            CacheMap.cacheMap.put(getCacheKey(), getCache());
        } else {
            CacheMap.evictCacheMap.put(getCacheKey(), getCache());
        }
    }

    /**
     * 获取缓存对象
     *
     * @return
     */
    public abstract Cache getCache();

}
