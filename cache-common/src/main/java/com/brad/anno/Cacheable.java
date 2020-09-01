package com.brad.anno;

import com.brad.constants.Constants;

import java.lang.annotation.*;

/**
 * 自定义缓存查询注解
 * <p>
 * 需要添加缓存的方法，可以在方法上添加该注解
 * <p>
 * 注意： @Cacheable 和 @CacheEvict 不能同时添加在同一方法类
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable {
    /**
     * 缓存的索引
     * 后期缓存更新的时候，通过指定该值删除关联的缓存
     * <p>
     * 该值的索引与添加该索引的类路径全限定名称联合唯一
     * <p>
     * 默认值是方法名
     * 注意： 这里如果不唯一的话，在方法名相同的情况下，后面的删除缓存就会存在误删除的情况
     * <p>
     * 优化：建议索引采用 关键字.索引值 的方式，这样在进行缓存更新时，可以同过通配符的方式来 关键字.* 的方式来一次性删除多个缓存值
     *
     * @return
     */
    String cacheIndex() default "";

    /**
     * 缓存失效时间
     * 默认是30分钟 单位 秒
     *
     * @return
     */
    int expireTime() default Constants.DEFAULT_EXPIRETIME;

}
