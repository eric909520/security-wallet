package org.secwallet.datasource.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = DynamicDataSourceContextHolder.getDataSourceRouterKey();
        return DynamicDataSourceContextHolder.getDataSourceRouterKey();
    }
}
