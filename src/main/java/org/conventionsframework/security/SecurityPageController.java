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
package org.conventionsframework.security;

import org.conventionsframework.bean.StateController;
import org.conventionsframework.util.Constants;
import org.conventionsframework.util.MessagesController;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Rafael M. Pestano Jun 24, 2012 19:23:17 AM
 */
@SessionScoped
@Named(value = "pageController")
public class SecurityPageController implements Serializable {

    private String message;//message to show when user has no access to the page
    private String outcome;//outcome to forward user when user has no access to the page
    @Inject
    private StateController stateController;

    public void checkUserAccess(String rolesAlowed, String outcome, String message, boolean removeLastState) {
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
            //if it gets here user has no access to the page
            if (message != null && !"".endsWith(message)) {
                MessagesController.addFatal(message);
            }
            if (removeLastState) {//will remove the last state from stateList - useful when using securityPage in conjunction with statePusher 
                if (stateController != null && stateController.getStateItens() != null && !stateController.getStateItens().isEmpty()) {
                    stateController.getStateItens().removeLast();
                }
            }

            if (outcome != null && !"".endsWith(outcome)) {
                try {
                    FacesContext context = FacesContext.getCurrentInstance();
                    NavigationHandler navHandler = context.getApplication().getNavigationHandler();
                    navHandler.handleNavigation(context, null, outcome);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(SecurityPageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }//end !isPostback
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return outcome to match a navigation rule when user has no access to the
     * page
     */
    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
