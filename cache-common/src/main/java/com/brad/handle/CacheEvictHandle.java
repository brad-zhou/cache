package com.brad.handle;

import com.brad.anno.CacheEvict;
import com.brad.bean.Cache;
import com.brad.bean.CacheMap;
import com.brad.bean.CacheRelevance;
import com.brad.utils.string.StringUtil;

import java.lang.reflect.Method;

/**
 * CacheEvictHandle
 */
public class CacheEvictHandle extends AbstractCacheHandle {
    /**
     * 方法
     */
    private Method method;
    /**
     * 缓存删除注解
     */
    private CacheEvict annotation;

    public CacheEvictHandle(Method method) {
        super(method, Boolean.FALSE);
        this.method = method;
        this.annotation = method.getDeclaredAnnotation(CacheEvict.class);
    }

    /**
     * @return
     */
    @Override
    public String getCacheIndex() {
        String cacheIndex = annotation.cacheIndex();
        if (StringUtil.isNullOrEmpty(cacheIndex)) {
            cacheIndex = method.getName();
        }
        return cacheIndex;
    }

    /**
     * 校验缓存索引和方法所属类全路径名称是否联合唯一
     */
    @Override
    public void validate() {
        Cache cache = CacheMap.evictCacheMap.get(super.getCacheKey());
        if (!StringUtil.isNullOrEmpty(cache)) {
            throw new RuntimeException("方法：" + super.getClazz().getName() + "." + this.method.getName() + " 索引值重复");
        }
    }

    /**
     * 返回缓存删除对象
     *
     * @return
     */
    @Override
    public Cache getCache() {
        // 缓存对象构建器
        Cache.Builder builder = new Cache.Builder(super.getClazz().getName(), this.getCacheIndex());

        CacheEvict.Relevance[] relevances = annotation.relevance();
        CacheRelevance[] cacheRelevances = new CacheRelevance[relevances.length];
        if (null != relevances) {
            int index = 0;
            CacheRelevance cacheRelevance = null;
            for (CacheEvict.Relevance relevance : relevances) {
                cacheRelevance = new CacheRelevance();
                cacheRelevance.setRelevanceIndex(relevance.relevanceIndex());
                cacheRelevance.setRelevanceClass(relevance.relevanceClass());
                cacheRelevances[index] = cacheRelevance;
                index++;
            }
        }
        builder.evictIndexs(cacheRelevances);
        return builder.builder();
    }
}
