/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 
 *
 * @author rmpestano Aug 21, 2011 9:33:12 AM
 */
@MappedSuperclass
public abstract class VersionatedBaseEntityLong extends AbstractVersionatedBaseEntity<Long>  {
    
    private Long id;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

   
    
}
