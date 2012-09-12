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

package org.conventionsframework.composite;

import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author rmpestano Oct 9, 2011 1:17:23 PM
 */
@FacesComponent(value="org.conventionsframework.composite.ModalCC")
public class ModalCC extends UINamingContainer {

    @Override
    public String getFamily() {
        return "javax.faces.NamingContainer"; // Important! Required for composite components.
    }

    
    public void closeListener(CloseEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression ajaxEventListener = (MethodExpression) getAttributes().get("closeListener");
        if(ajaxEventListener != null){
            ajaxEventListener.invoke(context.getELContext(), new Object[] { event });
        }
    }
    
}
