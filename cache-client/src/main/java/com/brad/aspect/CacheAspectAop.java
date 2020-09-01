package com.brad.aspect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.brad.anno.CacheEvict;
import com.brad.anno.Cacheable;
import com.brad.aspect.service.AbstractAspectAroundHandler;
import com.brad.aspect.service.impl.CacheEvictAspectAroundHandler;
import com.brad.aspect.service.impl.CacheableAspectAroundHandler;
import com.brad.bean.AspectAroundHandleResult;
import com.brad.bean.CacheMap;
import com.brad.exception.ApplicationException;
import com.brad.util.AopUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * 针对 执行方法上添加了注解@Cacheable 和 @CacheEvict 进行拦截
 * <p>
 * 在方法执行之前，通过组装key到redis中获取对应的值，
 * <p>
 * 如果存在值则直接返回，否则执行方法，获取方法返回值，并且将該值保存到redis缓存中
 */
@Component
@Aspect
@ComponentScan
@EnableAspectJAutoProxy
public class CacheAspectAop {

    public static final Logger logger = LoggerFactory.getLogger(CacheAspectAop.class);

    public static final String LEFT_ANGLE_BRACKET = "<";
    public static final String RIGHT_ANGLE_BRACKET = ">";
    /**
     * 是否开启缓存
     */
    @Value("${cache.enabled:false}")
    public Boolean cacheEnabled;

    /**
     * 对所有方法注解了 Cacheable 和 CacheEvict 进行拦截
     */
    @Pointcut("@annotation(com.brad.anno.Cacheable) " +
            " || @annotation(com.brad.anno.CacheEvict)")
    public void intercepter() {
    }

    /**
     * 判断当前是否是 缓存查询 还是 缓存删除
     *
     * @param joinPoint
     * @return true 缓存查询  false 缓存删除  null 表示没有添加相关相关的注解，不走缓存业务逻辑
     */
    private Boolean isCacheableByAnnotation(JoinPoint joinPoint) {
        Boolean result = null;
        Method method = AopUtils.getMethod(joinPoint);
        if (StringUtils.isEmpty(method)) {
            return null;
        }

        boolean flag = method.isAnnotationPresent(Cacheable.class) || method.isAnnotationPresent(CacheEvict.class);
        if (flag) {
            return method.isAnnotationPresent(Cacheable.class) ? Boolean.TRUE : Boolean.FALSE;
        }

        return null;
    }

    /**
     * 对方法执行前后进行拦截
     * <p>
     * 1. 先判断方法是否执行缓存业务逻辑  如果不支持，则直接执行方法返回
     * <p>
     * 2. 通过判断方法上的注解，判断执行缓存查询还是缓存删除
     * <p>
     * 3. 进行方法处理之前和处理之后的业务逻辑
     * <p>
     * 注意：
     * 拦截时，如果直接执行了方法，则process()方法执行的返回值可以直接用于返回，不必进行任务处理
     * 但是如果方法没有执行，直接从缓存中获取数据，由于缓存中数据保存的是String类型的，所以需要对数据进行处理
     * <p>
     * 此处需要注意，如果返回值类型是集合类型的，则需要进行特殊处理（目前处理仅支持 List和Set 集合）
     *
     * @param proceedingJoinPoint
     * @return
     */
    @Around("intercepter()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        logger.info("enter intercepter cache method....");
        try {
            if (!cacheEnabled) {
                logger.info("不开启缓存....");
                return proceedingJoinPoint.proceed();
            }
            // 判断当前是否是 缓存查询 还是 缓存删除
            // 如果是 true 则表示是缓存查询， 如果是false 则是缓存删除 如果返回null 在说明被拦截的方法不支持缓存
            // 由于AOP是拦截自定义注解，所以此方法返回值不会为空，如果为空了，则说明程序异常，需要定位下原因
            Boolean isSupport = this.isCacheableByAnnotation(proceedingJoinPoint);

            // 如果不支持缓存，则直接执行方法返回，不走对应的缓存逻辑
            if (StringUtils.isEmpty(isSupport)) {
                return proceedingJoinPoint.proceed();
            }

            AbstractAspectAroundHandler handler = null;
            if (isSupport) {
                // 缓存查询处理器
                handler = new CacheableAspectAroundHandler(proceedingJoinPoint);
            } else {
                // 缓存删除处理器
                handler = new CacheEvictAspectAroundHandler(proceedingJoinPoint);
            }

            // 方法执行之前处理，判断是否需要执行方法
            // 缓存查询和缓存删除各自进行前置处理
            // 缓存查询中，根据缓存的索引key到缓存中查询是否存在对应的值，如果存在，则直接返回该值，并且是否执行方法设置为false，表示不执行被拦截的方法
            // 缓存删除中，根据自定义注解中配置的规则，删除关联的缓存索引key，并且是否执行方法设置为true，依旧执行该方法
            AspectAroundHandleResult result = handler.beforeProcess();
            logger.info("cache handle result:{}", result.toString());
            logger.info("cache mem map: {}", isSupport ? CacheMap.cacheMap : CacheMap.evictCacheMap);

            Object data = null;

            // 不支持缓存或者没有缓存都需要执行方法
            if (!result.isSupportCache() || result.isExecute()) {
                logger.info(" cache execute intercept method....");
                data = proceedingJoinPoint.proceed();
            } else {
                logger.info(" cache get data from redis cache....");
                data = result.getCacheData();
            }

            // 方法执行之后， 决定是否需要将返回值存储到redis缓存中
            handler.afterProcess(data);
            /**
             * 这里如果执行了方法，则data类型中包含方法返回的类型，可以直接返回，不必进行处理
             * 如果不执行方法，直接从缓存中缓存数据，由于缓存中的数据是字符串类型的，则需要进行数据处理
             */
            if (result.isExecute()) {
                return data;
            }
            // 获取方法的返回值
            Class<?> clazz = result.getMethod().getReturnType();
            // 返回值需要判断是否集合类
            if (clazz == List.class || clazz == Set.class) {
                Class<?> typeClazz = this.parseTypeForClass(result.getMethod());
                return JSONArray.parseArray((String) data, typeClazz);
            } else {
                return JSONObject.parseObject((String) data, clazz);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new ApplicationException(throwable.getMessage());
        }
    }

    /**
     * 解析获取类型中的泛型Class类对象
     *
     * @param method
     * @return
     * @throws ClassNotFoundException
     */
    private Class<?> parseTypeForClass(Method method) throws ClassNotFoundException {
        Type type = method.getGenericReturnType();
        String clazzName = this.parseTypeForClassString(type.getTypeName());
        if (StringUtils.isEmpty(clazzName)) {
            return null;
        }
        return Class.forName(clazzName);

    }

    /**
     * 截取字符串中 <> 之间的字符串
     *
     * @param typeString
     * @return
     */
    private String parseTypeForClassString(String typeString) {
        if (typeString.indexOf(LEFT_ANGLE_BRACKET) > 0) {
            String string = typeString.substring(typeString.indexOf(LEFT_ANGLE_BRACKET) + 1, typeString.indexOf(RIGHT_ANGLE_BRACKET));
            return string;
        }
        return null;
    }

}
