/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jsf.conventions.bean;

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