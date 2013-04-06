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

import org.conventionsframework.dao.BaseHibernateDao;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.conventionsframework.model.WrappedData;
import org.conventionsframework.qualifier.PersistentClass;
import org.conventionsframework.service.BaseService;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.conventionsframework.entitymanager.EntityManagerProvider;
import org.conventionsframework.qualifier.Log;
import org.conventionsframework.qualifier.Service;
import org.conventionsframework.util.BeanManagerController;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.loader.custom.ScalarReturn;
import org.hibernate.transform.ResultTransformer;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Rafael M. Pestano Mar 19, 2011 11:55:37 AM
 */
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public abstract class BaseServiceImpl<T, K extends Serializable> implements BaseService<T, K>, Serializable {

    protected BaseHibernateDao<T, K> dao;
    @Inject
    @Log
    private transient Logger log;

  
    public void initDao() {
        dao = (BaseHibernateDao<T, K>) BeanManagerController.getBeanByName("baseHibernateDao");
        dao.setEntityManagerProvider(getEntityManagerProvider());
        dao.setPersistentClass(getPersistentClass());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void store(T entity) {
        doStore(entity);
    }

    public final void doStore(T entity) {
        this.beforeStore(entity);
        this.saveOrUpdate(entity);
        this.afterStore(entity);
    }

    @Override
    public void afterStore(T entity) {
    }

    @Override
    public void beforeStore(T entity) {
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void remove(T entity) {
        this.doRemove(entity);
    }

    @Override
    public void beforeRemove(T entity) {
    }

    public final void doRemove(T entity) {
        this.beforeRemove(entity);
        this.delete(entity);
        this.afterRemove(entity);
    }

    @Override
    public void afterRemove(T entity) {
    }

    /**
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
    public DetachedCriteria configFindPaginated(Map<String, String> columnFilters, Map<String, Object> externalFilter) {
        DetachedCriteria dc = getDetachedCriteria();
        getDao().addBasicFilterRestrictions(dc, externalFilter);
        getDao().addBasicFilterRestrictions(dc, columnFilters);
        return dc;
    }
    
    public DetachedCriteria configFindPaginated(Map<String, String> columnFilters, Map<String, Object> externalFilter,DetachedCriteria dc) {
        getDao().addBasicFilterRestrictions(dc, externalFilter);
        getDao().addBasicFilterRestrictions(dc, columnFilters);
        return dc;
    }

    @Override
    public int countAll() {
        return getDao().countAll();
    }

    @Override
    public T load(K id) {
        return (T) getDao().load(id);
    }

    @Override
    public T get(K id) {
        return (T) getDao().get(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(T entity) {
        getDao().save(entity);
        flushSession();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(T entity) {
        getDao().update(entity);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(T entity) {
        getDao().delete(entity);
        flushSession();
    }

    @TransactionAttribute
    public T merge(T entity) {
        return getDao().merge(entity);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveOrUpdate(T entity) {
        getDao().saveOrUpdate(entity);
        flushSession();
    }

    @Override
    public T refresh(T entity) {
        return (T) getDao().refresh(entity);
    }

    @Override
    public List<T> findAll() {
        return getDao().findAll();
    }

    @Override
    public List<T> findAll(Integer first, Integer max) {
        return getDao().findAll(first, max);
    }

    @Override
    public BaseHibernateDao<T, K> getDao() {
        if(dao == null){
            initDao();
        }
        return dao;
    }

    public void setDao(BaseHibernateDao<T, K> dao) {
        this.dao = dao;
    }

    @Override
    public Session getSession() {
        return getDao().getSession();
    }

    @Override
    public List<T> findByExample(T entity) {
        return getDao().findByExample(entity);
    }

    @Override
    public List<T> findByExample(final T entity, MatchMode matchMode) {
        return getDao().findByExample(entity, matchMode);
    }

    @Override
    public List<T> findByExample(final T entity, int maxResult) {
        return getDao().findByExample(entity, maxResult);
    }

    @Override
    public List<T> findByExample(final T entity, int maxResult, MatchMode matchMode) {
        return getDao().findByExample(entity, maxResult, matchMode);
    }

    @Override
    public T findOneByExample(T entity) {
        return (T) getDao().findOneByExample(entity);
    }

    @Override
    public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
        return getDao().findByExample(exampleInstance, excludeProperty);
    }

    @Override
    public T findOneByExample(T entity, MatchMode matchMode) {
        return (T) getDao().findOneByExample(entity, matchMode);
    }

    @Override
    public List<T> findByCriteria(DetachedCriteria criteriaObject, int first, int maxResult) {
        return getDao().findByCriteria(criteriaObject, first, maxResult);
    }

    @Override
    public List<T> findByCriteria(DetachedCriteria criteriaObject) {
        return getDao().findByCriteria(criteriaObject);
    }

    @Override
    public WrappedData<T> findPaginated(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> columnFilters, Map<String, Object> externalFilters) {
        DetachedCriteria dc = configFindPaginated(columnFilters, externalFilters);
        return getDao().executePagination(first, pageSize, sortField, sortOrder, dc);
    }

    @Override
    public Long getRowCount(DetachedCriteria criteria) {
        return getDao().getRowCount(criteria);
    }

    @Override
    public DetachedCriteria getDetachedCriteria() {
        return getDao().getDetachedCriteria();
    }

    @Override
    public Criteria getCriteria() {
        return getDao().getCriteria();
    }

    /**
     *
     * @param nativeQuery
     * @param params
     * @param class entity
     * @param result transformer
     * @param Scalar
     * @return
     */
    @Override
    public List findByNativeQuery(String nativeQuery, Map params, Class entity, ResultTransformer rt, ScalarReturn scalar) {
        return getDao().findByNativeQuery(nativeQuery, params, entity, rt, scalar);
    }

    public void flushSession() {
        if (isConventionsFlushSession()) {
            Session session = getSession();
            if (session.isOpen() && session.isDirty()) {
                session.flush();
            }
        }
    }

    @Override
    public final EntityManager getEntityManager() {
        return getDao().getEntityManager();
    }

    public void setPersistentClass(Class<T> clazz) {
        getDao().setPersistentClass(clazz);
    }

    public void setEntityManagerProvider(EntityManagerProvider entityManagerProvider) {
        getDao().setEntityManagerProvider(entityManagerProvider);
    }

    /**
     * search persistentClass to set in dao layer
     *
     * @param ip
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Class findPersistentClass(InjectionPoint ip) throws InstantiationException, IllegalAccessException {
        //try to get persistenceClass from class level annotation
        for (Annotation annotation : getClass().getAnnotations()) {
            if (annotation instanceof PersistentClass) {
                PersistentClass p = (PersistentClass) annotation;
                Class c = p.value();
                return c;
            }
        }

        if (ip != null && ip.getAnnotated() != null) {

            if (ip.getAnnotated().isAnnotationPresent(Service.class)) {
                //try to get persistentClass from injectionPoint via @Service(entity=SomeClass.class) annotation
                Class persistentClass = ip.getAnnotated().getAnnotation(Service.class).entity();
                if (!persistentClass.isPrimitive()) {
                    return persistentClass;
                }
            }
            if (ip.getAnnotated().isAnnotationPresent(PersistentClass.class)) {
                Class persistentClass = ip.getAnnotated().getAnnotation(PersistentClass.class).value();
                if (!persistentClass.isPrimitive()) {
                    return persistentClass;
                }
            }

        }//end  ip != null && ip.getAnnotated() != null
        // try resolve via reflection
        try {
            return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        } catch (Exception ex) {
            log.log(Level.WARNING, "Conventions service: could not find persistent class for service:" + getClass().getSimpleName() + " it will be resolved to null.(ignore this warn if you're using @Service annotation)");
        }
        return null;
    }

    public void addBasicFilterRestrictions(DetachedCriteria dc, Map restrictionFilter) {
        try {
            getDao().addBasicFilterRestrictions(dc, restrictionFilter);

        } catch (Exception ex) {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "Problem trying to infer restrictions from filter:" + ex.getMessage());
            }
        }

    }

    /**
     * @return true if hibernate session should be flushed by conventions after
     * insert/update methods in baseService false otherwise default is true
     */
    public boolean isConventionsFlushSession() {
        return true;
    }

    public WrappedData<T> executePagination(final int first, final int pageSize, String sortField, SortOrder sortOrder, final DetachedCriteria dc) {
        return getDao().executePagination(first, pageSize, sortField, sortOrder, dc);
    }
}
