/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.model;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * 
 *
 * @author rmpestano Aug 21, 2011 9:33:12 AM
 */
@MappedSuperclass
public abstract class AbstractBaseEntity<T extends Serializable> implements Serializable,SelectItemAware {
  

    @Transient
    public boolean isTransient() {
        return getId() == null;
    }
    
    @Transient
    @Override
    public String getLabel(){
        return this.toString();
    }
    
    @Override
    public String toString() {
        return "" + getId();
    }
    
    @Transient
    public abstract T getId();

    public abstract void setId(T id);

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractBaseEntity<T> other = (AbstractBaseEntity<T>) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }
    
    
}
