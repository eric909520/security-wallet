package org.secwallet.commonservice.service;

import com.alibaba.fastjson.JSONObject;
import org.secwallet.commonmodel.entity.UserOperating;
import org.secwallet.core.service.AbstractService;
import org.secwallet.commonservice.dao.UserOperatingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class UserOperatingService extends AbstractService<UserOperating,Long> {

    @Resource
    private UserOperatingMapper userOperatingMapper;

    /**
     *
     * Add user action record
     * @param jsonObject
     * @return
     */
    public int addUserOperating(JSONObject jsonObject){
        Long userId = jsonObject.getLong("userId");
        Integer type = jsonObject.getInteger("type");
        String desc = jsonObject.getString("desc");
        String ip = jsonObject.getString("ip");
        String os = jsonObject.getString("os");
        String address = jsonObject.getString("address");
       return userOperatingMapper.addUserOperating(userId, type, desc, ip, os, address, System.currentTimeMillis());
    }

    /**
     * Query user operation records
     * type >= 100 query all types of records
     * @param userId
     * @param type
     * @return
     */
    public List<UserOperating> findUserOperating(Long userId, Integer type, int pageSize, int pageNum){
        return userOperatingMapper.findUserOperating(userId, type, pageSize, (pageNum - 1) * pageSize);
    }

    /**
     * Query the total number of login history records
     * @param userId
     * @param type
     * @return
     */
    public int findUserOperatingTotalCount(Long userId, Integer type){
        return userOperatingMapper.findUserOperatingTotalCount(userId,type);
    }
}
