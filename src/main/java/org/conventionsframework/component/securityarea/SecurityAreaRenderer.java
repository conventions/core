/*
 * Copyright 2011-2013 Conventions Framework.  
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

package org.conventionsframework.component.securityarea;

import org.conventionsframework.security.SecurityContext;
import org.conventionsframework.util.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.renderkit.CoreRenderer;

/**
 * @author rmpestano
 */
public class SecurityAreaRenderer extends CoreRenderer {

    @Inject
    SecurityContext securityContext;


    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        SecurityArea securityArea = (SecurityArea) component;
        if (securityArea.isRendered() && securityArea.getChildCount() > 0) {
            if (allow(securityArea.getRolesAllowed(), securityArea.getRolesForbidden(), component, context)) {
                for (UIComponent child : securityArea.getChildren()) {
                    if (child.isRendered()) {
                        child.encodeAll(context);
                    }
                }
            }

        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    /**
     * @param rolesAllowed
     * @param rolesForbidden
     * @param context
     * @return false if user role(s) IS between roles forbidden || NOT IS in the list of roles allowed, true in other cases
     */
    private boolean allow(String rolesAllowed, String rolesForbidden, UIComponent component, FacesContext context) {

        boolean rolesForbiddenAttributeDefined = (rolesForbidden != null && !"".equals(rolesForbidden));
        boolean rolesAllowedAttributeDefined = (rolesAllowed != null && !"".equals(rolesAllowed));

        if (rolesForbiddenAttributeDefined && rolesAllowedAttributeDefined) {
            throw new FacesException("CONVENTIONS:rolesAllowed and rolesForbidden attributes defined at same time in SecurityArea component placed inside '" + component.getParent().getClientId(context) + "'.\nThese attributes are mutually exclusive, use only one at a time.");
        }

        if (rolesForbiddenAttributeDefined) {
            boolean isForbidden = checkRolesForbidden(rolesForbidden);
            return !isForbidden;
        } else if (rolesAllowedAttributeDefined) {
            boolean isAllowed = checkRolesAllowed(rolesAllowed);
            return isAllowed;
        } else {//either rolesAllowed neither forbidden if provided so render region
            return true;
        }
    }

    /**
     * check if user role(s) is in the list of roles forbidden
     *
     * @param roles
     * @param rolesForbidden
     * @return true if userRoles is in the list of rolesForbidden
     */
    private boolean checkRolesForbidden(String rolesForbidden) {


        String[] forbiddenRoles = rolesForbidden.split("[\\s,;]+");

        if (securityContext.hasAnyRole(forbiddenRoles)) {
            return Boolean.TRUE;
        }
        return false;//user role is NOT between forbidden roles
    }

    /**
     * check if user role(s) is in the list of roles allowed
     *
     * @param roles
     * @param rolesAllowed
     * @return true if userRoles is in the list of rolesAllowed
     */
    private boolean checkRolesAllowed(String rolesAllowed) {
        String[] allowedRoles =  rolesAllowed.split("[\\s,;]+");
        if (allowedRoles != null) {
            if(securityContext.hasAnyRole(allowedRoles)){
                return Boolean.TRUE;
            }
        }
        return false;//user role is NOT between allowed roles
    }
}
