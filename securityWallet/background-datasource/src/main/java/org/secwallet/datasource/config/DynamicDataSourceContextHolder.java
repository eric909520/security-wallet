package org.secwallet.datasource.config;

import java.util.ArrayList;
import java.util.List;

public class DynamicDataSourceContextHolder {
    /**
     * Store the key of the registered data source
     */
    public static List<String> dataSourceIds = new ArrayList<>();

    /**
     * Thread-level private variables
     */
    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    public static String getDataSourceRouterKey () {
        return HOLDER.get();
    }

    public static void setDataSourceRouterKey (String dataSourceRouterKey) {
        HOLDER.set(dataSourceRouterKey);
    }

    /**
     * Be sure to remove it before setting the data source
     */
    public static void removeDataSourceRouterKey () {
        HOLDER.remove();
    }

    /**
     * Determine whether the specified DataSrouce currently exists
     * @param dataSourceId
     * @return
     */
    public static boolean containsDataSource(String dataSourceId){
        return dataSourceIds.contains(dataSourceId);
    }
}
