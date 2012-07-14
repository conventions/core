/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.composite;

import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author rmpestano Oct 9, 2011 1:17:23 PM
 */
@FacesComponent(value="com.jsf.conventions.composite.ModalCC")
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
