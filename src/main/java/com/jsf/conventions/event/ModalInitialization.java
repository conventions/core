/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rmpestano Jul 5, 2011 10:15:28 PM
 */
public class ModalInitialization implements Serializable{
    
    //name of the modal bean which this event is addressed
    private String modal;
    //parameters to send to the modal
    private Map<String,Object> parameters = new HashMap<String,Object>();

    
    
    public ModalInitialization(Map<String,Object> parameters,String modalName ) {
        this.modal = modalName;
        this.parameters = parameters;
    }
    
    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getModal() {
        return modal;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }
    
    
    
}
