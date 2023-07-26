package org.secwallet.core.annontation;

import java.lang.annotation.*;

/**
 *
 * the currently requested user
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

}
