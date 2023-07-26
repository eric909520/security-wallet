package org.secwallet.commonservice.dao;

import org.secwallet.commonmodel.entity.AppConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppConfigMapper {
    /**
     * Query system configuration
     * @param key
     * @return
     */
    AppConfig findSysKey(@Param("key") String key);

    List<AppConfig> findWithdrawKey(@Param("type")Integer type);

    List<AppConfig> findFixedConfigList();

}
