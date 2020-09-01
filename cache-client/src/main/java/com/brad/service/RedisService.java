package com.brad.service;

/**
 * redis操作接口
 */
public interface RedisService {
    /**
     * 根据key 获取value值
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 保存key-value值，并且设定过期时间
     *
     * @param key
     * @param value
     * @param expirtTime
     * @return
     */
    boolean save(String key, String value, long expirtTime);

    /**
     * 保存 key-value 值 使用默认过期时间
     *
     * @param key
     * @param value
     * @return
     */
    boolean save(String key, String value);

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    boolean isContainsKey(String key);

    /**
     * 结合多租户标志来组装key
     *
     * @param key
     * @return
     */
    String assemblingKey(String key);

    /**
     * 通过模糊匹配来删除
     *
     * @param pattern
     * @return
     */
    boolean removeByRegEx(String pattern);
}
