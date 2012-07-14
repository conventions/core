/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.security;

import com.jsf.conventions.producer.ResourceBundleProvider;
import com.jsf.conventions.qualifier.SecurityMethod;
import com.jsf.conventions.util.MessagesController;
import com.jsf.conventions.util.ResourceBundle;
import java.io.Serializable;
import java.lang.reflect.Method;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * 
 * @author rmpestano Aug 27, 2011 12:23:17 AM
 */


public abstract class SecurityMethodInterceptor implements Serializable  {

    @Inject
    private ResourceBundleProvider resourceBundleProvider;

    @AroundInvoke
    public Object checkPermission(InvocationContext ic) throws Exception {
        try {
            String[] rolesAllowed = this.extractMethodRoles(ic.getMethod());
            if (rolesAllowed != null && rolesAllowed.length > 0) {
                if (!this.checkUserPermissions(rolesAllowed)) {
                        if(resourceBundleProvider.getCurrentBundle() != null){
                             if(resourceBundleProvider.getCurrentBundle().getString(ic.getMethod().getAnnotation(SecurityMethod.class).message()) != null){
                                 MessagesController.addFatal(resourceBundleProvider.getCurrentBundle().getString(ic.getMethod().getAnnotation(SecurityMethod.class).message()));
                             }
                             else{//no message for given key
                                MessagesController.addFatal(ic.getMethod().getAnnotation(SecurityMethod.class).message());
                             }
                         }
                         else{//no resourceBundle configured
                                MessagesController.addFatal(ic.getMethod().getAnnotation(SecurityMethod.class).message());
                             }
                    return null;
                }
            }
            return ic.proceed();
        } catch (Exception ex) {
           ex.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param list of roles allowed to execute the method
     * @return true if user has permission to execute the method and false otherwise
     */
    public abstract boolean checkUserPermissions(String[] rolesAllowed);
       

    private String[] extractMethodRoles(Method m) {
        if (m.isAnnotationPresent(SecurityMethod.class)) {
            SecurityMethod securityMethod = m.getAnnotation(SecurityMethod.class);
            return securityMethod.rolesAllowed();
        }
        return null;
    }

    
}
