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
import org.conventionsframework.model.WrappedData;
import org.conventionsframework.qualifier.Dao;
import org.conventionsframework.qualifier.Log;
import org.conventionsframework.qualifier.PersistentClass;
import org.conventionsframework.qualifier.Service;
import org.conventionsframework.service.BaseService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.primefaces.model.SortOrder;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael M. Pestano Mar 19, 2011 11:55:37 AM
 *
 */
@Service
public class BaseServiceImpl<T, K extends Serializable> implements BaseService<T, K>, Serializable {

    @Inject
    @Dao
    protected BaseHibernateDao<T, K> dao;

    @PersistenceContext
    EntityManager em;

    Class<T> persistentClass;

    @Inject
    @Log
    private transient Logger log;


    @Inject
    public void initService(InjectionPoint ip) {
        persistentClass = findPersistentClass(ip);
        dao.setEntityManager(getEntityManager());
        dao.setPersistentClass(persistentClass);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void store(T entity) {
        doStore(entity);
    }

    public final void doStore(T entity) {
        this.beforeStore(entity);
        getDao().saveOrUpdate(entity);
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
        getDao().delete(entity);
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

    public DetachedCriteria configFindPaginated(Map<String, String> columnFilters, Map<String, Object> externalFilter, DetachedCriteria dc) {
        getDao().addBasicFilterRestrictions(dc, externalFilter);
        getDao().addBasicFilterRestrictions(dc, columnFilters);
        return dc;
    }


    @Override
    public BaseHibernateDao<T, K> getDao() {
        return dao;
    }

    public void setDao(BaseHibernateDao<T, K> dao) {
        this.dao = dao;
    }


    @Override
    public WrappedData<T> findPaginated(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> columnFilters, Map<String, Object> externalFilters) {
        DetachedCriteria dc = configFindPaginated(columnFilters, externalFilters);
        return getDao().executePagination(first, pageSize, sortField, sortOrder, dc);
    }

    @Override
    public DetachedCriteria getDetachedCriteria() {
        return getDao().getDetachedCriteria();
    }

    @Override
    public Criteria getCriteria() {
        return getDao().getCriteria();
    }


    public void flushSession() {
        if (isConventionsFlushSession()) {
            Session session = getDao().getSession();
            if (session.isOpen() && session.isDirty()) {
                session.flush();
            }
        }
    }


    public void setPersistentClass(Class<T> clazz) {
        getDao().setPersistentClass(clazz);
    }


    /**
     * search persistentClass to set in dao layer
     *
     * @param ip
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Class findPersistentClass(InjectionPoint ip) {
        //try to get persistenceClass from class level annotation
        for (Annotation annotation : getClass().getAnnotations()) {
            if (annotation instanceof PersistentClass) {
                PersistentClass p = (PersistentClass) annotation;
                Class c = p.value();
                return c;
            }
        }

        if (ip != null && ip.getAnnotated() != null) {
            ParameterizedType type = null;
            try{
                //get type from generic injection @Inject BaseService<Entity,ID>
                type = (ParameterizedType) ip.getType();
            }catch (ClassCastException ex){}
            if(type != null){
                Type[] typeArgs = type.getActualTypeArguments();
                if(typeArgs != null && typeArgs.length == 2){
                    return (Class<T>) typeArgs[0];
                }
            }
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

    @Override
    public Class<T> getPersistentClass() {
        return persistentClass;
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

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
