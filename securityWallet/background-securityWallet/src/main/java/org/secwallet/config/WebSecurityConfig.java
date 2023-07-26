package org.secwallet.config;


import org.secwallet.core.security.CTAuthProvider;
import org.secwallet.core.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Use a custom authentication component
        auth.authenticationProvider(new CTAuthProvider(userDetailsService));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.GET,
                        "/api/**")
                .antMatchers(HttpMethod.POST,"/api/**")
                .and()
                .ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/login",
                        "/thirdPartLogin",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.woff?v=*",
                        "/**/*.ttf?v=*",
                        "/**/*.doc",
                        "/**/*.docx",
                        "/**/*.xlsx",
                        "/**/*.xls",
                        "/**/*.woff2",
                        "/lib/**",
                        "/api/**"
                ).and()
                .ignoring()
                .antMatchers("/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Allow all users to access "/" and "/home"
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"/**")
                .permitAll()
                .antMatchers("/uploadFile/**", "/v2/api-docs", "/v2/api-docs-ext","/configuration/ui", "/swagger-resources/**",
                        "/configuration/security", "/webjars/**", "/static/**", "/api/**", "/lib/**","/login","/thirdPartLogin","/getCheckCode","/register"
                        ,"/sendMobileCode","/sendRegisterCode","/sendCodeForget","/logout","/config/**")
                .permitAll()
                .anyRequest().authenticated();
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        http.addFilterBefore(jwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager){
        return new JwtAuthenticationFilter(authenticationManager);
    }

}
