package com.brad.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by lulx on 2019/4/26 0026 下午 16:50
 *
 * @author lulx
 */
@Service
@Lazy(false)
public class RedisUtils {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplateHold;

    private static RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {
        redisTemplate = redisTemplateHold;
        logger.info("redisTemplateHold init");
    }

    /**
     * 设置数据 String类型
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setValue(String key, String value) {
        boolean retVal = true;
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            retVal = false;
            logger.error("some error occur when execute setValue function:", e);
            throw new RuntimeException("some error occur when execute setValue function:", e);
        }
        return retVal;
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public static boolean existsValue(String key) {
        boolean ret = false;
        try {
            ret = redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error("some error occur when execute existsValue function:", e);
            throw new RuntimeException("some error occur when execute existsValue function:", e);
        }
        return ret;
    }

    /**
     * 获取数据 String类型
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        String value = null;
        try {
            value = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error("some error occur when execute getValue function:", e);
            throw new RuntimeException("some error occur when execute getValue function:", e);
        }
        return value;
    }

    /**
     * 设置数据，并设置过期时间
     *
     * @param key
     * @param value
     * @param expireSec
     * @return
     */
    public static boolean setValue(String key, String value, long expireSec) {
        boolean retVal = true;
        try {
            redisTemplate.opsForValue().set(key, value, expireSec, TimeUnit.SECONDS);
        } catch (Exception e) {
            retVal = false;
            logger.error("some error occur when execute setValue function:", e);
            throw new RuntimeException("some error occur when execute setValue function:", e);
        }
        return retVal;
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param seconds
     */
    public static void expire(String key, long seconds) {
        try {
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("some error occur when execute expire function:", e);
            throw new RuntimeException("some error occur when execute expire function:", e);
        }
    }

    /**
     * 向set集合中插入String类型的数据
     *
     * @param key
     * @param values
     * @return
     */
    public static long add2set(String key, String... values) {
        long ret = 0L;
        try {
            ret = redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            logger.error("some error occur when execute add2set function:", e);
            throw new RuntimeException("some error occur when execute add2set function:", e);
        }
        return ret;
    }

    /**
     * 判断 member 元素是否是集合 key 的成员
     *
     * @param key
     * @param member
     * @return
     */
    public static boolean sismember(String key, String member) {
        boolean b = false;
        try {
            b = redisTemplate.opsForSet().isMember(key, member);
        } catch (Exception e) {
            logger.error("some error occur when execute sismember function:", e);
            throw new RuntimeException("some error occur when execute sismember function:", e);
        }
        return b;
    }

    /**
     * 移除集合中一个或多个成员
     *
     * @param key
     * @param member
     */
    public static void srem(String key, String... member) {
        try {
            redisTemplate.opsForSet().remove(key, member);
        } catch (Exception e) {
            logger.error("some error occur when execute srem function:", e);
            throw new RuntimeException("some error occur when execute srem function:", e);
        }
    }

    /**
     * 返回set集合中的所有元素
     *
     * @param key
     * @return
     */
    public static Set<String> smembers(String key) {
        Set<String> ret = null;
        try {
            ret = redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("some error occur when execute smembers function:", e);
            throw new RuntimeException("some error occur when execute smembers function:", e);
        }
        return ret;
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
     * 当 key 存在但不是字符串类型时，返回一个错误
     *
     * @param key
     * @param value
     * @return
     */
    public static String getSet(String key, String value) {
        String str = null;
        try {
            str = redisTemplate.opsForValue().getAndSet(key, value);
        } catch (Exception e) {
            logger.error("some error occur when execute getSet function:", e);
            throw new RuntimeException("some error occur when execute getSet function:", e);
        }
        return str;
    }

    /**
     * 重命名key
     *
     * @param oldKey
     * @param newKey
     * @return
     */
    public static void renameKey(String oldKey, String newKey) {
        try {
            redisTemplate.rename(oldKey, newKey);
        } catch (Exception e) {
            logger.error("some error occur when execute renameKey function:", e);
            throw new RuntimeException("some error occur when execute renameKey function:", e);
        }
    }

    /**
     * 获取所有满足pattern的key
     *
     * @param pattern
     * @return
     */
    public static Set<String> getKeys(String pattern) {
        Set<String> keys = null;
        try {
            keys = redisTemplate.keys(pattern);
        } catch (Exception e) {
            logger.error("some error occur when execute getKeys function:", e);
            throw new RuntimeException("some error occur when execute getKeys function:", e);
        }
        return keys;
    }

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    public static boolean delKey(String key) {
        boolean retVal = true;
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            retVal = false;
            logger.error("some error occur when execute delKey function:", e);
            throw new RuntimeException("some error occur when execute delKey function:", e);
        }
        return retVal;
    }

    /**
     * 设置计数器
     *
     * @param key
     * @return
     */
    public static Long setCounter(final String key) {
        Long increment = null;
        try {
            increment = redisTemplate.opsForValue().increment(key, 1);
        } catch (Exception e) {
            logger.error("some error occur when execute setCounter function:", e);
            throw new RuntimeException("some error occur when execute setCounter function:", e);
        }
        return increment;
    }

    /**
     * 设置计数器
     *
     * @param key
     * @param value
     * @return
     */
    public static Long setCounter(final String key, final long value) {
        Long increment = null;
        try {
            increment = redisTemplate.opsForValue().increment(key, value);
        } catch (Exception e) {
            logger.error("some error occur when execute setCounter function:", e);
            throw new RuntimeException("some error occur when execute setCounter function:", e);
        }
        return increment;
    }

    /**
     * 计数器加1
     *
     * @param key
     * @return
     */
    public static Long incrCounter(final String key) {
        long ret = 0;
        try {
            ret = redisTemplate.opsForValue().increment(key, 1L);
        } catch (Exception e) {
            logger.error("some error occur when execute incrCounter function:", e);
            throw new RuntimeException("some error occur when execute incrCounter function:", e);
        }
        return ret;
    }

    /**
     * 计数器加num
     *
     * @param key
     * @param num
     * @return
     */
    public static Long incrCounter(final String key, long num) {
        long ret = 0;
        try {
            ret = redisTemplate.opsForValue().increment(key, num);
        } catch (Exception e) {
            logger.error("some error occur when execute incrCounter function:", e);
            throw new RuntimeException("some error occur when execute incrCounter function:", e);
        }
        return ret;
    }

    /**
     * 删除key (key是String)
     *
     * @param keys
     */
    public static void delete(String... keys) {
        try {
            redisTemplate.delete(Arrays.asList(keys));
        } catch (Exception e) {
            logger.error("some error occur when execute delete function:", e);
            throw new RuntimeException("some error occur when execute delete function:", e);
        }
    }

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在
     * 若给定的 key 已经存在，则不做任何动作。
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setValueIfNotExist(final String key, final String value) {
        boolean ret = false;
        try {
            // 不存在key时才赋值，否则不覆盖
            ret = redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            logger.error("some error occur when execute setValueIfNotExist function:", e);
            throw new RuntimeException("some error occur when execute setValueIfNotExist function:", e);
        }
        return ret;
    }

    /**
     * 设置map
     *
     * @param key
     * @param map
     */
    public static void setMap(String key, Map<String, String> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            logger.error("some error occur when execute setHashMap function:", e);
            throw new RuntimeException("some error occur when execute setHashMap function:", e);
        }
    }

    /**
     * 获得保存的map
     *
     * @param key
     * @return
     */
    public static Map<Object, Object> getMap(String key) {
        Map<Object, Object> map = null;
        try {
            map = redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            logger.error("some error occur when execute getHashMap function:", e);
            throw new RuntimeException("some error occur when execute getHashMap function:", e);
        }
        return map;
    }

    /**
     * 从头部插入一个value元素到key as list
     *
     * @param key
     * @param value
     */
    public static Long pushList(String key, String value) {
        Long aLong = 0L;
        try {
            aLong = redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            logger.error("some error occur when execute pushList function:", e);
            throw new RuntimeException("some error occur when execute pushList function:", e);
        }
        return aLong;
    }

    /**
     * 从头部插入一个value元素到key as list
     *
     * @param key
     * @param value
     */
    public static Long leftPushList(String key, String value) {
        Long aLong = 0L;
        try {
            aLong = redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            logger.error("some error occur when execute pushList function:", e);
            throw new RuntimeException("some error occur when execute pushList function:", e);
        }
        return aLong;
    }

    /**
     * 从尾部插入一个value元素到key as list
     *
     * @param key
     * @param value
     */
    public static Long rightPushList(String key, String value) {
        Long aLong = 0L;
        try {
            aLong = redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            logger.error("some error occur when execute rightList function:", e);
            throw new RuntimeException("some error occur when execute rightList function:", e);
        }
        return aLong;
    }

    /**
     * 移除并获取list中最后一个元素
     *
     * @param key
     * @return
     */
    public static String lpop(String key) {
        String ret = null;
        try {
            ret = redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            logger.error("some error occur when execute rpop function:", e);
            throw new RuntimeException("some error occur when execute rpop function:", e);
        }
        return ret;
    }

    /**
     * 移除并获取list中第一个元素
     *
     * @param key
     * @return
     */
    public static String rpop(String key) {
        String ret = null;
        try {
            ret = redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            logger.error("some error occur when execute rpop function:", e);
            throw new RuntimeException("some error occur when execute rpop function:", e);
        }
        return ret;
    }

    /**
     * 保留list中前n个数据，其他的移除
     *
     * @param key
     * @param size
     */
    public static void trimList(String key, int size) {
        try {
            redisTemplate.opsForList().trim(key, 0, size - 1);
        } catch (Exception e) {
            logger.error("some error occur when execute trimList function:", e);
            throw new RuntimeException("some error occur when execute trimList function:", e);
        }
    }

    /**
     * 获得List中指定元素列表
     *
     * @param key
     * @param start 从0开始
     * @param end   -1为结尾
     * @return
     */
    public static List<String> rangeList(String key, int start, int end) {
        List<String> ret = null;
        try {
            ret = redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            logger.error("some error occur when execute rangeList function:", e);
            throw new RuntimeException("some error occur when execute rangeList function:", e);
        }
        return ret;
    }

    /**
     * 获得List
     *
     * @param key
     * @return
     */
    public static List<String> getList(String key) {
        List<String> ret = null;
        try {
            ret = redisTemplate.opsForList().range(key, 0, -1);
        } catch (Exception e) {
            logger.error("some error occur when execute rangeList function:", e);
            throw new RuntimeException("some error occur when execute rangeList function:", e);
        }
        return ret;
    }


    /**
     * 移除list中所有与value相等的值
     *
     * @param key
     * @param value
     * @return
     */
    public static long removeInList(String key, String value) {
        long ret = 0;
        try {
            ret = redisTemplate.opsForList().remove(key, 0, value);
        } catch (Exception e) {
            logger.error("some error occur when execute removeInList function:", e);
            throw new RuntimeException("some error occur when execute removeInList function:", e);
        }
        return ret;
    }

    /**
     * 发布消息
     *
     * @param channel
     * @param message
     */
    public static void publish(String channel, String message) {
        try {
            redisTemplate.convertAndSend(channel, message);
        } catch (Exception e) {
            logger.error("some error occur when execute publish function:", e);
            throw new RuntimeException("some error occur when execute publish function:", e);
        }
    }
}
