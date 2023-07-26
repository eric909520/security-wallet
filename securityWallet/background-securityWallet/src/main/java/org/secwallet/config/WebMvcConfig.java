package org.secwallet.config;

import org.secwallet.core.Interceptor.AuthorizationInterceptor;
import org.secwallet.core.annontation.CurrentUserHandlerMethodArgResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Autowired
    AuthorizationInterceptor authorizationInterceptor;
    @Autowired
    CurrentUserHandlerMethodArgResolver currentUserHandlerMethodArgResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //Set paths that allow cross-domain
        registry.addMapping("/**")
                //Allows setting the domain name for cross-domain requests
                //.allowedOrigins("http://localhost:8081")
                .allowedOrigins("*")
                //whether to allow certificates
                //.allowCredentials(true)
                //Set allowed methods
                //.allowedMethods(CorsConfiguration.ALL)
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")	// allow request method
                //Set the allowed request header
                //.allowedHeaders(CorsConfiguration.ALL)
                .allowedHeaders("*")
                //Time allowed across domains
                .maxAge(3000000)
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/api/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserHandlerMethodArgResolver);
    }


}
