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

import org.conventionsframework.bean.StateController;
import org.conventionsframework.qualifier.Config;
import org.conventionsframework.util.Constants;
import org.conventionsframework.util.MessagesController;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael M. Pestano Jun 24, 2012 19:23:17 AM
 */
@SessionScoped
@Named(value = "pageController")
public class SecurityPageController implements Serializable {

    @Inject
    private StateController stateController;

    @Inject
    @Config
    transient Instance<ExternalContext> externalContext;

    public void checkUserAccess(String rolesAlowed, String outcome, String message, boolean removeLastState, String page) {
        if (!FacesContext.getCurrentInstance().isPostback() && rolesAlowed != null && !"".endsWith(rolesAlowed)) {//do not check on ajax requests
            List<String> userRoles = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Constants.USER_ROLES);
            if (userRoles != null) {
                String[] roles = rolesAlowed.split("[\\s,;]+");//comma,space or semicolon separed list of rolesAllowed
                for (String role : roles) {
                    if (userRoles.contains(role)) {
                        return;
                    }
                }
            }
            //if it gets here user has no access to the outcome
            if (message != null && !"".endsWith(message)) {
                MessagesController.addFatal(message);
            }
            if (removeLastState) {//will remove the last state from stateList - useful when using securityPage in conjunction with statePusher 
                if (stateController != null && stateController.getStateItens() != null && !stateController.getStateItens().isEmpty()) {
                    stateController.getStateItens().removeLast();
                }
            }

            if (outcome != null && !"".endsWith(outcome.trim())) {
                try {
                    FacesContext context = FacesContext.getCurrentInstance();
                    NavigationHandler navHandler = context.getApplication().getNavigationHandler();
                    navHandler.handleNavigation(context, null, outcome);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(SecurityPageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if(page != null && !"".endsWith(page)){
                try{
                    if(!page.startsWith("/")){
                        page ="/"+page;
                    }
                    ExternalContext ec = externalContext.get();
                    ec.redirect(ec.getRequestContextPath() + page);
                }catch (Exception e){
                    e.printStackTrace();
                    Logger.getLogger(SecurityPageController.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }//end !isPostback
    }


}
