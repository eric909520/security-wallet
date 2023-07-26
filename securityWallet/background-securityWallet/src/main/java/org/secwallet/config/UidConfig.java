package org.secwallet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = { "classpath:cached-uid-spring.xml" })
public class UidConfig {
}
