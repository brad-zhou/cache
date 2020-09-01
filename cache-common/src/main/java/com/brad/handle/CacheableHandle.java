package com.brad.handle;

import com.brad.anno.Cacheable;
import com.brad.bean.Cache;
import com.brad.bean.CacheMap;
import com.brad.util.CacheUtils;
import com.brad.utils.string.StringUtil;

import java.lang.reflect.Method;

/**
 * CacheableHandle
 */
public class CacheableHandle extends AbstractCacheHandle {
    /**
     * 方法
     */
    private Method method;
    /**
     * 缓存查询的注解
     */
    private Cacheable annotation;

    public CacheableHandle(Method method) {
        super(method, Boolean.TRUE);
        this.method = method;
        this.annotation = method.getDeclaredAnnotation(Cacheable.class);
    }

    /**
     * 返回索引值
     * 如果没有指定索引值，则将默认将该方法名当成索引值
     *
     * @return
     */
    @Override
    public String getCacheIndex() {
        String cacheIndex = annotation.cacheIndex();
        return CacheUtils.getCacheIndex(method, cacheIndex);
    }

    /**
     * 校验缓存索引和方法所属类全路径名称是否联合唯一
     */
    @Override
    public void validate() {
        String cacheKey = super.getCacheKey();
        Cache cache = CacheMap.cacheMap.get(cacheKey);
        if (!StringUtil.isNullOrEmpty(cache)) {
            throw new RuntimeException("方法：" + super.getClazz().getName() + "." + this.method.getName() + " 索引值重复");
        }
    }

    /**
     * 返回缓存查询对象
     *
     * @return
     */
    @Override
    public Cache getCache() {
        Cache cache = new Cache.Builder(super.getClazz().getName(), annotation.cacheIndex())
                .expireTime(annotation.expireTime()).builder();
        return cache;
    }
}
