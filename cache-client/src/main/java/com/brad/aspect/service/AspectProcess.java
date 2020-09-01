package com.brad.aspect.service;


import com.brad.bean.AspectAroundHandleResult;

/**
 * AspectProcess
 */
public interface AspectProcess {
    /**
     * 获取组装的key
     *
     * @return
     */
    String getKey();

    /**
     * 方法执行之前处理
     */
    AspectAroundHandleResult beforeProcess();

    /**
     * 方法执行之后处理
     *
     * @param data
     */
    void afterProcess(Object data);
}
