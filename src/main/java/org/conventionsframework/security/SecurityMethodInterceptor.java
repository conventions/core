/*
 * Copyright 2012 Conventions Framework.  
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
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
 * @author Rafael M. Pestano Aug 27, 2011 12:23:17 AM
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
