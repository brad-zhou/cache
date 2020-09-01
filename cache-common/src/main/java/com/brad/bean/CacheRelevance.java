package com.brad.bean;


import com.brad.constants.Constants;
import com.brad.utils.string.StringUtil;

/**
 * CacheRelevance
 */
public class CacheRelevance {
    /**
     * 关联缓存索引的值
     *
     * @return
     */
    private String relevanceIndex;

    /**
     * 如果需要删除非本类方法的缓存，则需要指明需要删除的查询缓存所在的类对象
     * 删除时，根据类对象以及缓存索引名称来进行删除
     *
     * @return
     */
    private Class<?> relevanceClass;
    /**
     * 是否含有通配符
     */
    private Boolean isWildcard;

    public String getRelevanceIndex() {
        return relevanceIndex;
    }

    public void setRelevanceIndex(String relevanceIndex) {
        this.relevanceIndex = relevanceIndex;
    }

    public Class<?> getRelevanceClass() {
        return relevanceClass;
    }

    public void setRelevanceClass(Class<?> relevanceClass) {
        this.relevanceClass = relevanceClass;
    }

    /**
     * 返回是否含有通配符
     *
     * @return
     */
    public Boolean getWildcard() {
        if (StringUtil.isNullOrEmpty(this.relevanceIndex)) {
            return Boolean.FALSE;
        }
        if (this.relevanceIndex.contains(Constants.WILDCARD_SEPERATOR)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void setWildcard(Boolean wildcard) {
        isWildcard = wildcard;
    }

    @Override
    public String toString() {
        return "CacheRelevance{" +
                "relevanceIndex='" + relevanceIndex + '\'' +
                ", relevanceClass=" + relevanceClass +
                '}';
    }
}
