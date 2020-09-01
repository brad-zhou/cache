package com.brad.anno;

import java.lang.annotation.*;

/**
 * 需要将类作为缓存类的，则添加该注解
 **/
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    /**
     * 缓存类的名称
     * <p>
     * 作用与 @Service 作用是一样的
     *
     * @return
     */
    String value() default "";
}
