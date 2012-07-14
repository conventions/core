/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.model;

import com.jsf.conventions.bean.state.State;
import java.io.Serializable;

/**
 *
 * @author rmpestano Aug 8, 2011 7:26:03 PM
 * 
 * Class with represents an item in <code>StateController#stateItens</code>
 * 
 */public class StateItem<T> implements Serializable{
    
    private String page;
    private State beanState;
    private T entity;
    private String title;//breadCrumb link value displayed on the page
    private Class invokerClass;
    private boolean ajax;
    private String callback;
    private String update;
    

    public StateItem() {
    }
    

    public StateItem(String page,T entity, State beanState,String title,Class invoker,boolean ajax,String callback,String update) {
           this.page = page;
           this.entity = entity;
           this.beanState = beanState;
           this.title = title;
           this.invokerClass = invoker;
           this.ajax = ajax;
           this.callback = callback;
           this.update = update;
    }

    public State getBeanState() {
        return beanState;
    }

    public void setBeanState(State beanState) {
        this.beanState = beanState;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public Class getInvokerClass() {
        return invokerClass;
    }

    public void setInvokerClass(Class invokerClass) {
        this.invokerClass = invokerClass;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
    
    
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }//the stacks differ in bean state and invoker class
        StateItem other = (StateItem) obj;
        if(!this.getBeanState().equals(other.getBeanState())){
            return false;
        }
        if(!this.getInvokerClass().equals(other.invokerClass)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }
    
    
    
    
}
