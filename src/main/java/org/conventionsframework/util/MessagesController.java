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
package org.conventionsframework.util;

import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Rafael M. Pestano
 */
public class MessagesController {

    public static void addInfo(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    public static void addInfo(String summary) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, ""));
    }

    public static void addWarn(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail));
    }

    public static void addWarn(String summary) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, ""));
    }

    public static void addError(String summary, String detail) {
        addValidationFailedCallbackParam();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }

    public static void addError(String summary) {
        addValidationFailedCallbackParam();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, ""));
    }

    public static void addFatal(String summary) {
        addValidationFailedCallbackParam();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, ""));
    }

    public static void addFatal(String summary, String detail) {
        addValidationFailedCallbackParam();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, detail));
    }

    public static void addMessage(String summary, String detail, FacesMessage.Severity severity) {
        addValidationFailedCallbackParam();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public static void addMessage(String summary, FacesMessage.Severity severity) {
        addValidationFailedCallbackParam();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, ""));
    }

    private static void addValidationFailedCallbackParam() {
        RequestContext rc = RequestContext.getCurrentInstance();
        if (rc != null) {
            rc.addCallbackParam("validationFailed", true);
        }
    }
}
