package com.brad.bean;


import com.brad.utils.string.StringUtil;

import java.lang.reflect.Method;

/**
 * 切面拦截处理的结果对象
 */
public class AspectAroundHandleResult {
    /**
     * 是否支持缓存
     */
    private Boolean isSupportCache;
    /**
     * 是否需要执行方法
     */
    private Boolean execute;
    /**
     * 缓存数据集
     */
    private String cacheData;
    /**
     * 拦截的方法
     */
    private Method method;

    public Boolean isExecute() {
        if (StringUtil.isNullOrEmpty(this.execute)) {
            return Boolean.TRUE;
        }
        return execute;
    }

    public void setExecute(Boolean execute) {
        this.execute = execute;
    }

    public String getCacheData() {
        return cacheData;
    }

    public void setCacheData(String cacheData) {
        this.cacheData = cacheData;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Boolean isSupportCache() {
        if (StringUtil.isNullOrEmpty(this.isSupportCache)) {
            return Boolean.FALSE;
        }
        return isSupportCache;
    }

    public void setSupportCache(Boolean supportCache) {
        isSupportCache = supportCache;
    }

    @Override
    public String toString() {
        return "AspectAroundHandleResult{" +
                "isSupportCache=" + isSupportCache +
                ", execute=" + execute +
                ", cacheData='" + cacheData + '\'' +
                ", method=" + method +
                '}';
    }
}
