/*
 * Copyright 2011-2012 Conventions Framework.  
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

import org.conventionsframework.util.Constants;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.renderkit.CoreRenderer;

/**
 *
 * @author rmpestano
 */
public class SecurityAreaRenderer extends CoreRenderer{
    
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        SecurityArea securityArea = (SecurityArea) component;
        if(securityArea.isRendered() && securityArea.getChildCount() > 0){
            if(allow(securityArea.getRolesAllowed(),securityArea.getRolesForbidden(),component,context)){
                for (UIComponent child : securityArea.getChildren()) {
                    if(child.isRendered()){
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
     * 
     * @param rolesAllowed
     * @param rolesForbidden
     * @param context
     * @return false if user role(s) IS between roles forbidden || NOT IS in the list of roles allowed, true in other cases
     */
    private boolean allow(String rolesAllowed, String rolesForbidden, UIComponent component,FacesContext context) {
        
        boolean rolesForbiddenAttributeDefined = (rolesForbidden != null && !"".equals(rolesForbidden));
        boolean rolesAllowedAttributeDefined = (rolesAllowed != null && !"".equals(rolesAllowed));
        
        if(rolesForbiddenAttributeDefined && rolesAllowedAttributeDefined){
            throw new FacesException("CONVENTIONS:rolesAllowed and rolesForbidden attributes defined at same time in SecurityArea component placed inside '"+component.getParent().getClientId(context) + "'.\nThese attributes are mutualy exclusive, use only one at a time.");
        }
        
        List<String> userRoles = (List<String>) context.getExternalContext().getSessionMap().get(Constants.USER_ROLES);
        
        if(rolesForbiddenAttributeDefined){
            boolean isForbidden = checkRolesForbidden(userRoles,rolesForbidden);
            return !isForbidden;
        }
        else if(rolesAllowedAttributeDefined){
           boolean isAllowed = checkRolesAllowed(userRoles,rolesAllowed);
           return isAllowed;
        }
        else{//either rolesAllowed neither forbidden if provided so render region
            return true;
        }
    }

    /**
     * check if user role(s) is in the list of roles forbidden
     * @param roles
     * @param rolesForbidden
     * @return true if userRoles is in the list of rolesForbidden 
     */
    private boolean checkRolesForbidden(List<String> userRoles, String rolesForbidden) {
        
        //user has no roles so it cant be in the list of forbidden ones
        if(userRoles == null || userRoles.isEmpty()){
            return false;//
        }
        List<String> forbiddenRoles = Arrays.asList(rolesForbidden.split("[\\s,;]+"));
        for (String string : userRoles) {
             if(string == null){
                    continue;
                }
            for (String string1 : forbiddenRoles) {
                if(string.equalsIgnoreCase(string1)){
                    return true;//user roles is forbidden
                }
            }
        }
        return false;//user role is NOT between forbidden roles
    }

     /**
     * check if user role(s) is in the list of roles allowed
     * @param roles
     * @param rolesAllowed
     * @return true if userRoles is in the list of rolesAllowed
     */
    private boolean checkRolesAllowed(List<String> userRoles, String rolesAllowed) {
        if(userRoles == null || userRoles.isEmpty()){
            return false;//user role is not in the list of allowed
        }
        
        List<String> allowedRoles = Arrays.asList(rolesAllowed.split("[\\s,;]+"));
        if(allowedRoles != null){
            for (String string : userRoles) {
                if(string == null){
                    continue;
                }
                for (String string1 : allowedRoles) {
                    if(string.equalsIgnoreCase(string1)){
                        return true;//user roles is allowed
                    }
                }
            }
        }
        return false;//user role is NOT between allowed roles
    }
}
