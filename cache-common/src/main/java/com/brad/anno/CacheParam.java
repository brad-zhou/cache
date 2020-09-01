package com.brad.anno;

import java.lang.annotation.*;

/**
 * 缓存的参数注解
 * <p>
 * 该注解用于组装缓存中的key
 * <p>
 * 注解的类型
 * 1. 字符串或者基本数据类型，则将该值直接组装key
 * 2. 集合类型，则将结合按照顺序遍历，将值组装key
 * 3. 对象类型, 将该对象中不为null的对象，按照顺序遍历，将值组装key
 * <p>
 * 如果方法的参数中没有该注解，则当方法执行时，不以参数中的属性作为缓存索引key，默认使用类名+索引值为缓存的key的前缀
 * <p>
 * 该注解可以添加在在多个参数上，需要指定参数名称
 */
@Target(value = {ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface CacheParam {
    /**
     * 返回参数的名称
     * <p>
     * 该名称将做为缓存对象的key的一部分
     * 如果该值为空，则默认是方法的参数名称
     *
     * @return
     */
    String value() default "";
}
