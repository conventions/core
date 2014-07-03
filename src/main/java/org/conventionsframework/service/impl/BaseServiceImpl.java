/*
 * Copyright 2011-2014 Conventions Framework.
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

import org.conventionsframework.crud.Crud;
import org.conventionsframework.model.BaseEntity;
import org.conventionsframework.model.PaginationResult;
import org.conventionsframework.model.SearchModel;
import org.conventionsframework.qualifier.Log;
import org.conventionsframework.qualifier.PersistentClass;
import org.conventionsframework.qualifier.Service;
import org.conventionsframework.service.BaseService;
import org.hibernate.Criteria;
import org.hibernate.Session;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
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
    protected Crud<T> crud;

    @Inject
    protected EntityManager em;


    @Inject
    @Log
    private transient Logger log;


    @Inject
    public void initService(InjectionPoint ip) {
        if(crud.getPersistentClass() == null){
            crud.setPersistentClass(findPersistentClass(ip));
        }
        crud.setEntityManager(getEntityManager());//override crud entityManager to use same hibernate session
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void store(T entity) {
        doStore(entity);
    }

    public final void doStore(T entity) {
        this.beforeStore(entity);
        crud().saveOrUpdate(entity);
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
            entity = crud().load((Serializable) ((BaseEntity)entity).getId());
        }
        this.doRemove(entity);
    }


    @Override
    public void beforeRemove(T entity) {
    }

    public final void doRemove(T entity) {
        this.beforeRemove(entity);
        crud().delete(entity);
        flushSession();
        this.afterRemove(entity);
    }

    @Override
    public void afterRemove(T entity) {
    }


    public Criteria configPagination(SearchModel<T> searchModel) {
        return crud().configPagination(searchModel);
    }

    protected final Criteria configPagination(SearchModel<T> searchModel, Criteria criteria) {
        return crud().configPagination(searchModel,criteria);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public PaginationResult<T> executePagination(SearchModel<T> searchModel) {
        return crud().executePagination(searchModel, configPagination(searchModel));
    }


    @Override
    public Crud<T> crud() {
        return crud;
    }

    public void setCrud(Crud<T> crud) {
        this.crud = crud;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Criteria getCriteria() {
        return crud().getCriteria();
    }


    public void flushSession() {
        if (isConventionsFlushSession()) {
            Session session = crud().getSession();
            if (session.isOpen() && session.isDirty()) {
                session.flush();
            }
        }
    }


    public void setPersistentClass(Class<T> clazz) {
        crud().setPersistentClass(clazz);
    }


    /**
     * search persistentClass to set in crud layer
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
            log.log(Level.WARNING, "Conventions service: could not find persistent class for service:" + getClass().getSimpleName() + " it will be resolved to null.");
        }
        return null;
    }


    @Override
    public Class<T> getPersistentClass() {
        return crud.getPersistentClass();
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
