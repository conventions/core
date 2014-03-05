/*
 * Copyright 2011-2013 Conventions Framework.  
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.conventionsframework.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author rmpestano Dec 11, 2011 5:04:59 PM
 */
@MappedSuperclass
public abstract class AbstractVersionatedEntity<T extends Serializable> extends AbstractBaseEntity<T> {

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updateDate;
    @Version
    private Integer version;
    

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date dataAtualizacao) {
        this.updateDate = dataAtualizacao;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date dataCriacao) {
        this.createDate = dataCriacao;
    }

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
