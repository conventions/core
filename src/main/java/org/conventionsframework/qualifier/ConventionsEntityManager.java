 
package org.conventionsframework.qualifier;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

@Qualifier
@Retention(RUNTIME)
@Target({TYPE, FIELD,PARAMETER,METHOD})
public @interface ConventionsEntityManager {
    
    Type type() default Type.CUSTOM;

    
    public static final String STATELESS_ENTITY_MANAGER = "statelessEntityManagerProvider";
    public static final String STATEFUL_ENTITY_MANAGER = "statefulEntityManagerProvider";
    public static final String CUSTOM_ENTITY_MANAGER = "customEntityManagerProvider";
    
}

