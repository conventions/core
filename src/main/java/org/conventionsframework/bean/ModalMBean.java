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
package org.conventionsframework.bean;

import java.util.Map;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import org.conventionsframework.event.ModalCallback;
import org.conventionsframework.event.ModalInitialization;
import org.conventionsframework.model.BaseEntity;


/**
 *
 * @author Rafael M. Pestano Apr 26, 2011 11:16:10 PM
 * 
 * Represents managed bean which controls a modal
 */
public abstract class ModalMBean<T extends BaseEntity> extends BaseMBean<T> {

    @Inject
    private Event<ModalCallback> modalCallbackEvent;
    private Map<String,Object> parameters;

    /* fired by modalButton */
    public void invokeModalCallback() {
        modalCallbackEvent.fire(new ModalCallback(modalCallback()));
    }

    
   public void beforeOpen(@Observes(notifyObserver = Reception.IF_EXISTS) ModalInitialization modalInit){
        parameters = modalInit.getParameters();
        onOpen();
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
    
    

    public abstract Object modalCallback();

    public void onOpen() {
    }
}