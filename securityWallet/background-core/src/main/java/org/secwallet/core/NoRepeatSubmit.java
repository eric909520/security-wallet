package org.secwallet.core;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface NoRepeatSubmit {
    /**
     * Default time default 60 seconds
     * @return
     */
    int lockTime() default 300000;
}
