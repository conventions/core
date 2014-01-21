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

import java.io.Serializable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * 
 *
 * @author rmpestano Aug 21, 2011 9:33:12 AM
 */
@MappedSuperclass
public abstract class AbstractBaseEntity<T extends Serializable> implements Serializable,BaseEntity<T> {
  

    @Transient
    public boolean isTransient() {
        return getId() == null;
    }
    
    @Transient
    @Override
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
