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
package org.conventionsframework.service.impl;

import org.conventionsframework.qualifier.Log;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.extensions.cdi.jpa.api.Transactional;
import org.conventionsframework.entitymanager.EntityManagerProvider;
import org.conventionsframework.model.WrappedData;
import org.conventionsframework.qualifier.*;
import org.hibernate.criterion.DetachedCriteria;
import org.primefaces.model.SortOrder;

/**
 * Non JavaEE(EJB) dependent Service
 *
 * for this service work properly an EntityManagerProvider implementation should
 * be provided
 *
 * an example can be found here:
 * {@link https://github.com/conventions/issuetracker-weld/blob/master/src/br/com/triadworks/issuetracker/entitymanager/provider/IssueTrackerProvider.java}
 * and here:
 * {@link http://conventions.github.com/docs/#d0e161}
 *
 * @author Rafael M. Pestano Jun 12, 2012 7:18:29 PM
 */
@Dependent
@Service(type = Type.CUSTOM)
@Named(value = Service.CUSTOM)
public class CustomHibernateService<T, K extends Serializable> extends BaseServiceImpl<T, K> {

    protected Class<T> persistenceClass;
    @Inject
    @ConventionsEntityManager(type = Type.CUSTOM)
    protected EntityManagerProvider entityManagerProvider;

    public CustomHibernateService() {
    }
    @Inject
    @Log
    private transient Logger log;

    @Inject
    public void CustomHibernateService(InjectionPoint ip) {
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

    @Override
    @Transactional
    public void store(T entity) {
        super.store(entity);
    }

    @Override
    @Transactional
    public void remove(T entity) {
        super.remove(entity);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        super.delete(entity);
    }

    @Override
    @Transactional
    public void save(T entity) {
        super.save(entity);
    }

    @Override
    @Transactional
    public void saveOrUpdate(T entity) {
        super.saveOrUpdate(entity);
    }

    @Override
    @Transactional
    public void update(T entity) {
        super.update(entity);
    }

}