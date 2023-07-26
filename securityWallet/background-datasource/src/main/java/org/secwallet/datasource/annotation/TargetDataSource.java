package org.secwallet.datasource.annotation;

import java.lang.annotation.*;

/**
 * Multiple data source annotations
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TargetDataSource {
    String value() default "";
}
