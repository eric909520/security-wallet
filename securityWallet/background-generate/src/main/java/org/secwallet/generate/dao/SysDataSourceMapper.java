package org.secwallet.generate.dao;


import org.secwallet.generate.model.SysDataSource;
import org.secwallet.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Data source configuration management
 */
@Mapper
public interface SysDataSourceMapper extends BaseMapper<SysDataSource,String> {

}
