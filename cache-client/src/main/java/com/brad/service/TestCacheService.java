package com.brad.service;

import com.alibaba.fastjson.JSONArray;
import com.brad.bean.BaseDTO;

import java.util.List;

/**
 * TestCacheService
 */
public interface TestCacheService {
    /**
     * 获取缓存
     *
     * @param id
     * @param name
     */
    String getCache(String id, String name);

    /**
     * 删除缓存
     *
     * @param id
     */
    void removeCache(String id);

    /**
     * @param id
     * @return
     */
    List<BaseDTO> getListCache(String id);

    /**
     * @param id
     * @return
     */
    JSONArray getJSONArray(String id);
}
