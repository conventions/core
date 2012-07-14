/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.Version;

/**
 *
 * @author rmpestano Dec 11, 2011 5:04:59 PM
 */
@MappedSuperclass
public abstract class AbstractVersionatedBaseEntity<T extends Serializable> extends AbstractBaseEntity<T> {
    
    private Date createDate;
    private Date updateDate;
    private Integer version;
    
      @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date dataAtualizacao) {
        this.updateDate = dataAtualizacao;
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date dataCriacao) {
        this.createDate = dataCriacao;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @PrePersist
    public void beforePersist() {
       this.createDate = new Date();
       this.updateDate = new Date();
       this.version = 0;
    }
    
    @PreUpdate
    public void beforeUpdate(){
        this.updateDate = new Date();
        this.version++;
    }
    
    
}
