package org.secwallet.commonservice.service;

import org.secwallet.commonmodel.entity.AppConfig;
import org.secwallet.core.service.AbstractService;
import org.secwallet.commonservice.dao.AppConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AppConfigService extends AbstractService<AppConfig,Long> {

    @Resource
    private AppConfigMapper appConfigMapper;

    public AppConfig findSysKey(String key){
        return appConfigMapper.findSysKey(key);
    }

    public List<AppConfig> findWithdrawKey(Integer type) {
       return appConfigMapper.findWithdrawKey(type);
    }

    public List<AppConfig> findFixedConfigList() {
        return appConfigMapper.findFixedConfigList();
    }
}
