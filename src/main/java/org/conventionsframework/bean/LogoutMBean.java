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
package org.conventionsframework.bean;

import org.conventionsframework.qualifier.Config;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author rmpestano
 */
@Named
@RequestScoped
public class LogoutMBean {

    @Inject
    @Config
    Instance<HttpSession> session;

    @Inject
    @Config
    Instance<ExternalContext> externalContext;
    
    public void doLogout() throws IOException{
        session.get().invalidate();
        externalContext.get().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
    }
    
}
