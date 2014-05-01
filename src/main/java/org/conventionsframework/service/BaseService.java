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
package org.conventionsframework.service;

import org.conventionsframework.dao.BaseHibernateDao;
import org.conventionsframework.model.BaseEntity;
import org.conventionsframework.model.PaginationResult;
import org.conventionsframework.model.SearchModel;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;

import javax.persistence.EntityManager;

/**
 *
 * @author Rafael M. Pestano Mar 19, 2011 9:13:05 AM
 */
public interface BaseService<T extends BaseEntity> {

    /**
     *
     * @param Entity
     */
    void store(T Entity);

    void afterStore(T entity);

    void beforeStore(T entity);

    void remove(T entity);

    void beforeRemove(T entity);

    void afterRemove(T entity);
    
    BaseHibernateDao<T> getDao();

    void setDao(BaseHibernateDao<T> dao);

    /**
     * database pagination over a search model which contains necessary information
     * to get a database page
     * @param searchModel
     * @return PaginationResult containing database page and total rows returned by pagination
     */
    PaginationResult<T> paginate(SearchModel<T> searchModel);

    DetachedCriteria getDetachedCriteria();

    Criteria getCriteria();

    Class<T> getPersistentClass();

    EntityManager getEntityManager();

    void setEntityManager(EntityManager em);

}
