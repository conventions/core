/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * 
 *
 * @author rmpestano Aug 21, 2011 9:33:12 AM
 */
@MappedSuperclass
public abstract class BaseEntityString extends AbstractBaseEntity<String> {
    
    private String id;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
    

   
    
}
