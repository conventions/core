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
package org.conventionsframework.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

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
        addBusinessErroCallbackParam();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }

    public static void addError(String summary) {
        /* 
         * businessError param is used by framework:messageHide component
         * also can be used to keep a dialog open on error
         */
        addBusinessErroCallbackParam();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, ""));
    }

    public static void addFatal(String summary) {
        addBusinessErroCallbackParam();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, ""));
    }

    public static void addFatal(String summary, String detail) {
        RequestContext.getCurrentInstance().addCallbackParam("businessError", true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, detail));
    }

    public static void addMessage(String summary, String detail, FacesMessage.Severity severity) {
        RequestContext.getCurrentInstance().addCallbackParam("businessError", true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public static void addMessage(String summary, FacesMessage.Severity severity) {
        addBusinessErroCallbackParam();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, ""));
    }

    private static void addBusinessErroCallbackParam() {
        RequestContext rc = RequestContext.getCurrentInstance();
        if (rc != null) {
            rc.addCallbackParam("businessError", true);
        }
    }
}
