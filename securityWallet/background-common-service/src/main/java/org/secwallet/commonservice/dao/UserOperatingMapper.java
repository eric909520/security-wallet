package org.secwallet.commonservice.dao;

import org.secwallet.commonmodel.entity.UserOperating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserOperatingMapper {

    int addUserOperating(@Param("userId")Long userId,@Param("type")int type,@Param("desc")String desc,@Param("ip")String ip,@Param("os")String os,@Param("address")String address,@Param("time")Long time);


    List<UserOperating> findUserOperating(@Param("userId") Long userId, @Param("type")Integer type
            , @Param("pageSize")int pageSize, @Param("pageNum")int pageNum);

    int findUserOperatingTotalCount(@Param("userId") Long userId, @Param("type")Integer type);
}
