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

import org.conventionsframework.model.BaseEntity;
import org.conventionsframework.model.PaginationResult;
import org.conventionsframework.model.SearchModel;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.loader.custom.ScalarReturn;
import org.hibernate.transform.ResultTransformer;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Rafael M. Pestano
 *
 */
public interface BaseHibernateDao<T extends BaseEntity, Id extends Serializable> extends Serializable {

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

    int countAll();

    void setPersistentClass(Class<T> clazz);

    Session getSession();

    List<T> findByExample(final T entity);

    List<T> findByExample(final T entity, int maxResult);

    List<T> findByExample(final T entity, MatchMode matchMode);

    List<T> findByExample(final T entity, int maxResult, MatchMode matchMode);

    List<T> findByExample(T exampleInstance, String[] excludeProperty);

    T findOneByExample(final T entity);

    T findOneByExample(final T entity, MatchMode matchMode);

    List<T> findByCriteria(DetachedCriteria criteriaObject, int first, int maxResult);

    List<T> findByCriteria(DetachedCriteria criteriaObject);

    T findOneByCriteria(DetachedCriteria dc);

    T findOneByCriteria(Criteria criteria);

    /**
     * @param searchModel
     * @return a populated detachedCriteria based on given searchModel
     */
    DetachedCriteria configPagination(SearchModel<T> searchModel);

    /**
     *
     * @param searchModel
     * @param dc pre populated criteria
     * @return same pre populated detachedCriteria adding basic restrictions based on filter map
     * and adds hibernate Example restriction based on searchModel entity
     *
     */
    DetachedCriteria configPagination(SearchModel<T> searchModel, DetachedCriteria dc);

    /**
     * database pagination over a search model which contains necessary information
     * to get a database page
     * @param searchModel
     * @param dc populated detachedCriteria used to execute database pagination
     * @return PaginationResult containing database page and total rows returned by pagination
     */
    PaginationResult<T> executePagination(SearchModel<T> searchModel, DetachedCriteria dc);

    Long getRowCount(final DetachedCriteria criteria);

    DetachedCriteria getDetachedCriteria();

    Criteria getCriteria();

    List findByNativeQuery(String nativeQuery, Map params, Class entity, ResultTransformer rt, ScalarReturn scalar);

    void addBasicFilterRestrictions(DetachedCriteria dc, Map externalFilters);

    Class<T> getPersistentClass();

    EntityManager getEntityManager();

    void setEntityManager(EntityManager em);

}
