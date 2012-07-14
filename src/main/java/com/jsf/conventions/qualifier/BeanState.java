
package com.jsf.conventions.qualifier;

import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, FIELD, METHOD, PARAMETER })
public @interface BeanState {
        
        
        /**
         * 
         * @return page to return when clicking in a stateItem,
         * use "" to return to the same page
         */
	@Nonbinding
	String page() default "";
        /**
         * 
         * @return breadCrumb title for this state
         */
	@Nonbinding
	String title();
        @Nonbinding
        String beanState();
        /**
         * 
         * @return name of the action to be called when 
         * clicking in an item, use in the form of #{bean.method}
         * with String return type,
         * use "" to call no method
         * 
         * NOTE: when using callback the 'page' attribute will
         * be ignore so if you want to navigate to another page after the callback
         * specify that in the return of the callback method
         */
        @Nonbinding
        String callback() default "";
        @Nonbinding
        boolean ajax() default true;
        /**
         * 
         * @return id of component to update on ajax call
         * use only in ajaxifyed stateItens
         */
        @Nonbinding
        String update() default "@none";
}
