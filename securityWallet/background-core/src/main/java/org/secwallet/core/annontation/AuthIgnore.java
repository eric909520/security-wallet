package org.secwallet.core.annontation;

import java.lang.annotation.*;

/**
 * Do not verify token
 **/
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthIgnore {
}
