/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.event;

import com.jsf.conventions.bean.StateController;
import com.jsf.conventions.model.StateItem;
import java.io.Serializable;

/**
 *
 * @author Rafael M. Pestano Ago 08, 2011 7:28:32 PM
 * 
 * 
 * Event of pushing an item to the {@see StateController#stateItens}
 */

public class StatePushEvent implements Serializable{
    
    private StateItem stackItem;

    public StatePushEvent() {
    }

    
    public StatePushEvent(StateItem itemToPush) {
        this.stackItem = itemToPush;
    }

    public StateItem getStackItem() {
        return stackItem;
    }

    public void setStackItem(StateItem item) {
        this.stackItem = item;
    }
    
}
