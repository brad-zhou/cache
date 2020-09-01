package com.brad.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * 缓存模型
 */
public class Cache {
    /**
     * 缓存注解所在的类全限定名称
     */
    private String clazzName;
    /**
     * 缓存索引
     */
    private String cacheIndex;
    /**
     * 需要删除的索引名称
     */
    private CacheRelevance[] evictIndexs;
    /**
     * 记录id
     */
    private String id;
    /**
     * 失效时间
     * 如果为0 则表示永不失效
     */
    private long expireTime;
    /**
     * 集成多租户中的锐信号
     */
    private String sceo;

    public static class Builder {
        private String clazzName;
        private String cacheIndex;
        private CacheRelevance[] evictIndexs;
        private String id;
        private long expireTime;
        private String sceo;

        public Builder(String clazzName, String cacheIndex) {
            this.clazzName = clazzName;
            this.cacheIndex = cacheIndex;
        }

        public Builder evictIndexs(CacheRelevance[] evictIndexs) {
            this.evictIndexs = evictIndexs;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder expireTime(long expireTime) {
            this.expireTime = expireTime;
            return this;
        }

        public Builder sceo(String sceo) {
            this.sceo = sceo;
            return this;
        }

        public Cache builder() {
            return new Cache(this);
        }
    }

    private Cache(Builder builder) {
        this.cacheIndex = builder.cacheIndex;
        this.clazzName = builder.clazzName;
        this.evictIndexs = builder.evictIndexs;
        this.expireTime = builder.expireTime;
        this.id = builder.id;
        this.sceo = builder.sceo;
    }

    public String getClazzName() {
        return clazzName;
    }

    public String getCacheIndex() {
        return cacheIndex;
    }

    public CacheRelevance[] getEvictIndexs() {
        return evictIndexs;
    }

    public String getId() {
        return id;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public String getSceo() {
        return sceo;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public void setCacheIndex(String cacheIndex) {
        this.cacheIndex = cacheIndex;
    }

    public void setEvictIndexs(CacheRelevance[] evictIndexs) {
        this.evictIndexs = evictIndexs;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public void setSceo(String sceo) {
        this.sceo = sceo;
    }

    /**
     * 获取key
     *
     * @return
     */
    public String getKey() {
        return this.getClazzName() + CacheMap.CACHE_SEPARATOR + this.getCacheIndex();
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
