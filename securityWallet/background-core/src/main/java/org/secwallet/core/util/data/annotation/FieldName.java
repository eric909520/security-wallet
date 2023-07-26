package org.secwallet.core.util.data.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FieldName {

    String fieldName() default "unknown name";
}
