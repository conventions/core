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
 * Event of pulling an item from the <code>StateController</code>
 * @see StateController#stateItens
 */
public class StatePullEvent  implements Serializable {
    
    private StateItem stackItem;

    public StatePullEvent() {
    }

    public StatePullEvent(StateItem stackItem) {
        this.stackItem = stackItem;
    }

    public StateItem getStackItem() {
        return stackItem;
    }

    public void setStackItem(StateItem stackItem) {
        this.stackItem = stackItem;
    }

    
}
