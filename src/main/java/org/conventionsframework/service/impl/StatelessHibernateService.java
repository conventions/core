/*
 * Copyright 2012 Conventions Framework.  
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
package org.conventionsframework.service.impl;

import org.conventionsframework.qualifier.Log;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import org.conventionsframework.entitymanager.EntityManagerProvider;
import org.conventionsframework.qualifier.*;

/**
 * Stateless EJB based service
 * 
 * @author Rafael M. Pestano Dec 4, 2011 9:40:01 PM
 */
@Service(type= Type.STATELESS)
@Named(value=Service.STATELESS)
public class StatelessHibernateService<T,K extends Serializable> extends BaseServiceImpl<T, K> {
    
    @Inject @Log
    private transient Logger log;
    
    protected Class<T> persistenceClass;
    
    @Inject @ConventionsEntityManager(type= Type.STATELESS) 
    protected EntityManagerProvider entityManagerProvider;
    
    
    @Inject
    public void StatelessHibernateService(InjectionPoint ip) {
        try {
            this.persistenceClass = this.findPersistentClass(ip);
        } catch (Exception ex) {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "Conventions:could not resolve persistent class for service:" + this.getClass().getSimpleName() + ", message:" + ex.getMessage());
            }
        }
    }

    public Class<T> getPersistentClass() {
        return persistenceClass;
    }

    public EntityManagerProvider getEntityManagerProvider() {
        return entityManagerProvider;
    }

    
   
}

