package com.brad.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.brad.anno.Cache;
import com.brad.anno.CacheEvict;
import com.brad.anno.CacheParam;
import com.brad.anno.Cacheable;
import com.brad.bean.BaseDTO;
import com.brad.service.TestCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TestCacheServiceImpl
 */
@Service
@Cache
public class TestCacheServiceImpl implements TestCacheService {
    public static final Logger logger = LoggerFactory.getLogger(TestCacheServiceImpl.class);

    /**
     * 获取缓存
     *
     * @param id
     * @param name
     */
    @Cacheable(cacheIndex = "cache.get")
    @Override
    public String getCache(@CacheParam String id, @CacheParam String name) {
        logger.info("getCache" + System.currentTimeMillis());
        String random = "返回数据";
        return random;
    }

    /**
     * 删除缓存
     *
     * @param id
     */
    @CacheEvict(cacheIndex = "remove", relevance =
            {@CacheEvict.Relevance(relevanceIndex = "cache.*")})
    @Override
    public void removeCache(@CacheParam String id) {
        logger.info("removeCache:{}", System.currentTimeMillis());
    }

    /**
     * @param id
     * @return
     */
    @Cacheable(cacheIndex = "getListCache")
    @Override
    public List<BaseDTO> getListCache(@CacheParam("id") String id) {
        List<BaseDTO> list = new ArrayList<>();
        BaseDTO dto = new BaseDTO();
        dto.setName(id);
        dto.setValue(id);
        list.add(dto);
        return list;
    }

    @Override
    @Cacheable(cacheIndex = "cache.getJSONArrray")
    public JSONArray getJSONArray(@CacheParam String id) {
        logger.info("execute getJSONArray method ....");
        String[] arr = new String[]{"1", "2"};
        List<String> list = Arrays.asList(arr);
        JSONArray jsonArray = JSONArray.parseArray(JSONArray.toJSONString(list));
        return jsonArray;
    }
}
