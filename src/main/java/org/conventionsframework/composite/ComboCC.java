/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventionsframework.composite;

import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

/**
 *
 * @author rmpestano Oct 9, 2011 1:17:23 PM
 */
@FacesComponent(value="org.conventionsframework.composite.ComboCC")
public class ComboCC extends UINamingContainer {

    @Override
    public String getFamily() {
        return "javax.faces.NamingContainer"; // Important! Required for composite components.
    }

    public void listener(javax.faces.event.AjaxBehaviorEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression ajaxEventListener = (MethodExpression) getAttributes().get("listener");
        if(ajaxEventListener != null){
            ajaxEventListener.invoke(context.getELContext(), new Object[] { event });
        }
    }
}
