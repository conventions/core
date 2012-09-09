
package org.conventionsframework.qualifier;

import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface QueryParam {
    
        @Nonbinding
	String name();
    
        @Nonbinding
	String value() default "";
        
        @Nonbinding
        int intValue() default -9999;//-9999 is not a valid param ;(
        
        
        
}
