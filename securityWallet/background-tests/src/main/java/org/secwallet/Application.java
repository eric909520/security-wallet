package org.secwallet;


import org.secwallet.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import org.secwallet.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import org.secwallet.datasource.datasource.DynamicDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(DynamicDataSource.class)
@EnableTransactionManagement
@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = {"org.secwallet"})
@ComponentScan(
        basePackages = "org.secwallet",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {AdminApplication.class})})
public class Application extends SpringBootServletInitializer {
    @Bean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor() {
        return new DynamicDataSourceAnnotationAdvisor(new DynamicDataSourceAnnotationInterceptor());
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
