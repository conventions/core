/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.event;

import com.jsf.conventions.model.StateItem;
import java.io.Serializable;

/**
 *
 * @author Rafael M. Pestano Ago 08, 2011 7:28:32 PM
 * 
 * 
 * Back event
 */

public class StateBackEvent implements Serializable{
    
    private StateItem stateItem;

    public StateBackEvent() {
    }

    
    public StateBackEvent(StateItem item) {
        this.stateItem = item;
    }

    public StateItem getStateItem() {
        return stateItem;
    }

    public void setStateItem(StateItem item) {
        this.stateItem = item;
    }
    
}
