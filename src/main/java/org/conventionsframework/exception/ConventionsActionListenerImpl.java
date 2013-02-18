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
package org.conventionsframework.exception;

import org.conventionsframework.util.MessagesController;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.myfaces.application.ActionListenerImpl;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Rafael M. Pestano Jun 21, 2011 11:21:08 PM
 *
 *
 */
public class ConventionsActionListenerImpl extends ActionListenerImpl implements Serializable {

    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    public void processAction(ActionEvent event) {
        try {
            try {
                super.processAction(event);
            } catch (Throwable e) {
                throw ExceptionUtils.getRootCause(e);
            }
        } catch (BusinessException be) {
            if (be.getSeverity() != null) {
                MessagesController.addMessage(be.getSummary(), be.getDetail(), be.getSeverity());
            } else {
                MessagesController.addError(be.getSummary(), be.getDetail());
            }
            this.scrollAndFocusOnError(be);
        } catch (Throwable ex) {//if its uncaught exception go to error page
            MessagesController.addFatal(ex.getMessage());
            FacesContext.getCurrentInstance().getExternalContext().log("UNEXPECTED ERROR:  " + ex.getMessage(), ex);
            handleFatalError(ex);
        }
    }

    /**
     * gather information about the exception and pass the info to errorBean so
     * the error details can be displayed to end user.
     *
     * @param ex
     */
    private void handleFatalError(final Throwable ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        String errorName = (rootCause == null) ? ex.getClass().getCanonicalName() : rootCause.getClass().getCanonicalName();
        String errorMessage = ExceptionUtils.getRootCauseMessage(ex);
        String stackTrace = ExceptionUtils.getStackTrace(rootCause == null ? ex : rootCause);
        /*
         * ELFlash.getFlash().put("errorName", errorName); /
         * ELFlash.getFlash().put("errorMessage", errorMessage);
         * ELFlash.getFlash().put("stackTrace", stackTrace); flash doesnt help
         * here, the ideia is to redirect to an error page
         */
        FacesContext context = FacesContext.getCurrentInstance();


        ErrorMBean errorMBean = context.getApplication().evaluateExpressionGet(context, "#{convErrorMBean}", ErrorMBean.class);
        errorMBean.setErrorMessage(errorMessage);
        errorMBean.setErrorName(errorName);
        errorMBean.setStacktrace(stackTrace);
        goToErrorPage();
    }

    /**
     * redirect to an error page('errorPage' navigation rule) with exception
     * information to display to the user in a friendly way
     */
    private void goToErrorPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            NavigationHandler navHandler = context.getApplication().getNavigationHandler();
            navHandler.handleNavigation(context, null, "errorPage");
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(ConventionsActionListenerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void scrollAndFocusOnError(BusinessException be) {
        if (be.getId() != null && !"".endsWith(be.getId())) {
            RequestContext rc = RequestContext.getCurrentInstance();
            String componentId = be.getId();
            if (rc != null) {
                rc.scrollTo(componentId);
                String js = "if(document.getElementById('"+componentId+"')){document.getElementById('"+componentId+"').focus();}";
                rc.execute(js);
            }
        }
    }
    
    
}
