package top.dddpeter.ishare.common.redis.util;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate valueOperations;

    /**
     * 默认过期时长，单位：秒
     */
//    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    public final static long DEFAULT_EXPIRE = 86400L;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;

    /**
     * 插入缓存默认时间
     *
     * @param key   键
     * @param value 值
     * @author zmr
     */
    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    /**
     * 插入缓存
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间(s)
     * @author zmr
     */
    public void set(String key, Object value, long expire) {
        valueOperations.opsForValue().set(key, toJson(value));
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 返回字符串结果
     *
     * @param key 键
     * @author zmr
     */
    public String get(String key) {
        return valueOperations.opsForValue().get(key);
    }

    /**
     * 返回指定类型结果
     *
     * @param key   键
     * @param clazz 类型class
     * @return
     * @author zmr
     */
    public <T> T get(String key, Class<T> clazz) {
        String value = valueOperations.opsForValue().get(key);
        return value == null ? null : fromJson(value, clazz);
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @author zmr
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 模糊删除缓存
     * @param prex 前缀
     */
    public void deleteByPrex(String prex) {
        Set<String> keys = redisTemplate.keys(prex+"*");
        if (null != keys && keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double
                || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }
}