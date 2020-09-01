package com.brad.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * AopUtils
 **/
public class AopUtils {
    /**
     * 根据切面拦截的方法获取Method对象
     *
     * @param joinPoint
     * @return
     */
    public static Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
