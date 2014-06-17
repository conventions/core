package org.conventionsframework.crud;

/**
 * Created by rmpestano on 5/23/14.
 */

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.conventionsframework.model.BaseEntity;
import org.conventionsframework.model.PaginationResult;
import org.conventionsframework.model.SearchModel;
import org.conventionsframework.qualifier.Log;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.primefaces.model.SortOrder;


/**
 *
 * Crud helper
 *
 * @param <T> entity to crud
 * @author rafael-pestano
 */
@Dependent
public final class Crud<T extends BaseEntity> implements Serializable {

    @Inject
    private EntityManager entityManager;

    private Session session;

    private Class<T> persistentClass;

    private MatchMode matchMode;

    private Example example;

    private Integer maxResult;

    private Integer firstResult;

    private List<String> excludeProperties;

    private Projection projection;

    private Criteria criteria;

    @Inject
    @Log
    private Logger log;

    @Inject
    public void init(InjectionPoint ip) {
        if (ip != null && ip.getType() != null) {
            ParameterizedType type = (ParameterizedType) ip.getType();
            Type[] typeArgs = type.getActualTypeArguments();
            try{
                persistentClass = (Class<T>) typeArgs[0];
            }catch (ClassCastException e){
                //intentional, will receive classcast in BaseServiceImpl(CDI BUG)
            }
        } else {
            throw new IllegalArgumentException("provide entity at injection point ex: @Inject Crud<Entity> crud");
        }
    }

    //buider methods

    public Crud<T> matchMode(MatchMode matchMode) {
        this.matchMode = matchMode;
        return this;
    }

    public Crud<T> example(Example example) {
        this.example = example;
        return this;
    }

    public Crud<T> maxResult(Integer maxResult) {
        this.maxResult = maxResult;
        return this;
    }

    public Crud<T> firstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    public Crud<T> criteria(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public Crud<T> criteria() {
        this.criteria = getCriteria();
        return this;
    }

    public Crud<T> projection(Projection projection) {
        this.projection = projection;
        return this;
    }

    public Crud<T> excludeProperties(List<String> properties) {
        this.excludeProperties = excludeProperties;
        return this;
    }

    public Crud<T> excludeProperty(String property) {
        if (this.excludeProperties == null) {
            excludeProperties = new ArrayList<String>();
        }

        if (!excludeProperties.contains(property)) {
            excludeProperties.add(property);
        }
        return this;
    }


    //find methods

    /**
     * find entity
     *
     * @return
     */
    public T find() {
        return findByExample(null);
    }

    /**
     * find entity
     *
     * @param entity
     * @return
     */
    public T findByExample(final T entity) {
        if (criteria == null) {
            criteria = getSession().createCriteria(getPersistentClass());
        }
        if (entity != null && example == null) {
            example = Example.create(entity);
        }
        if (excludeProperties != null && !excludeProperties.isEmpty()) {
            for (String exclude : excludeProperties) {
                example.excludeProperty(exclude);
            }
        }
        if (matchMode == null) {
            matchMode = MatchMode.ANYWHERE;
        }

        if (projection != null) {
            criteria.setProjection(projection);
        }
        if (example != null) {
            criteria.add(example.enableLike(matchMode).ignoreCase());
        }
        T result = (T) criteria.uniqueResult();
        resetCriteria();
        return result;
    }

    //list methods

    public List<T> list() {
        return listByExample(null);
    }

    public List<T> listAll() {
        return getCriteria().list();
    }


    /**
     * find list by example
     *
     * @param entity
     * @return
     */
    public List<T> listByExample(T entity) {
        if (criteria == null) {
            criteria = getSession().createCriteria(getPersistentClass());
        }
        if (entity != null && example == null) {
            example = Example.create(example);
        }
        if (excludeProperties != null && !excludeProperties.isEmpty()) {
            for (String exclude : excludeProperties) {
                example.excludeProperty(exclude);
            }
        }
        if (matchMode == null) {
            matchMode = MatchMode.ANYWHERE;
        }

        if (firstResult != null) {
            criteria.setFirstResult(firstResult);
        }

        if (maxResult != null) {
            criteria.setMaxResults(maxResult);
        }
        if (projection != null) {
            criteria.setProjection(projection);
        }
        if (example != null) {
            criteria.add(example.enableLike(matchMode).ignoreCase());
        }
        List<T> result = criteria.list();
        resetCriteria();
        return result;
    }

    //count methods

    public int count() {
        return countByExample(null);
    }

    /**
     * count by example
     *
     * @param entity, the example
     * @return
     */
    public int countByExample(T entity) {
        if (criteria == null) {
            criteria = getSession().createCriteria(getPersistentClass());
        }
        if (entity != null && example == null) {
            example = Example.create(entity);
        }
        if (excludeProperties != null && !excludeProperties.isEmpty()) {
            for (String exclude : excludeProperties) {
                example.excludeProperty(exclude);
            }
        }
        if (matchMode == null) {
            matchMode = MatchMode.ANYWHERE;
        }

        if (projection != null) {
            criteria.setProjection(projection);
        }
        if (example != null) {
            criteria.add(example.enableLike(matchMode).ignoreCase());
        }
        criteria.setProjection(Projections.count(getSession().getSessionFactory().getClassMetadata(persistentClass).getIdentifierPropertyName()));
        Long result = (Long) criteria.uniqueResult();
        resetCriteria();
        return result.intValue();

    }


    public int countAll() {
        return projection(Projections.rowCount()).criteria(
                getCriteria().setFirstResult(0).setMaxResults(1)
        ).count();
    }


    //pagination

    public Criteria configPagination(SearchModel<T> searchModel) {
        Criteria criteria = getCriteria();
        addBasicFilterRestrictions(criteria, searchModel.getFilter());
        addBasicFilterRestrictions(criteria, searchModel.getDatatableFilter());
        if(searchModel.getEntity() != null){
            Example example = Example.create(searchModel.getEntity()).enableLike(MatchMode.ANYWHERE).ignoreCase();
            criteria.add(example);
        }
        return criteria;
    }

    public Criteria configPagination(SearchModel<T> searchModel, Criteria criteria) {
        addBasicFilterRestrictions(criteria, searchModel.getFilter());
        addBasicFilterRestrictions(criteria, searchModel.getDatatableFilter());
        if(searchModel.getEntity() != null){
            Example example = Example.create(searchModel.getEntity()).enableLike(MatchMode.ANYWHERE).ignoreCase();
            criteria.add(example);
        }
        return criteria;
    }

    public PaginationResult<T> executePagination(SearchModel<T> searchModel, final Criteria criteria) {

        int size = criteria(criteria).projection(Projections.rowCount()).firstResult(0)
                .maxResult(1).count();
        criteria.setProjection(null).setResultTransformer(Criteria.ROOT_ENTITY);
        String sortField = searchModel.getSortField();
        if (sortField != null) {
            if(searchModel.getSortOrder().equals(SortOrder.UNSORTED)){
                searchModel.setSortOrder(SortOrder.ASCENDING);
            }
            if (searchModel.getSortOrder().equals(SortOrder.ASCENDING)) {
                criteria.addOrder(Order.asc(sortField));
            } else {
                criteria.addOrder(Order.desc(sortField));
            }
            criteria.addOrder(Order.asc(getSession().getSessionFactory().getClassMetadata(getPersistentClass()).getIdentifierPropertyName()));

        }

        List<T> data = criteria(criteria).firstResult(searchModel.getFirst()).maxResult(searchModel.getPageSize()).list();


        return new PaginationResult<T>(data, size);
    }


    //hibernate session shortcuts

    public T load(Serializable id) {
        return (T) this.getSession().load(persistentClass, id);
    }

    public T get(Serializable id) {
        return (T) this.getSession().get(persistentClass, id);
    }

    public void save(T entity) {
        this.getSession().save(entity);
    }

    public T merge(T entity) {
        return (T) getSession().merge(entity);
    }

    public void update(T entity) {
        this.getSession().update(entity);
    }

    public void delete(T entity) {
        this.getSession().delete(this.get((Serializable) entity.getId()));
    }

    public T refresh(T entity) {
        this.getSession().refresh(entity);
        return entity;
    }

    public void saveOrUpdate(T entity) {
        this.getSession().saveOrUpdate(entity);
    }


    private void resetCriteria() {
        criteria = null;
        matchMode = null;
        maxResult = null;
        example = null;
        firstResult = null;
        projection = null;

    }

    // Getters & Setters

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Session getSession() {
        if (session == null || !session.isOpen()) {
            session = getEntityManager().unwrap(Session.class);
        }
        return session;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public void setPersistentClass(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public Criteria getCriteria() {
        if (criteria == null) {
            criteria = getSession().createCriteria(getPersistentClass());
        }
        return criteria;
    }


    //general utilities

    /**
     * try to infer the restrictions based on the entity being paged, will work
     * only for basic field for more complex restriction such as relationships
     * override configPagination
     *
     * the method will add a hibernate <b>ilike</b> for string fields and
     * <b>eq<b/> for int/Integer/Long/long/Date/Calendar fields
     *
     * @param criteria
     * @param externalFilter
     */
    public void addBasicFilterRestrictions(Criteria criteria, Map externalFilter) {
        if (criteria != null && externalFilter != null && !externalFilter.isEmpty()) {
            for (Iterator<Map.Entry<String, Object>> it = (Iterator<Map.Entry<String, Object>>) externalFilter.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, Object> entry = it.next();
                if (entry != null && entry.getValue() != null && !"".equals(entry.getValue())) {
                    Field f;
                    try {
                        f = getField(persistentClass, entry.getKey());
                        if (f.getType().isAssignableFrom(String.class)) {
                            criteria.add(Restrictions.ilike(entry.getKey(), (String) entry.getValue(), MatchMode.ANYWHERE));
                        } else if (f.getType().isAssignableFrom(Integer.class) || f.getType().isAssignableFrom(int.class)) {
                            criteria.add(Restrictions.eq(entry.getKey(), Integer.parseInt((String) entry.getValue())));
                        } else if (f.getType().isAssignableFrom(Long.class) || f.getType().isAssignableFrom(long.class)) {
                            criteria.add(Restrictions.eq(entry.getKey(), Long.parseLong((String) entry.getValue())));
                        } else if (f.getType().isAssignableFrom(Date.class)) {
                            criteria.add(Restrictions.eq(entry.getKey(), (Date) entry.getValue()));
                        } else if (f.getType().isAssignableFrom(Calendar.class)) {
                            criteria.add(Restrictions.eq(entry.getKey(), (Calendar) entry.getValue()));
                        }
                    } catch (NoSuchFieldException ex) {
                          log.warning("Could not addBasicRestriction from field:" + entry.getKey() + "." + ex.getMessage());
                    } catch (SecurityException ex) {
                    }
                }
            }
        }


    }

    private Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }
}