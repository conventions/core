/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventionsframework.security;

import org.conventionsframework.qualifier.SecurityMethod;
import org.conventionsframework.util.MessagesController;
import org.conventionsframework.util.ResourceBundle;
import java.io.Serializable;
import java.lang.reflect.Method;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 *
 * @author rmpestano Aug 27, 2011 12:23:17 AM
 */
public abstract class SecurityMethodInterceptor implements Serializable {

    @Inject
    private ResourceBundle  resourceBundle;

    @AroundInvoke
    public Object checkPermission(InvocationContext ic) throws Exception {
        try {
            String[] rolesAllowed = this.extractMethodRoles(ic.getMethod());
            if (rolesAllowed != null && rolesAllowed.length > 0) {
                if (!this.checkUserPermissions(rolesAllowed)) {
                    MessagesController.addFatal(getFatalMessage(ic.getMethod().getAnnotation(SecurityMethod.class).message()));
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
     * @return true if user has permission to execute the method and false
     * otherwise
     */
    public abstract boolean checkUserPermissions(String[] rolesAllowed);

    private String[] extractMethodRoles(Method m) {
        if (m.isAnnotationPresent(SecurityMethod.class)) {
            SecurityMethod securityMethod = m.getAnnotation(SecurityMethod.class);
            return securityMethod.rolesAllowed();
        }
        return null;
    }

    private String getFatalMessage(String message) {
        if (resourceBundle  != null) {
            String i18nMessage = null;
            try {
                i18nMessage = resourceBundle.getString(message);
            } catch (java.util.MissingResourceException re) {
            }
            if (i18nMessage != null) {
               return i18nMessage;
            } else {//no message for given key
                return message;
            }
        }
        else {//no resourceBundle just return given message
            return message;
        }
    }
}
