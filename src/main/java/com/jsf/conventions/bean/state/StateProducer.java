/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jsf.conventions.bean.state;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 *
 * @author rmpestano Mar 26, 2012 at 10:50:49 PM
 */
public class StateProducer {

    @Produces
    @Named
     public State findState(){
        return CrudState.FIND;
    }
    
    @Produces
    @Named
    public State insertState(){
        return CrudState.INSERT;
    }
    
    @Produces
    @Named
    public State updateState(){
        return CrudState.UPDATE;
    }
}
