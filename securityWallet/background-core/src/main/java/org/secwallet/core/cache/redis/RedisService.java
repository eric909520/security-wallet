package org.secwallet.core.cache.redis;


import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.secwallet.core.cache.ICache;
import org.secwallet.core.cache.KeyPrefix;
import org.secwallet.core.exception.CTException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.MultiKeyCommands;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Redis service
 * String realKey=prefix.getPrefix()+key;
 */
@Slf4j
@Repository("redisService")
@Scope(value= ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RedisService extends AbstractBaseRedis<Object, Object> implements ICache {

    @Override
    public boolean delete(KeyPrefix prefix, String key) {
        try {
            String realKey=prefix.getPrefix()+key;
            return this.redisTemplate.delete(realKey);
        }catch (CTException e){
            throw e;
        }
    }

    @Override
    public boolean delete(String key) {
        try {
            return this.redisTemplate.delete(key);
        }catch (CTException e){
            throw e;
        }
    }

    @Override
    public boolean delete(KeyPrefix prefix, String... keys) {
        try{
            Set<String> kSet = Stream.of(keys).map(k -> prefix.getPrefix()+k).collect(Collectors.toSet());
            return this.redisTemplate.delete(kSet);
        }catch (CTException e){
            throw e;
        }
    }

    @Override
    public boolean delete(KeyPrefix prefix, Collection<String> keys) {
        try{
            Set<String> kSet = keys.stream().map(k -> prefix.getPrefix()+k).collect(Collectors.toSet());
            return this.redisTemplate.delete(kSet);
        }catch (CTException e){
            throw e;
        }
    }

    @Override
    public boolean delete(KeyPrefix prefix) {
        try{
            Set<String> kSet=scan(prefix);
            return this.redisTemplate.delete(kSet);
        }catch (CTException e){
            throw e;
        }
    }

    @Override
    public void expire(KeyPrefix prefix, String key, long time, TimeUnit timeUnit) {
        try{
            String realKey=prefix.getPrefix()+key;
            redisTemplate.expire(realKey, time, timeUnit);
        }catch (CTException e){
            throw e;
        }
    }

    @Override
    public void expire(KeyPrefix prefix, String key, Date date) {
        try{
            String realKey=prefix.getPrefix()+key;
            redisTemplate.expireAt(realKey,date);
        }catch (CTException e){
            throw e;
        }
    }

    @Override
    public long getExpire(KeyPrefix prefix, String key, TimeUnit timeUnit) {
        try{
            String realKey=prefix.getPrefix()+key;
            return this.redisTemplate.getExpire(realKey,timeUnit);
        }catch (CTException e){
            throw e;
        }
    }

    @Override
    public long getExpire(KeyPrefix prefix, String key) {
        try{
            String realKey=prefix.getPrefix()+key;
            return this.redisTemplate.getExpire(realKey);
        }catch (CTException e){
            throw e;
        }
    }

    @Override
    public boolean exists(KeyPrefix prefix, String key) {
        try{
            String realKey=prefix.getPrefix()+key;
            return this.redisTemplate.hasKey(realKey);
        }catch (CTException e){
            throw e;
        }
    }

    @Override
    public boolean vSet(KeyPrefix prefix, String key, Object value) {
        try {
            String realKey = prefix.getPrefix() + key;
            ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
            operations.set(realKey, value,prefix.expireSeconds(),TimeUnit.SECONDS);
            return true;
        } catch (CTException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean vSet(KeyPrefix prefix, Object key, Object value, Long expireTime) {
        try {
            String realKey = prefix.getPrefix() + key;
            ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
            operations.set(realKey, value,expireTime,TimeUnit.SECONDS);
            return true;
        } catch (CTException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean set(Object key, Object value, Long expireTime) {
        try {
            ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value,expireTime,TimeUnit.SECONDS);
            return true;
        } catch (CTException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public Object vGet(KeyPrefix prefix,Object key) {
        try{
            String realKey = prefix.getPrefix() + key;
            ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
            return operations.get(realKey);
        }catch (CTException e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Object vGet(Object key) {
        try{
            ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
            return operations.get(key);
        }catch (CTException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void hmSet(KeyPrefix prefix, Object key, Object hashKey, Object value) {
        try{
            String realKey = prefix.getPrefix() + key;
            HashOperations<Object, Object, Object> hash = redisTemplate.opsForHash();
            hash.put(realKey, hashKey, value);
        }catch (CTException e){
            e.printStackTrace();
        }
    }

    /**
     * @param prefix
     * @return
     */
    public Set<String> scan(KeyPrefix prefix) {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keys = Sets.newHashSet();
            JedisCommands commands = (JedisCommands) connection.getNativeConnection();
            MultiKeyCommands multiKeyCommands = (MultiKeyCommands) commands;
            ScanParams scanParams = new ScanParams();
            scanParams.match(prefix.getPrefix() + "*");
            scanParams.count(1000);
            ScanResult<String> scan = multiKeyCommands.scan("0", scanParams);
            while (null != scan.getStringCursor()) {
                keys.addAll(scan.getResult());
                if (!StringUtils.equals("0", scan.getStringCursor())) {
                    scan = multiKeyCommands.scan(scan.getStringCursor(), scanParams);
                    continue;
                } else {
                    break;
                }
            }
            return keys;
        });
    }
    /**
     * @param prefix
     * @return
     */
    public Set<Object> getPrefix(KeyPrefix prefix) {
        Set<Object> keys = redisTemplate.keys(prefix.getPrefix()+"*");
        return keys;
    }


    /**
     * @param key
     * @param value
     * @return
     */
    public Long lPush(KeyPrefix prefix,String key, String value) {
        return redisTemplate.opsForList().leftPush(prefix.getPrefix()+key, value);
    }
    /**
     * @param key
     * @param value
     * @return
     */
    public Long lPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }
    /**
     * @param key
     * @param values
     * @return
     */
    public Long lPushAll(KeyPrefix prefix,Object key, List values) {
        return redisTemplate.opsForList().leftPushAll(prefix.getPrefix() + key,values);
    }

    /**
     *
     * @param key
     * @return
     */
    public Object rightPop(KeyPrefix prefix,String key) {
        return redisTemplate.opsForList().rightPop(prefix.getPrefix() + key);
    }

    /**
     * @param key
     * @return
     */
    public Boolean hasKey(KeyPrefix prefix,String key) {
        return redisTemplate.hasKey(prefix.getPrefix()+key);
    }

    /**
     * @param key
     * @return
     */
    public Boolean tryLock(String key,String value,Long timeOut) {
        return redisTemplate.opsForValue().setIfAbsent(key,value,timeOut,TimeUnit.MILLISECONDS);
    }

    public Boolean checkRedis() {
        try (RedisConnection redisConnection = redisTemplate.getRequiredConnectionFactory().getConnection()) {
            return true;
        } catch (Exception e) {
            log.error("The redis health check failed and the connection could not be successfully obtained");
            return false;
        }
    }
}

