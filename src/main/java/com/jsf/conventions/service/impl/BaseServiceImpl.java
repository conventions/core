/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.service.impl;

import com.jsf.conventions.dao.HibernateDao;
import com.jsf.conventions.exception.BusinessException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import com.jsf.conventions.model.WrappedData;
import com.jsf.conventions.qualifier.PersistentClass;
import com.jsf.conventions.qualifier.StatefulService;
import com.jsf.conventions.qualifier.StatelessService;
import com.jsf.conventions.service.BaseService;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.loader.custom.ScalarReturn;
import org.hibernate.transform.ResultTransformer;
import org.primefaces.model.SortOrder;

/**
 * EJB base service
 *
 * @author Rafael M. Pestano Mar 19, 2011 11:55:37 AM
 */
public abstract class BaseServiceImpl<T, K extends Serializable> implements BaseService<T, K>, Serializable {

    protected HibernateDao<T,K> dao;
    
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void store(T entity) {
        try {
            doStore(entity);
        } catch (BusinessException be) {
            throw be;
        }

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void doStore(T entity) {
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
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void remove(T entity) {
        this.doRemove(entity);
    }

    @Override
    public void beforeRemove(T entity) {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void doRemove(T entity) {
        this.beforeRemove(entity);
        this.delete(entity);
        this.afterRemove(entity);
    }

    @Override
    public void afterRemove(T entity) {
    }

    @Override
    public WrappedData<T> configFindPaginated(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters, Map<String, Object> externalFilter) {
        return getDao().configFindPaginated(first, pageSize, sortField, sortOrder, filters, externalFilter);
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

    @Override
    public T refresh(T entity) {
        return (T) getDao().refresh(entity);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveOrUpdate(T entity) {
        getDao().saveOrUpdate(entity);
        flushSession();
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
    public void setPersistentClass(Class<T> persistentClass) {
        getDao().setPersistentClass(persistentClass);
    }

    @Override
    public Class<T> getPersistentClass() {
        return getDao().getPersistentClass();
    }

    @Override
    public HibernateDao<T,K> getDao() {
        return dao;
    }

    public void setDao(HibernateDao<T,K> dao) {
        this.dao = dao;
    }

    @Override
    public Session getSession() {
        return dao.getSession();
    }

    @Override
    public List<T> findByExample(T entity) {
        return dao.findByExample(entity);
    }

    @Override
    public List<T> findByExample(final T entity, MatchMode matchMode) {
        return dao.findByExample(entity, matchMode);
    }

    @Override
    public List<T> findByExample(final T entity, int maxResult) {
        return dao.findByExample(entity, maxResult);
    }

    @Override
    public List<T> findByExample(final T entity, int maxResult, MatchMode matchMode) {
        return dao.findByExample(entity, maxResult, matchMode);
    }

    @Override
    public T findOneByExample(T entity) {
        return (T) dao.findOneByExample(entity);
    }

    @Override
    public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
        return dao.findByExample(exampleInstance, excludeProperty);
    }

    @Override
    public T findOneByExample(T entity, MatchMode matchMode) {
        return (T) dao.findOneByExample(entity, matchMode);
    }

    @Override
    public List<T> findByCriteria(DetachedCriteria criteriaObject, int first, int maxResult) {
        return dao.findByCriteria(criteriaObject, first, maxResult);
    }

    @Override
    public List<T> findByCriteria(DetachedCriteria criteriaObject) {
        return dao.findByCriteria(criteriaObject);
    }

    @Override
    public WrappedData<T> findPaginated(int first, int pageSize, String sortField, SortOrder sortOrder, DetachedCriteria dc) {
        return dao.findPaginated(first, pageSize, sortField, sortOrder, dc);
    }

    @Override
    public Long getRowCount(DetachedCriteria criteria) {
        return dao.getRowCount(criteria);
    }

    @Override
    public DetachedCriteria getDetachedCriteria() {
        return DetachedCriteria.forClass(getPersistentClass());
    }

    @Override
    public Criteria getCriteria() {
        return dao.getCriteria();
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
        return dao.findByNativeQuery(nativeQuery, params, entity, rt, scalar);
    }

    public void flushSession() {
        Session session = getSession();
        if (session.isOpen() && session.isDirty()) {
            session.flush();
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return dao.getEntityManager();
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
        //inspect injection point
        if (ip != null && ip.getAnnotated() != null) {

            if (ip.getAnnotated().isAnnotationPresent(StatelessService.class)) {
                //try to get persistentClass from injectionPoint via @StatelessService(entity=SomeClass.class) annotation
                Class persistentClass = ip.getAnnotated().getAnnotation(StatelessService.class).entity();
                if (!persistentClass.isPrimitive()) {
                    return persistentClass;
                }
            }
            if (ip.getAnnotated().isAnnotationPresent(StatefulService.class)) {
                //try to get persistentClass from injectionPoint via @StatefulService(entity=SomeClass.class) annotation
                Class persistentClass = ip.getAnnotated().getAnnotation(StatefulService.class).entity();
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
            ex.printStackTrace();
            Logger.getLogger(getClass().getSimpleName()).log(Level.WARNING, "Conventions: could not find persistent class for service:" + getClass().getSimpleName() + " it will be resolved to null.");
        }
        return null;
    }
}
