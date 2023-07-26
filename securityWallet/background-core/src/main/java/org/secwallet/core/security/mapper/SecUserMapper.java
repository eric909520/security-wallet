package org.secwallet.core.security.mapper;

import org.secwallet.core.model.SecUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SecUserMapper {
    /**
     * Get details based on user id
     * @param id
     * @return
     */
    @Select("select * from user_info where ID=#{id}")
    SecUser findById(@Param("id") String id);
    @Select("select * from user_info where mobile=#{account}")
    SecUser findByAccount(@Param("account")String account);
    @Update("update user_info set token = null where id = #{id}")
    int updateUserToken(@Param("id")Long id);
}
