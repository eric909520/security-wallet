package org.secwallet.datasource.aop;

import org.secwallet.datasource.annotation.TargetDataSource;
import org.secwallet.datasource.config.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;

@Slf4j
public class DynamicDataSourceAspect {
    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, TargetDataSource ds) throws Throwable {
        String dsId = ds.value();
        if (DynamicDataSourceContextHolder.dataSourceIds.contains(dsId)) {
            log.debug("Use DataSource :{} >", dsId, point.getSignature());
            DynamicDataSourceContextHolder.setDataSourceRouterKey(dsId);
        } else {
            log.info("Cache method annotation values{}", dsId, point.getSignature());
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
        log.debug("Revert DataSource : " + ds.value() + " > " + point.getSignature());
        DynamicDataSourceContextHolder.removeDataSourceRouterKey();

    }
}
