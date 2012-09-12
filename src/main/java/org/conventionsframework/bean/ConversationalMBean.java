/*
 * Copyright 2012 Conventions Framework.  
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

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;

/**
 *
 * @author Rafael M. Pestano Apr 26, 2011 10:42:23 PM
 */
public abstract class ConversationalMBean<T> extends BaseMBean<T> implements Serializable {
    
 
	private static final long serialVersionUID = 1L;
	
	@Inject
    protected Conversation conversation;
     
    
    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public void beginConversation(){
        if(conversation.isTransient()){
            conversation.begin();
        }
    }

    public void endConversation(){
        if(!conversation.isTransient()){
            conversation.end();
        }
    }
    
    public void beginNewConversation(){
         endConversation();
         beginConversation();
    }

}