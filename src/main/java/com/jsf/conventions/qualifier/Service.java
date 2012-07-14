 
package com.jsf.conventions.qualifier;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;

@Inherited
@Retention(RUNTIME)
@Target({TYPE, FIELD})
public @interface Service {
    
    @Nonbinding
    String name() default "";
    @Nonbinding
    Class entity() default Byte.class;
}

