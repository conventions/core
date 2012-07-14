/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.component.securityarea;

import javax.faces.component.UIComponentBase;

/**
 *
 * @author rmpestano
 */
public class SecurityArea extends UIComponentBase{
    private static final String RENDERER_TYPE = "com.jsf.conventions.component.SecurityAreaRenderer";
    private static final String COMPONENT_TYPE = "com.jsf.conventions.component.SecurityArea";
    private static final String COMPONENT_FAMILY = "com.jsf.conventions.component";

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
