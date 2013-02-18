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
package org.conventionsframework.dao;

import org.conventionsframework.model.AbstractBaseEntity;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.conventionsframework.model.WrappedData;
import javax.persistence.EntityManager;
import org.conventionsframework.entitymanager.EntityManagerProvider;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.loader.custom.ScalarReturn;
import org.hibernate.transform.ResultTransformer;
import org.primefaces.model.SortOrder;

/**
 * @author Rafael M. Pestano
 *
 */
public interface BaseHibernateDao<T, Id extends Serializable> extends Serializable {

    T load(Id id);

    T get(Id id);

    void save(T entity);

    void update(T entity);

    T merge(T entity);

    void delete(T entity);

    T refresh(T entity);

    void saveOrUpdate(T entity);

    List<T> findAll();

    List<T> findAll(final Integer first, final Integer max);

    WrappedData<T> configFindPaginated(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> columnFilters, Map<String, Object> externalFilters);

    int countAll();

    abstract Class<T> getPersistentClass();

    void setPersistentClass(Class<T> clazz);

    Session getSession();

    List<T> findByExample(final T entity);

    List<T> findByExample(final T entity, int maxResult);

    List<T> findByExample(final T entity, MatchMode matchMode);

    List<T> findByExample(final T entity, int maxResult, MatchMode matchMode);

    T findOneByExample(final T entity);

    List<T> findByExample(T exampleInstance, String[] excludeProperty);

    T findOneByExample(final T entity, MatchMode matchMode);

    List<T> findByCriteria(DetachedCriteria criteriaObject, int first, int maxResult);

    List<T> findByCriteria(DetachedCriteria criteriaObject);

    WrappedData<T> findPaginated(final int first, final int pageSize, String sortField, SortOrder sortOrder, DetachedCriteria dc);

    Long getRowCount(final DetachedCriteria criteria);

    DetachedCriteria getDetachedCriteria();

    Criteria getCriteria();

    List findByNativeQuery(String nativeQuery, Map params, Class entity, ResultTransformer rt, ScalarReturn scalar);

    EntityManager getEntityManager();

    abstract EntityManagerProvider getEntityManagerProvider();
    
    void setEntityManagerProvider(EntityManagerProvider entityManagerProvider);

    void addBasicFilterRestrictions(DetachedCriteria dc, Map externalFilters);
}
