package org.secwallet.core.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final ECInterceptor oneInterceptor;

    @Autowired
    public InterceptorConfig(ECInterceptor oneInterceptor) {
        this.oneInterceptor = oneInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(oneInterceptor).addPathPatterns("/**");
    }
}
