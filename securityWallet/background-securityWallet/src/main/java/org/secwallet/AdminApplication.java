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
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(DynamicDataSource.class)
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"org.secwallet"})
@MapperScan(basePackages = {"org.secwallet"})
public class AdminApplication extends SpringBootServletInitializer {
    @Bean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor() {
        return new DynamicDataSourceAnnotationAdvisor(new DynamicDataSourceAnnotationInterceptor());
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AdminApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
//    @Bean
//    public ErrorPageFilter errorPageFilter() {
//        return new ErrorPageFilter();
//    }

//    @Bean
//    public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(filter);
//        filterRegistrationBean.setEnabled(false);
//        return filterRegistrationBean;
//    }
}
