package org.secwallet.core.cache.redis;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

public abstract class AbstractBaseRedis<K, V> {
    @Resource(name = "redisTemplate")
    protected RedisTemplate<K, V> redisTemplate;

    /**
     * set redisTemplate
     *
     * @param redisTemplate
     */
    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
