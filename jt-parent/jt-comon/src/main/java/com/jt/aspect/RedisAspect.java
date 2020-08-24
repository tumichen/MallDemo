package com.jt.aspect;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

@Aspect
@Component
@Slf4j
public class RedisAspect {
    //当spring容器加载时不会立即注入对象
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 规定:
     * 1.环绕通知必须使用ProceedingJoinPoint
     * 2
     *
     * @param joinPoint
     * @return
     */
    //该切入点表达式,方法名称必须匹配
    @Around(value = "@annotation(cache_Find)")
    public Object around(ProceedingJoinPoint joinPoint, Cache_Find cache_Find) {
        //动态的获取key
        String key = getKey(joinPoint, cache_Find);
        String resultJson = jedisCluster.get(key);
        Object resultData = null;
        try {
            if (StringUtils.isEmpty(resultJson)) {
                //缓存没有,调用目标方法
                resultData = joinPoint.proceed();
                String json = ObjectMapperUtil.toJSON(resultData);
                //判断当前类型是否有失效时间
                if (cache_Find.secondes() == 0) {
                    jedisCluster.set(key, json);
                } else {
                    jedisCluster.setex(key, cache_Find.secondes(), json);
                }
            }else {
                Class returnType = getClass(joinPoint);
                resultData = ObjectMapperUtil.toObject(resultJson,returnType);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        }
        return resultData;
    }
    //获得方法的返回值
    private Class getClass(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getReturnType();
    }

    /**
     * key的规则,如果是auto,则自动生成key,
     *
     * @param joinPoint
     * @param cache_find
     * @return
     */
    private String getKey(ProceedingJoinPoint joinPoint, Cache_Find cache_find) {
        if (KEY_ENUM.EMTY.equals(cache_find.keyType())) {
            return cache_find.key();
        }
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取第一个参数
        String arg0 = String.valueOf(joinPoint.getArgs()[0]);
        return new StringBuilder().append(methodName).append("::").append(arg0).toString();

    }
}
