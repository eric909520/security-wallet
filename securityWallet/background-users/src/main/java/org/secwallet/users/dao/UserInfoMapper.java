package org.secwallet.users.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.secwallet.commonmodel.entity.UserInfo;

@Mapper
public interface UserInfoMapper {

    UserInfo findUserInfoByPhone(@Param("mobile") String mobile);

    int updateUserToken(@Param("id") Long id,@Param("token") String token);

}
