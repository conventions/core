/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jsf.conventions.bean.state;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 *
 * @author Rafael M. Pestano Mar 19, 2011 5:59:22 PM
 */
public enum CrudState implements State{
    INSERT("insert"),UPDATE("update"),FIND("find");
    
    private final String stateName ;
    
    CrudState(String stateName){
        this.stateName = stateName;
    }

    @Override
    public String getStateName() {
        return this.stateName;
    }

    @Override
    public String toString() {
        return this.stateName;
    }
    
   
    
}
