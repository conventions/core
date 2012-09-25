/*
 * Copyright 2011-2012 Conventions Framework.  
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
package org.conventionsframework.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.conventionsframework.dao.BaseHibernateDao;
import org.conventionsframework.entitymanager.EntityManagerProvider;
import org.conventionsframework.model.WrappedData;
import org.conventionsframework.qualifier.Log;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.loader.custom.ScalarReturn;
import org.hibernate.transform.ResultTransformer;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Rafael M. Pestano Jul 23, 2012 8:58:11 PM
 */
public class BaseHibernateDaoImpl<T, K extends Serializable> implements BaseHibernateDao<T, K>, Serializable {

    private Class<T> persistentClass;
    private Session session;
    @Inject
    @Log
    private transient Logger log;
    private EntityManagerProvider entityManagerProvider;

    public BaseHibernateDaoImpl() {
    }

    public BaseHibernateDaoImpl(EntityManagerProvider entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    public BaseHibernateDaoImpl(Class<T> persistentClass, EntityManagerProvider entityManagerProvider) {
        this.persistentClass = persistentClass;
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    public Session getSession() {
        if (session == null || !session.isOpen()) {
            if (getEntityManager().getDelegate() instanceof org.hibernate.ejb.HibernateEntityManager) {
                session = ((org.hibernate.ejb.HibernateEntityManager) getEntityManager().getDelegate()).getSession();
            } else {
                session = (org.hibernate.Session) getEntityManager().getDelegate();
            }
        }
        return session;
    }

    public EntityManagerProvider getEntityManagerProvider() {
        return entityManagerProvider;
    }

    public void setEntityManagerProvider(EntityManagerProvider entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    public EntityManager getEntityManager() {
        return getEntityManagerProvider().getEntityManager();
    }

    @Override
    public Class<T> getPersistentClass() {
        return this.persistentClass;
    }

    public void setPersistentClass(Class<T> clazz) {
        this.persistentClass = clazz;
    }

    @Override
    public T load(K id) {
        return (T) this.getSession().load(this.persistentClass, id);
    }

    @Override
    public T get(K id) {
        return (T) this.getSession().get(this.persistentClass, id);
    }

    @Override
    public void save(T entity) {
        this.getSession().save(entity);
    }

    public T merge(T entity) {
        return (T) getSession().merge(entity);
    }

    @Override
    public void update(T entity) {
        this.getSession().update(entity);
    }

    @Override
    public void delete(T entity) {
        this.getSession().delete(entity);
    }

    @Override
    public T refresh(T entity) {
        this.getSession().refresh(entity);
        return entity;
    }

    @Override
    public void saveOrUpdate(T entity) {
        this.getSession().saveOrUpdate(entity);
    }

    @Override
    public List<T> findAll() {
        Criteria criteria = getSession().createCriteria(persistentClass);
        return criteria.list();
    }

    @Override
    public List<T> findAll(Integer first, Integer max) {
        Criteria criteria = getSession().createCriteria(persistentClass);
        criteria.setFirstResult(first);
        criteria.setMaxResults(max);
        return criteria.list();
    }

    @Override
    public List<T> findByExample(final T entity) {
        Criteria criteria = getSession().createCriteria(persistentClass);
        criteria.add(Example.create(entity).enableLike(MatchMode.ANYWHERE).ignoreCase());
        return criteria.list();
    }

    @Override
    public T findOneByExample(final T entity) {
        Criteria criteria = getSession().createCriteria(persistentClass);
        criteria.add(Example.create(entity).enableLike(MatchMode.ANYWHERE).ignoreCase());
        return (T) criteria.uniqueResult();

    }

    @Override
    public List<T> findByExample(final T entity, MatchMode matchMode) {
        Criteria criteria = getSession().createCriteria(persistentClass);
        criteria.add(Example.create(entity).enableLike(matchMode).ignoreCase());
        return criteria.list();
    }

    @Override
    public List<T> findByExample(final T entity, int maxResult) {
        Criteria criteria = getSession().createCriteria(persistentClass);
        criteria.add(Example.create(entity).enableLike(MatchMode.ANYWHERE).ignoreCase());
        criteria.setMaxResults(maxResult);
        return criteria.list();
    }

    @Override
    public List<T> findByExample(final T entity, int maxResult, MatchMode matchMode) {
        Criteria criteria = getSession().createCriteria(persistentClass);
        criteria.add(Example.create(entity).enableLike(matchMode).ignoreCase());
        criteria.setMaxResults(maxResult);
        return criteria.list();
    }

    @Override
    public T findOneByExample(final T entity, MatchMode matchMode) {
        Criteria criteria = getSession().createCriteria(persistentClass);
        criteria.add(Example.create(entity).enableLike(matchMode).ignoreCase());
        return (T) criteria.uniqueResult();

    }

    @Override
    public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
        Criteria crit = getSession().createCriteria(persistentClass);
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }

    /**
     * Hibernate implementation of pagination
     *
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @param dc
     * @return
     */
    @Override
    public WrappedData<T> findPaginated(final int first, final int pageSize, String sortField, SortOrder sortOrder, final DetachedCriteria dc) {

        int size = getRowCount(dc).intValue();

        if (sortField != null && !sortOrder.equals(SortOrder.UNSORTED)) {
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                dc.addOrder(Order.asc(sortField));
            } else {
                dc.addOrder(Order.desc(sortField));
            }

        }


        List<T> data = this.findByCriteria(dc, first, pageSize);


        return new WrappedData<T>(data, size);
    }

    /**
     * basic implementation of pagination without restrictions
     *
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @param columnFilters primefaces datatable column filters
     * @param externalFilters filters outside datatable- eg: inputText, sliders,
     * autocomplete etc..
     * @return wrapped data with paginated list and rowCount
     */
    @Override
    public WrappedData<T> configFindPaginated(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> columnFilters, Map<String, Object> externalFilters) {
        DetachedCriteria dc = DetachedCriteria.forClass(persistentClass);

        if (externalFilters != null && !externalFilters.isEmpty()) {
            this.addBasicFilterRestrictions(dc, externalFilters);
        }
        if (columnFilters != null && !columnFilters.isEmpty()) {
            this.addBasicFilterRestrictions(dc, columnFilters);
        }
        return this.findPaginated(first, pageSize, sortField, sortOrder, dc);
    }

    @Override
    public Long getRowCount(final DetachedCriteria dc) {
        Criteria criteria = dc.getExecutableCriteria(getSession());
        criteria.setProjection(Projections.rowCount());
        criteria.setFirstResult(0);
        criteria.setMaxResults(1);
        Long result = (Long) criteria.uniqueResult();
        dc.setProjection(null).setResultTransformer(Criteria.ROOT_ENTITY);
        return result;
    }

    @Override
    public int countAll() {
        Criteria criteria = getSession().createCriteria(persistentClass);
        criteria.setProjection(Projections.rowCount());
        criteria.setFirstResult(0);
        criteria.setMaxResults(1);
        return ((Long) criteria.uniqueResult()).intValue();
    }

    @Override
    public List<T> findByCriteria(DetachedCriteria dc, int first, int maxResult) {
        Criteria criteria = dc.getExecutableCriteria(getSession());
        criteria.setFirstResult(first);
        criteria.setMaxResults(maxResult);
        return criteria.list();
    }

    @Override
    public List<T> findByCriteria(DetachedCriteria dc) {
        return dc.getExecutableCriteria(getSession()).list();
    }

    @Override
    public DetachedCriteria getDetachedCriteria() {
        return DetachedCriteria.forClass(persistentClass);
    }

    @Override
    public Criteria getCriteria() {
        return getSession().createCriteria(persistentClass);
    }

    /**
     *
     * @param nativeQuery
     * @param params
     * @param class entity to be queried, if no entity is passed persistentClass
     * will be used
     * @param result transformer
     * @param Scalar
     * @return
     */
    @Override
    public List findByNativeQuery(String nativeQuery, Map params, Class entity, ResultTransformer rt, ScalarReturn scalar) {
        SQLQuery query = getSession().createSQLQuery(nativeQuery);
        if (scalar != null) {
            query.addScalar(scalar.getColumnAlias(), scalar.getType());
        }
        if (rt != null) {
            query.setResultTransformer(rt);
        }
        if (entity != null) {
            query.addEntity(entity);
        } else {
            query.addEntity(getPersistentClass());
        }

        Set<Map.Entry> rawParameters = params.entrySet();
        for (Map.Entry entry : rawParameters) {
            query.setParameter(entry.getKey().toString(), entry.getValue());
        }
        return query.list();
    }

    /**
     * try to infer the restrictions, for more complex restriction override
     * configFindPaginated
     *
     * @param dc
     * @param externalFilter
     */
    public void addBasicFilterRestrictions(DetachedCriteria dc, Map externalFilter) {
        for (Iterator<Map.Entry<String, Object>> it = (Iterator<Map.Entry<String, Object>>) externalFilter.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Object> entry = it.next();
            if (entry != null && entry.getValue() != null && !"".equals(entry.getValue())) {
                Field f;
                try {
                    f = persistentClass.getDeclaredField(entry.getKey());
                    if (f.getType().isAssignableFrom(String.class)) {
                        dc.add(Restrictions.ilike(entry.getKey(), (String) entry.getValue(), MatchMode.ANYWHERE));
                    } else if (f.getType().isAssignableFrom(Integer.class) || f.getType().isAssignableFrom(int.class)) {
                        dc.add(Restrictions.eq(entry.getKey(), Integer.parseInt((String) entry.getValue())));
                    } else if (f.getType().isAssignableFrom(Long.class) || f.getType().isAssignableFrom(long.class)) {
                        dc.add(Restrictions.eq(entry.getKey(), Long.parseLong((String) entry.getValue())));
                    } else if (f.getType().isAssignableFrom(Date.class)) {
                        dc.add(Restrictions.eq(entry.getKey(), (Date) entry.getValue()));
                    } else if (f.getType().isAssignableFrom(Calendar.class)) {
                        dc.add(Restrictions.eq(entry.getKey(), (Calendar) entry.getValue()));
                    }
                } catch (NoSuchFieldException ex) {
                } catch (SecurityException ex) {
                }
            }
        }


    }
}
