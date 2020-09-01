package com.brad.process;

import com.brad.bean.Cache;
import com.brad.bean.CacheMap;
import com.brad.bean.CacheRelevance;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 关联删除索引的处理
 **/
public class RelevanceCacheIndexProcess {
    /**
     * 缓存索引在内存中的存储值
     */
    private Cache cache;

    public RelevanceCacheIndexProcess(Cache cache) {
        this.cache = cache;
    }

    /**
     * 获取关联删除索引组装的key
     * 关联的key的组装方式：
     * 如果 relevanceClass 为空 则默认获取本方法所在的类全限定名
     * 如果 relevanceClass 不为空， 则获取指定类的类全限定名
     *
     * @return
     */
    public List<String> handleForEvictKey() {
        CacheRelevance[] cacheRelevances = cache.getEvictIndexs();
        if (null == cacheRelevances || cacheRelevances.length == 0) {
            return null;
        }

        List<String> evicts = new ArrayList<>(cacheRelevances.length);
        for (CacheRelevance relevance : cacheRelevances) {
            if (StringUtils.isEmpty(relevance.getRelevanceClass()) || Object.class == relevance.getRelevanceClass()) {
                evicts.add(cache.getClazzName() + CacheMap.CACHE_SEPARATOR + relevance.getRelevanceIndex());
            } else {
                evicts.add(relevance.getRelevanceClass().getName() + CacheMap.CACHE_SEPARATOR + relevance.getRelevanceIndex());
            }
        }
        return evicts;
    }
}
