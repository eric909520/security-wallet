package org.secwallet.core.cache;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface ICache {

    boolean delete(KeyPrefix prefix, String key);

    boolean delete(String key);


    boolean delete(KeyPrefix prefix, String... keys);


    boolean delete(KeyPrefix prefix, Collection<String> keys);


    boolean delete(KeyPrefix prefix);


    void expire(KeyPrefix prefix, String key, long time, TimeUnit timeUnit);


    void expire(KeyPrefix prefix, String key, Date date);


    long getExpire(KeyPrefix prefix, String key, TimeUnit timeUnit);


    long getExpire(KeyPrefix prefix, String key);


    boolean exists(KeyPrefix prefix, String key);


    boolean vSet(KeyPrefix prefix, String key, Object value);


    boolean vSet(KeyPrefix prefix, Object key, Object value, Long expireTime);

    boolean set(Object key, Object value, Long expireTime);


    Object vGet(KeyPrefix prefix, Object key);

    Object vGet(Object key);


    void hmSet(KeyPrefix prefix, Object key, Object hashKey, Object value);


    Long lPush(KeyPrefix prefix,String key, String value);

    Long lPush(String key, String value);

    Long lPushAll(KeyPrefix prefix,Object key, List values) ;


    Object rightPop(KeyPrefix prefix,String key);


    Boolean hasKey(KeyPrefix prefix,String key);

    Boolean tryLock(String key,String value,Long timeOut);

    Boolean checkRedis();
}
