package com.brad.aspect.service;

import com.brad.service.RedisService;
import com.brad.util.AopUtils;
import com.brad.util.ApplicationSpringContextUtils;
import org.aspectj.lang.JoinPoint;

import java.lang.reflect.Method;

/**
 * 切面拦截抽象处理器
 * <p>
 * 主要进行如下的通用处理
 * 1. 获取拦截方法上的注解
 * 2. 根据注解的值组装
 */
public abstract class AbstractAspectAroundHandler implements AspectProcess {

    private JoinPoint joinPoint;
    /**
     * 需要拦截的方法
     */
    protected Method method;

    /**
     * 方法所在的类对象
     */
    protected Class<?> clazz;

    protected RedisService redisService = ApplicationSpringContextUtils.getBean(RedisService.class);

    public AbstractAspectAroundHandler(JoinPoint joinPoint) {
        this.joinPoint = joinPoint;
        method = getMethod();
        this.clazz = method.getDeclaringClass();
    }

    /**
     * 是否存在于缓存内存中
     *
     * @return true 存在 false 不存在
     */
    public abstract boolean isExistsInMem();

    /**
     * 获取拦截的方法
     *
     * @return
     */
    public Method getMethod() {
        return AopUtils.getMethod(joinPoint);
    }

}
