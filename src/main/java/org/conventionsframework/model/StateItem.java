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
package org.conventionsframework.model;

import org.conventionsframework.bean.state.State;

import java.io.Serializable;

/**
 * @author rmpestano Aug 8, 2011 7:26:03 PM
 *         <p/>
 *         Class which represents an item in <code>StateController#stateItens</code>
 */
public class StateItem<T> implements Serializable {

    private String outcome;
    private State beanState;
    private T entity;
    private String title;
    private String value;//breadCrumb link value displayed on the outcome
    private Class invokerClass;
    private boolean ajax;
    private boolean global;
    private boolean resetValues;
    private boolean immediate;
    private boolean addEntityIdParam;//add entity id param in stateItem outcome
    private String callback;
    private String update;
    private String oncomplete;

    public StateItem() {
    }

    //TODO builder pattern would be good
    public StateItem(String outcome, T entity, State beanState, String value, String title, Class invoker, boolean ajax, String callback, String update, boolean global, boolean resetValues, boolean immediate,String oncomplete, boolean addEntityIdParam) {
        this.outcome = outcome;
        this.entity = entity;
        this.beanState = beanState;
        this.title = title;
        this.value = value;
        this.invokerClass = invoker;
        this.ajax = ajax;
        this.callback = callback;
        this.update = update;
        this.global = global;
        this.resetValues = resetValues;
        this.immediate = immediate;
        this.oncomplete = oncomplete;
        this.addEntityIdParam = addEntityIdParam;
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

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
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

    public String getValue() {
        return value;
    }

    public void setValue(String link) {
        this.value = link;
    }

    public boolean isGlobal() {
        return global;
    }

    public boolean isResetValues() {
        return resetValues;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public void setResetValues(boolean resetValues) {
        this.resetValues = resetValues;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public String getOncomplete() {
        return oncomplete;
    }

    public void setOncomplete(String onComplete) {
        this.oncomplete = onComplete;
    }

    public boolean isAddEntityIdParam() {
        return addEntityIdParam;
    }

    public void setAddEntityIdParam(boolean addEntityId) {
        this.addEntityIdParam = addEntityId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }//the stateItens differ in bean state and invoker class
        StateItem other = (StateItem) obj;
        if (!this.getBeanState().equals(other.getBeanState())) {
            return false;
        }
        if (!this.getInvokerClass().equals(other.invokerClass)) {
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
