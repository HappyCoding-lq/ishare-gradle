package top.dddpeter.ishare.common.redis.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import top.dddpeter.ishare.common.redis.annotation.RedisCache;
import top.dddpeter.ishare.common.redis.annotation.RedisEvict;
import top.dddpeter.ishare.common.redis.util.RedisUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Component
@Aspect
public class RedisAspect {
    private final static Logger logger = LoggerFactory.getLogger(RedisAspect.class);

    @Resource
    private RedisUtils redis;

    /**
     * 定义切入点，使用了 @RedisCache 的方法
     */
    @Pointcut("@annotation(top.dddpeter.ishare.common.redis.annotation.RedisCache)")
    public void redisCachePoint() {
    }

    @Pointcut("@annotation(top.dddpeter.ishare.common.redis.annotation.RedisEvict)")
    public void redisEvictPoint() {
    }

    @After("redisEvictPoint()")
    public void evict(JoinPoint point) {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        RedisEvict redisEvict = method.getAnnotation(RedisEvict.class);
        // 获取RedisCache注解
        String fieldKey = parseKey(redisEvict.fieldKey(), method, point.getArgs());
        //如果主键为空则根据前缀模糊删除缓存
        if (fieldKey.trim().isEmpty()) {
            String key = redisEvict.key()+":";
            logger.debug("<======切面模糊清除rediskey:{} ======>" + key);
            redis.deleteByPrex(key);
        }else {
            String rk = redisEvict.key() + ":" + fieldKey;
            logger.debug("<======切面清除rediskey:{} ======>" + rk);
            redis.delete(rk);
        }

    }

    /**
     * 环绕通知，方法拦截器
     */
    @Around("redisCachePoint()")
    public Object WriteReadFromRedis(ProceedingJoinPoint point) {
        try {
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            // 获取RedisCache注解
            RedisCache redisCache = method.getAnnotation(RedisCache.class);
            Class<?> returnType = ((MethodSignature) point.getSignature()).getReturnType();
            if (redisCache != null && redisCache.read()) {
                // 查询操作
                logger.debug("<======method:{} 进入 redisCache 切面 ======>", method.getName());
                String fieldKey = parseKey(redisCache.fieldKey(), method, point.getArgs());
                String rk = redisCache.key() + ":" + fieldKey;
                Object obj = redis.get(rk, returnType);
                if (obj == null) {
                    // Redis 中不存在，则从数据库中查找，并保存到 Redis
                    logger.debug("<====== Redis 中不存在该记录，从数据库查找 ======>");
                    obj = point.proceed();
                    if (obj != null) {
                        if (redisCache.expired() > 0) {
                            redis.set(rk, obj, redisCache.expired());
                        } else {
                            redis.set(rk, obj);
                        }
                    }
                }
                return obj;
            }
        } catch (Throwable ex) {
            logger.error("<====== RedisCache 执行异常: {} ======>", ex);
        }
        return null;
    }

    /**
     * 获取缓存的key
     * key 定义在注解上，支持SPEL表达式
     * 支持多个表达式 例如 #code1,#code2
     *
     * @param args
     * @param key
     * @return
     */
    private String parseKey(String key, Method method, Object[] args) {
        if (null == key || key.trim().isEmpty()) {
            return "";
        }
        // 获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        // 使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        // SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        StringBuffer value = new StringBuffer("");
        String[] keyArr = key.split(",");
        if (keyArr.length == 1) {
            value.append(key);
            value.append(":");
            value.append(parser.parseExpression(key).getValue(context, String.class));
            value.append(";");
        } else {
            for (String k : keyArr) {
                value.append(k);
                value.append(":");
                value.append(parser.parseExpression(k).getValue(context, String.class));
                value.append(";");
            }
        }
        return value.toString();
    }
}