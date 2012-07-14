/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.event;

import java.io.Serializable;

/**
 *
 * @author Rafael M. Pestano Apr 22, 2011 4:29:59 PM
 */

public class ModalCallback implements Serializable{
    
    private Object result;
    private String invokerName;

    public ModalCallback() {
    }

    
    public ModalCallback(Object result) {
        this.result = result;
    }
    public ModalCallback(Object result,String invoker) {
        this.invokerName = invoker;
        this.result = result;
    }

    
    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getInvokerName() {
        return invokerName;
    }

    public void setInvokerName(String invokerName) {
        this.invokerName = invokerName;
    }
}
