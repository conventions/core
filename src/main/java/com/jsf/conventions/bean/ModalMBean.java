/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.bean;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import com.jsf.conventions.event.ModalCallback;
import com.jsf.conventions.model.AbstractBaseEntity;

 
/**
 *
 * @author Rafael M. Pestano Apr 26, 2011 11:16:10 PM
 * 
 * Represents managed bean which controls a modal
 */
public abstract class ModalMBean<T> extends BaseMBean<T> {

    @Inject
    private Event<ModalCallback> modalCallbackEvent;
    private String invokerName;

    /* fired by modalButton */
    public void invokeModalCallback() {
        modalCallbackEvent.fire(new ModalCallback(modalCallback(),invokerName));
    }

    public String getInvokerName() {
        return invokerName;
    }

    public void setInvokerName(String invokerName) {
        this.invokerName = invokerName;
    }
    

    public abstract Object modalCallback();
}