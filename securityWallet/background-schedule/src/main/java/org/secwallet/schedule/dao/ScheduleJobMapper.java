package org.secwallet.schedule.dao;

import org.secwallet.core.mapper.BaseMapper;
import org.secwallet.schedule.model.ScheduleJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface ScheduleJobMapper extends BaseMapper<ScheduleJob,String> {

    /**
     * update task status
     * @param id
     * @param status
     * @return
     */
    int updateStatus(@Param("id") String id, @Param("status") Integer status);
}
