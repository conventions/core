/**
 * 
 */
package com.jsf.conventions.dao;

import com.jsf.conventions.model.AbstractBaseEntity;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.jsf.conventions.model.WrappedData;
import javax.persistence.EntityManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.loader.custom.ScalarReturn;
import org.hibernate.transform.ResultTransformer;
import org.primefaces.model.SortOrder;

/**
 * @author rmpestano
 *
 */
public interface HibernateDao<T, Id extends Serializable> extends Serializable{

    T load(Id id);

    T get(Id id);

    void save(T entity);

    void update(T entity);

    void delete(T entity);

    T refresh(T entity);

    void saveOrUpdate(T entity);

    List<T> findAll();

    List<T> findAll(final Integer first, final Integer max);
     
    WrappedData<T> configFindPaginated(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters, Map<String, Object> externalFilter);

    int countAll();
    
    Class<T> getPersistentClass();
    
    void setPersistentClass(Class<T> persistentClass);
    
    Session getSession();

    List<T> findByExample(final T entity);
    
    List<T> findByExample(final T entity,int maxResult);
    
    List<T> findByExample(final T entity,MatchMode matchMode);
    
    List<T> findByExample(final T entity,int maxResult,MatchMode matchMode);

    T findOneByExample(final T entity);

    List<T> findByExample(T exampleInstance, String[] excludeProperty);

    T findOneByExample(final T entity, MatchMode matchMode);
    
    List<T> findByCriteria(DetachedCriteria criteriaObject, int first, int maxResult);

    List<T> findByCriteria(DetachedCriteria criteriaObject);
    
    WrappedData<T> findPaginated(final int first, final int pageSize, String sortField, SortOrder sortOrder, DetachedCriteria dc);
    
    Long getRowCount(final DetachedCriteria criteria);
    
    DetachedCriteria getDetachedCriteria();
    
    Criteria getCriteria();
    
    List findByNativeQuery(String nativeQuery,Map params,Class entity,ResultTransformer rt,ScalarReturn scalar);

    public EntityManager getEntityManager();
    
    
}
