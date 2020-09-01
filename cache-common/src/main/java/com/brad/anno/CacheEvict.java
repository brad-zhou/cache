package com.brad.anno;

import java.lang.annotation.*;

/**
 * 缓存删除
 * <p>
 * 方法添加该注解，则表明删除对应的缓存
 * <p>
 * 同时也可指明需要删除的关联缓存
 * <p>
 * 注意： @Cacheable 和 @CacheEvict 不能同时添加在同一方法类
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface CacheEvict {
    /**
     * 删除缓存的索引
     * <p>
     * 默认值是方法名
     * 注意： 缓存索引值和类路径全称联合唯一
     *
     * @return
     */
    String cacheIndex() default "";

    /**
     * 指定需要删除的关联的缓存
     * <p>
     * 可以指定多个需要删除的关联缓存
     *
     * @return
     */
    CacheEvict.Relevance[] relevance() default {};

    /**
     * 关联缓存
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({})
    public @interface Relevance {
        /**
         * 关联缓存索引的值
         * <p>
         * 用来指定 @Cacheable中配置的索引
         * <p>
         * 注意：
         * 该值可以使用通配符的方式来定义一组需要删除的关联缓存索引
         *
         * @return
         */
        String relevanceIndex() default "";

        /**
         * 如果需要删除非本类方法的缓存，则需要指明需要删除的查询缓存所在的类对象
         * 删除时，根据类全限定名称以及缓存索引名称来进行删除
         * <p>
         * 如果该值没有配置的话，默认是该注解所在方法的类路径全限定名称
         *
         * @return
         */
        Class<?> relevanceClass() default Object.class;
    }
}
