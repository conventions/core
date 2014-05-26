/*
 * Copyright 2011-2014 Conventions Framework.
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

import org.conventionsframework.exception.BusinessException;
import org.conventionsframework.qualifier.SecurityMethod;
import org.conventionsframework.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 *
 * @author Rafael M. Pestano Aug 27, 2011 12:23:17 AM
 */
public abstract class BaseSecurityInterceptor implements Serializable {

    @Inject
    private ResourceBundle  resourceBundle;

    @AroundInvoke
    public Object checkPermission(InvocationContext ic) throws Exception {
            String[] rolesAllowed = this.extractMethodRoles(ic.getMethod());
            if (rolesAllowed != null && rolesAllowed.length > 0) {
                if (!this.checkUserPermissions(rolesAllowed)) {
                    String cause = getFatalMessage(ic.getMethod().getAnnotation(SecurityMethod.class).message());
                    BusinessException be = new BusinessException(cause);
                    be.setSeverity(FacesMessage.SEVERITY_FATAL);
                    be.setSummary(cause);
                    throw be;
                }
            }
            return ic.proceed();
    }

    /**
     *
     * @param rolesAllowed list of roles allowed to execute the method
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
