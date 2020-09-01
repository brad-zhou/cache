package com.brad.web;

import com.alibaba.fastjson.JSONArray;
import com.brad.bean.BaseDTO;
import com.brad.service.TestCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TestCacheController
 **/
@RestController
@RequestMapping("/test-cache")
public class TestCacheController {

    @Autowired
    TestCacheService cacheService;

    /**
     * 获取缓存
     *
     * @return
     */
    @GetMapping("/getCache/{id}/{name}")
    public String getCache(@PathVariable("id") String id,
                           @PathVariable("name") String name) {
        return cacheService.getCache(id, name);
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/getJSONArray/{id}")
    public JSONArray getJSONArray(@PathVariable("id") String id) {
        return cacheService.getJSONArray(id);
    }

    /**
     * 移除缓存
     *
     * @return
     */
    @GetMapping("/removeCache")
    public String removeCache() {
        cacheService.removeCache("1");
        return "ok";
    }

    @GetMapping("/getListCache/{id}")
    public List<BaseDTO> getListCache(@PathVariable("id") String id) {
        return cacheService.getListCache(id);
    }
}
