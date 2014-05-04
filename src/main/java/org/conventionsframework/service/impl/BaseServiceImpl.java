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
import org.conventionsframework.model.BaseEntity;
import org.conventionsframework.model.PaginationResult;
import org.conventionsframework.model.SearchModel;
import org.conventionsframework.qualifier.Dao;
import org.conventionsframework.qualifier.Log;
import org.conventionsframework.qualifier.PersistentClass;
import org.conventionsframework.qualifier.Service;
import org.conventionsframework.service.BaseService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael M. Pestano Mar 19, 2011 11:55:37 AM
 *
 */
@Service
@Named("baseService")
public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T>, Serializable {

    @Inject
    @Dao
    protected BaseHibernateDao<T> dao;

    @Inject
    protected EntityManager em;


    @Inject
    @Log
    private transient Logger log;


    @Inject
    public void initService(InjectionPoint ip) {
        if(dao.getPersistentClass() == null){
            dao.setPersistentClass(findPersistentClass(ip));
        }
        dao.setEntityManager(getEntityManager());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void store(T entity) {
        doStore(entity);
    }

    public final void doStore(T entity) {
        this.beforeStore(entity);
        getDao().saveOrUpdate(entity);
        flushSession();
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
        if(!getEntityManager().contains(entity)){
            entity = getDao().load((Serializable) ((BaseEntity)entity).getId());
        }
        this.doRemove(entity);
    }


    @Override
    public void beforeRemove(T entity) {
    }

    public final void doRemove(T entity) {
        this.beforeRemove(entity);
        getDao().delete(entity);
        flushSession();
        this.afterRemove(entity);
    }

    @Override
    public void afterRemove(T entity) {
    }


    public DetachedCriteria configPagination(SearchModel<T> searchModel) {
        return getDao().configPagination(searchModel);
    }

    public DetachedCriteria configPagination(SearchModel<T> searchModel, DetachedCriteria dc) {
        return getDao().configPagination(searchModel,dc);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public PaginationResult<T> executePagination(SearchModel<T> searchModel) {
        return getDao().executePagination(searchModel, configPagination(searchModel));
    }


    @Override
    public BaseHibernateDao<T> getDao() {
        return dao;
    }

    public void setDao(BaseHibernateDao<T> dao) {
        this.dao = dao;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public DetachedCriteria getDetachedCriteria() {
        return getDao().getDetachedCriteria();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
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

        //injectionPoint generic type persistenceClass
        if (ip != null && ip.getAnnotated() != null) {
            ParameterizedType type = null;
            try{
                //get type from generic injection @Inject BaseService<Entity,ID>
                type = (ParameterizedType) ip.getType();
                if(type != null){
                    Type[] typeArgs = type.getActualTypeArguments();
                    if(typeArgs != null && typeArgs.length == 1){
                        return (Class<T>) typeArgs[0];
                    }
                }
            }catch (Exception ex){
            	//intentional
            }
           

            //injectionPoint PersistenceClass
            if (ip.getAnnotated().isAnnotationPresent(PersistentClass.class)) {
                Class persistentClass = ip.getAnnotated().getAnnotation(PersistentClass.class).value();
                if (!persistentClass.isPrimitive()) {
                    return persistentClass;
                }
            }
             //class level annotation
            for (Annotation annotation : getClass().getAnnotations()) {
                if (annotation instanceof PersistentClass) {
                    PersistentClass p = (PersistentClass) annotation;
                    Class c = p.value();
                    return c;
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


        @Override
    public Class<T> getPersistentClass() {
        return dao.getPersistentClass();
    }

    /**
     * @return true if hibernate session should be flushed by conventions after
     * insert/update methods in baseService false otherwise default is true
     */
    public boolean isConventionsFlushSession() {
        return true;
    }


    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public void setEntityManager(EntityManager em){
    	this.em = em;
    }

}
