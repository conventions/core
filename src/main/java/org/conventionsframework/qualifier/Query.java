 
package org.conventionsframework.qualifier;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Query {
    
    @Nonbinding
    String entityManagerPrivider() default ConventionsEntityManager.CUSTOM_ENTITY_MANAGER;
    
    @Nonbinding
    String sql() default "";
    
    @Nonbinding
    String namedQuery() default "";

    
}

