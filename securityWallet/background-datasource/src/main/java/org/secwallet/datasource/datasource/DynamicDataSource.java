package org.secwallet.datasource.datasource;


import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.secwallet.datasource.config.DynamicDataSourceContextHolder;
import org.secwallet.datasource.config.DynamicRoutingDataSource;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DynamicDataSource implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    /**
     * Configuration context (can also be understood as a configuration file acquisition tool)
     */
    private Environment evn;


    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();

    /**
     * Due to the different configurations of some data sources, add aliases here to avoid the situation that some parameters cannot be injected when switching data sources.
     */
    static {
        aliases.addAliases("url", new String[]{"jdbc-url"});
        aliases.addAliases("username", new String[]{"user"});
    }

    /**
     *
     * Store our registered data sources
     */
    private Map<String, DataSource> customDataSources = new HashMap<String, DataSource>();

    /**
     * Parameter binding tool springboot2.0 newly launched
     */
    private Binder binder;

    /**
     * The implementation method of the ImportBeanDefinitionRegistrar interface, through which you can register beans in your own way
     *
     * @param annotationMetadata
     * @param beanDefinitionRegistry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        //Get all datasource configurations
        Map defauleDataSourceProperties;
        defauleDataSourceProperties = binder.bind("spring.datasource.master", Map.class).get();
        // Get data source type
        String typeStr = evn.getProperty("spring.datasource.master.type");
        // Get data source type
        Class<? extends DataSource> clazz = getDataSourceType(typeStr);
        //Bind the default data source parameter, which is the main data source
        DataSource defaultDatasource = bind(clazz, defauleDataSourceProperties);
        DynamicDataSourceContextHolder.dataSourceIds.add("master");
        log.info("The registration of the default data source succeeded");
        GenericBeanDefinition define = new GenericBeanDefinition();
        define.setBeanClass(DynamicRoutingDataSource.class);
        MutablePropertyValues mpv = define.getPropertyValues();
        mpv.add("defaultTargetDataSource", defaultDatasource);
        mpv.add("targetDataSources", customDataSources);
        beanDefinitionRegistry.registerBeanDefinition("datasource", define);
        log.info("The data source registration is successful, a total of {} {} data sources are registered", customDataSources.keySet(), customDataSources.keySet().size() + 1);
    }

    /**
     * The implementation method of the ImportBeanDefinitionRegistrar interface, through which you can register beans in your own way
     *
     * @param annotationMetadata
     * @param beanDefinitionRegistry
     */
    /*@Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        //Get all datasource configurations
        Map config, defauleDataSourceProperties;
        defauleDataSourceProperties = binder.bind("spring.datasource.master", Map.class).get();
        // Get data source type
        String typeStr = evn.getProperty("spring.datasource.master.type");
        // Get data source type
        Class<? extends DataSource> clazz = getDataSourceType(typeStr);
        //Bind the default data source parameter, which is the main data source
        DataSource consumerDatasource, defaultDatasource = bind(clazz, defauleDataSourceProperties);
        DynamicDataSourceContextHolder.dataSourceIds.add("master");
        log.info("The registration of the default data source succeeded");
        // Get other data source configuration
        List<Map> configs = binder.bind("spring.datasource.cluster", Bindable.listOf(Map.class)).get();
        // traverse from data source
        for (int i = 0; i < configs.size(); i++) {
            config = configs.get(i);
            clazz = getDataSourceType((String) config.get("type"));
            defauleDataSourceProperties = config;
            // bind parameter
            consumerDatasource = bind(clazz, defauleDataSourceProperties);
            // Get the key of the data source, so that the data source can be located through the key
            String key = config.get("key").toString();
            customDataSources.put(key, consumerDatasource);
            // The data source context, used to manage the data source and record the registered data source key
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
            log.info("Registering data source {} succeeded", key);
        }
        GenericBeanDefinition define = new GenericBeanDefinition();
        define.setBeanClass(DynamicRoutingDataSource.class);
        MutablePropertyValues mpv = define.getPropertyValues();
        mpv.add("defaultTargetDataSource", defaultDatasource);
        mpv.add("targetDataSources", customDataSources);
        beanDefinitionRegistry.registerBeanDefinition("datasource", define);
        log.info("The data source registration is successful, a total of {} {} data sources are registered", customDataSources.keySet(), customDataSources.keySet().size() + 1);
    }*/

    /**
     * Get the data source class object by string
     *
     * @param typeStr
     * @return
     */
    private Class<? extends DataSource> getDataSourceType(String typeStr) {
        Class<? extends DataSource> type;
        try {
            if (StringUtils.hasLength(typeStr)) {
                //If the string is not empty, the class object is obtained by reflection
                type = (Class<? extends DataSource>) Class.forName(typeStr);
            } else {
                // The default is the hikariCP data source, which is consistent with the springboot default data source
                type = HikariDataSource.class;
            }
            return type;
        } catch (Exception e) {
            //If the class object cannot be obtained through reflection, an exception will be thrown. This situation is generally written wrong, so a runtime exception is thrown this time.
            throw new IllegalArgumentException("can not resolve class with type: " + typeStr);
        }
    }

    /**
     * Binding parameters, the following three methods are implemented with reference to the bind method of DataSourceBuilder, the purpose is to ensure that the data source construction process we add is consistent with springboot as much as possible
     *
     * @param result
     * @param properties
     */
    private void bind(DataSource result, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
        //Bind parameters to objects
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
    }

    private <T extends DataSource> T bind(Class<T> clazz, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
        // Bind parameters by type and get instance object
        return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(clazz)).get();
    }

    /**
     * @param clazz
     * @param sourcePath
     * Parameter path, corresponding to the value in the configuration file, such as: spring.datasource
     * @param <T>
     * @return
     */
    private <T extends DataSource> T bind(Class<T> clazz, String sourcePath) {
        Map properties = binder.bind(sourcePath, Map.class).get();
        return bind(clazz, properties);
    }

    /**
     * The implementation method of the EnvironmentAware interface is injected through awareness, here is the environment object
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        log.info("Start registering data sources");
        this.evn = environment;
        // binding configurator
        binder = Binder.get(evn);
    }
}
