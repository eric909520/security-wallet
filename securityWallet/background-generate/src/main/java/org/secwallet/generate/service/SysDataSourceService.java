package org.secwallet.generate.service;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.secwallet.core.service.AbstractService;
import org.secwallet.generate.dao.SysDataSourceMapper;
import org.secwallet.generate.model.SysDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SysDataSourceService extends AbstractService<SysDataSource,String> {

    @Autowired
    private SysDataSourceMapper sysDataSourceMapper;
    /**
     * Get all data source configuration information, support paging
     * @param sysDataSource
     * @return
     */
    public PageInfo<SysDataSource> findAllToPage(SysDataSource sysDataSource) {
        try {
            if (!sysDataSource.isDisablePaging()) {
                PageHelper.startPage(sysDataSource.getPageNum(), sysDataSource.getPageSize());
            }
            PageInfo<SysDataSource> regPageInfo = new PageInfo<>(sysDataSourceMapper.findAll(sysDataSource));
            return regPageInfo;
        } catch (Exception e) {
            log.error("Failed to get all system registration information：" + e.getMessage());
            throw e;
        }
    }

    /**
     * Get all data source information without pagination
     * @param sysDataSource
     * @return
     */
    public List<SysDataSource> findAllDataSource(SysDataSource sysDataSource) {
        try {
            List<SysDataSource> list=sysDataSourceMapper.findAll(sysDataSource);
            return list;
        } catch (Exception e) {
            log.error("Failed to get all data source information：" + e.getMessage());
            throw e;
        }
    }
}
