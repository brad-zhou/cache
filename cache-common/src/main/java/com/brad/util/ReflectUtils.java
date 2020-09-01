package com.brad.util;

import com.brad.utils.string.StringUtil;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 反射操作类
 */
public class ReflectUtils {

    public static final String DEFAULT_SEPARATOR = "value=";

    /**
     * 查询类中添加指定注解的方法
     *
     * @param clazz      指定的类对象
     * @param annotation 指定的注解对象集
     * @return
     */
    public static List<Method> getMethodsByAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        if (StringUtil.isNullOrEmpty(clazz) || StringUtil.isNullOrEmpty(annotation)) {
            return null;
        }
        Method[] methodArr = clazz.getDeclaredMethods();
        if (StringUtil.isNullOrEmpty(methodArr)) {
            return null;
        }
        List<Method> methods = new ArrayList<>(methodArr.length);
        for (Method method : methodArr) {
            method.setAccessible(Boolean.TRUE);
            if (method.isAnnotationPresent(annotation) && !method.isBridge()) {
                methods.add(method);
            }
        }
        return methods;
    }

    /**
     * 获取方法名称
     *
     * @param method Method的对象
     * @return
     */
    public static String getMethodName(Method method) {
        return method.getName();
    }

    /**
     * 获取指定方法上含有指定注解的参数的值
     * 如果一个方法含有多个指定的注解 则将每个注解的value值以及参数值 作为key-value
     * 如果 注解没有指定参数名称，则默认是使用方法的参数名称
     * <p>
     * 返回的键值对其中
     * key 是参数名称
     * value 是参数对应的值
     *
     * @param method 指定方法
     * @param ano    指定的注解
     * @param args   参数的参数
     * @return
     */
    public static <T extends Annotation> LinkedHashMap<String, Object> getParamAnnotationValue(Method method, Class<T> ano, Object[] args) {

        // 如果参数上没有注解，则直接返回
        Annotation[][] annotations = method.getParameterAnnotations();
        if (null == annotations) {
            return null;
        }

        LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        // 利用Spring的工具类 获取指定方法的参数名称
        String[] paramNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);

        // 这里使用LinkedHashMap 主要是为了保证参数的位置
        LinkedHashMap<String, Object> map = new LinkedHashMap<>(paramNames.length);
        Parameter[] params = method.getParameters();
        for (int i = 0, len = params.length; i < len; i++) {
            Parameter parameter = params[i];
            if (parameter.isAnnotationPresent(ano)) {
                T annotation = parameter.getAnnotation(ano);
                // 获取注解中的参数值
                String paramName = parse(annotation.toString());
                if (StringUtil.isNullOrEmpty(paramName)) {
                    paramName = paramNames[i];
                }
                map.put(paramName, args[i]);
            }
        }
        return map;
    }

    /**
     * 解析 Annotation中的value的对象
     * 该方法不完善，只能限制 注解中只有一个value()方法使用
     *
     * @param string
     * @return
     */
    private static String parse(String string) {
        if (null == string || "".equals(string)) {
            return null;
        }
        int len = string.indexOf(DEFAULT_SEPARATOR);
        return string.substring(len + 6, string.length() - 1);
    }

    /**
     * 指定方法上的参数，是否含有指定的注解
     *
     * @param method 指定的方法
     * @param ano    指定的方法参数上的注解
     * @return
     */
    public static boolean isParamAnnotationExist(Method method, Class<? extends Annotation> ano) {
        Annotation[][] annotationArr = method.getParameterAnnotations();
        if (null == annotationArr) {
            return false;
        }
        for (Annotation[] annotations : annotationArr) {
            for (Annotation annotation : annotations) {
                // 获取参数的注解类型
                if (annotation.annotationType().equals(ano)) {
                    return true;
                }
            }
        }
        return false;
    }

}
