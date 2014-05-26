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

package org.conventionsframework.component.securityarea;

import javax.faces.component.UIComponentBase;

/**
 *
 * @author rmpestano
 */
public class SecurityArea extends UIComponentBase{
    private static final String RENDERER_TYPE = "org.conventionsframework.component.SecurityAreaRenderer";
    private static final String COMPONENT_TYPE = "org.conventionsframework.component.SecurityArea";
    private static final String COMPONENT_FAMILY = "org.conventionsframework.component";

    public SecurityArea() {
        super.setRendererType(RENDERER_TYPE);
    }

    
    @Override
    public String getFamily() {
       return COMPONENT_FAMILY;
    }
    
    public String getRolesAllowed() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.rolesAllowed);
    }

    public void setRolesAllowed(String _roles) {
        getStateHelper().put(PropertyKeys.rolesAllowed, _roles);
    }
    
     public String getRolesForbidden() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.rolesForbidden);
    }

    public void setRolesForbidden(String _roles) {
        getStateHelper().put(PropertyKeys.rolesForbidden, _roles);
    }
    
      protected enum PropertyKeys {

        rolesAllowed,rolesForbidden;
        String toString;

        PropertyKeys(String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return ((this.toString != null) ? this.toString : super.toString());
        }
    }
     
}
