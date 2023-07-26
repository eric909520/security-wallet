package org.secwallet.core.util.bean;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@ConfigurationProperties(prefix = "cthl.token")
@PropertySource("classpath:/sys-config.properties")
@EnableConfigurationProperties(TokenConfig.class)
public class TokenConfig {

    private  String secret;
    private  String header;
    private  Long expiration;
    public  String getSecret() {
        return secret;
    }

    public  void setSecret(String secret) {
        this.secret = secret;
    }

    public  String getHeader() {
        return header;
    }

    public  void setHeader(String header) {
        this.header = header;
    }

    public  Long getExpiration() {
        return expiration;
    }

    public  void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

}
