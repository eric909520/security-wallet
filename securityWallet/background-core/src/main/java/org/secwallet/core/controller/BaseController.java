package org.secwallet.core.controller;


import org.secwallet.core.cache.redis.RedisService;
import org.secwallet.core.util.bean.AppUtil;
import org.secwallet.core.util.security.SecurityUtil;
import org.secwallet.core.exception.CTException;
import org.secwallet.core.model.SecUser;
import org.secwallet.uid.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller
 */
@Slf4j
public class BaseController{

    @Autowired
    private SecurityUtil securityUtil;
    @Resource
    private RedisService redisService;

    /**
     * Get current system user information
     * @return
     */
    public SecUser getUser() {
        try{
            SecUser user = (SecUser)securityUtil.getUser();
            return user;
        }catch (CTException e){
            log.error("Failed to get current system user information:"+e.getMessage());
            throw e;
        }
    }
    /**
     * Get current system user information
     * @return
     */
    public SecUser getUser(HttpServletRequest request) {
        try{
            SecUser user = (SecUser)securityUtil.getUser(request);
            if (user == null) {
                return null;
            }
            Object cacheToken = redisService.vGet("userToken:" + user.getId());
            if (cacheToken == null || user == null || StringUtils.isBlank(user.getToken())
                    || !user.getToken().equals(cacheToken)) {
                return null;
            }
            return user;
        }catch (CTException e){
            log.error("Failed to get current system user information:"+e.getMessage());
            throw e;
        }
    }
    /**
     *
     * get unique uid
     * @return
     */
    public long genId() {
        UidGenerator uidGenerator= (UidGenerator) AppUtil.getBean("cachedUidGenerator");
        return  uidGenerator.getUID();
    }

    /**
     *
     * sign out
     * @return
     */
    public void cleanUser() {
        try{
            securityUtil.cleanUser();
        }catch (CTException e){
            log.error("Failed to log out:"+e.getMessage());
            throw e;
        }
    }
}
