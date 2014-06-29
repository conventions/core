package org.conventionsframework.crud;

/**
 * Created by rmpestano on 5/23/14.
 */

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.conventionsframework.model.BaseEntity;
import org.conventionsframework.model.PaginationResult;
import org.conventionsframework.model.SearchModel;
import org.conventionsframework.qualifier.Log;
import org.conventionsframework.util.Assert;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.primefaces.model.SortOrder;

/**
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

    private Criteria criteria;

    @Inject
    @Log
    private Logger log;

    @Inject
    public void init(InjectionPoint ip) {
        if (ip != null && ip.getType() != null) {
            ParameterizedType type = (ParameterizedType) ip.getType();
            Type[] typeArgs = type.getActualTypeArguments();
            try {
                persistentClass = (Class<T>) typeArgs[0];
            } catch (ClassCastException e) {
                // intentional, will receive classcast in BaseServiceImpl(CDI
                // BUG)
            }
        } else {
            throw new IllegalArgumentException(
                    "Provide entity at injection point ex: @Inject Crud<Entity> crud");
        }
    }

    // buider methods


    public Crud<T> example(T entity) {
        if (entity != null) {
            getCriteria().add(Example.create(entity));
        } else {
            log.warning("cannot create example for a null entity.");
            return this;
        }
        return this;
    }

    public Crud<T> example(T entity, List<String> excludeProperties) {
        Example example = null;
        if (entity != null) {
            example = Example.create(entity);
        } else {
            log.warning("cannot create example for a null entity.");
            return this;
        }
        if (Assert.hasElements(excludeProperties)) {
            for (String exclude : excludeProperties) {
                example.excludeProperty(exclude);
            }
        }
        getCriteria().add(example);
        return this;
    }

    public Crud<T> example(T entity, MatchMode mode) {
        if (entity != null) {
            getCriteria().add(Example.create(entity).enableLike(mode));
        } else {
            log.warning("cannot create example for a null entity.");
            return this;
        }
        return this;
    }

    public Crud<T> example(T entity, MatchMode mode, List<String> excludeProperties) {
        Example example = null;
        if (entity != null) {
            example = Example.create(entity).enableLike(mode);
        } else {
            log.warning("cannot create example for a null entity.");
            return this;
        }
        if (Assert.hasElements(excludeProperties)) {
            for (String exclude : excludeProperties) {
                example.excludeProperty(exclude);
            }
        }
        getCriteria().add(example);
        return this;
    }

    public Crud<T> maxResult(Integer maxResult) {
        getCriteria().setMaxResults(maxResult);
        return this;
    }

    public Crud<T> firstResult(Integer firstResult) {
        getCriteria().setFirstResult(firstResult);
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
        getCriteria().setProjection(projection);
        return this;
    }

    //nullsafe restrictions

    public Crud eq(String property, Object value) {
        if (value != null) {
            getCriteria().add(Restrictions.eq(property, value));
        }
        return this;
    }

    public Crud ne(String property, Object value) {
        if (value != null) {
            getCriteria().add(Restrictions.ne(property, value));
        }
        return this;
    }

    public Crud not(Criterion criterion) {
        if (criterion != null) {
            getCriteria().add(Restrictions.not(criterion));
        }
        return this;
    }

    public Crud ilike(String property, String value, MatchMode matchMode) {
        if (value != null) {
            getCriteria().add(Restrictions.ilike(property, value.toString(), matchMode));
        }
        return this;
    }

    public Crud ilike(String property, Object value) {
        if (value != null) {
            getCriteria().add(Restrictions.ilike(property, value));
        }
        return this;
    }

    public Crud like(String property, Object value) {
        if (value != null) {
            getCriteria().add(Restrictions.like(property, value));
        }
        return this;
    }

    public Crud like(String property, String value, MatchMode matchMode) {
        if (value != null) {
            getCriteria().add(Restrictions.like(property, value, matchMode));
        }
        return this;
    }

    public Crud ge(String property, Object value) {
        if (value != null) {
            getCriteria().add(Restrictions.ge(property, value));
        }
        return this;
    }

    public Crud le(String property, Object value) {
        if (value != null) {
            getCriteria().add(Restrictions.le(property, value));
        }
        return this;
    }

    public Crud between(String property, Calendar dtIni, Calendar dtEnd) {
        if (dtIni != null && dtEnd != null) {
            dtIni.set(Calendar.HOUR, 0);
            dtIni.set(Calendar.MINUTE, 0);
            dtIni.set(Calendar.SECOND, 0);
            dtEnd.set(Calendar.HOUR, 23);
            dtEnd.set(Calendar.MINUTE, 59);
            dtEnd.set(Calendar.SECOND, 59);
            getCriteria().add(Restrictions.between(property, dtIni, dtEnd));
        }
        return this;
    }

    public Crud between(String property, Integer ini, Integer end) {
        if (ini != null && end != null) {
            getCriteria().add(Restrictions.between(property, ini, end));
        }
        return this;
    }

    public Crud in(String property, List<?> list) {
        if (Assert.hasElements(list)) {
            getCriteria().add(Restrictions.in(property, list));
        }
        return this;
    }

    public Crud or(Criterion... criterions) {
        if (criterions != null) {
            getCriteria().add(Restrictions.or(criterions));
        }
        return this;
    }

    public Crud or(Criterion lhs, Criterion rhs) {
        if (lhs != null && rhs != null) {
            getCriteria().add(Restrictions.or(lhs, rhs));
        }
        return this;
    }

    public Crud and(Criterion... criterios) {
        if (criterios != null) {
            getCriteria().add(Restrictions.and(criterios));
        }
        return this;
    }

    public Crud and(Criterion lhs, Criterion rhs) {
        if (lhs != null && rhs != null) {
            getCriteria().add(Restrictions.and(lhs, rhs));
        }
        return this;
    }

    public Crud join(String property, String alias) {
        getCriteria().createAlias(property, alias);
        return this;
    }

    public Crud join(String property, String alias, JoinType type) {
        getCriteria().createAlias(property, alias, type);
        return this;
    }

    public Crud addCriterion(Criterion criterion) {
        if (criterion != null) {
            getCriteria().add(criterion);
        }
        return this;
    }

    public Crud isNull(String property) {
        if (property != null) {
            getCriteria().add(Restrictions.isNull(property));
        }
        return this;
    }

    public Crud isNotNull(String property) {
        if (property != null) {
            getCriteria().add(Restrictions.isNotNull(property));
        }
        return this;
    }

    public Crud isEmpty(String property) {
        if (property != null) {
            getCriteria().add(Restrictions.isEmpty(property));
        }
        return this;
    }

    public Crud isNotEmpty(String property) {
        if (property != null) {
            getCriteria().add(Restrictions.isNotEmpty(property));
        }
        return this;
    }

    // find


    public T find() {
        T result = (T) getCriteria().uniqueResult();
        resetCriteria();
        return result;
    }

    // list

    public List<T> list() {
        List<T> result = getCriteria().list();
        resetCriteria();
        return result;
    }

    public List<T> listAll() {
        resetCriteria();
        List<T> result = getCriteria().list();
        return result;
    }

    // count

    public int count() {
        getCriteria().setProjection(Projections.count(getSession()
                .getSessionFactory().getClassMetadata(persistentClass)
                .getIdentifierPropertyName()));
        Long result = (Long) getCriteria().uniqueResult();
        resetCriteria();
        return result.intValue();
    }

    public int countAll() {
        int result = projection(Projections.rowCount()).firstResult(0)
                .maxResult(1).count();
        resetCriteria();
        return result;
    }

    // pagination

    public Criteria configPagination(SearchModel<T> searchModel) {
        resetCriteria();
        addBasicFilterRestrictions(getCriteria(), searchModel.getFilter());
        addBasicFilterRestrictions(getCriteria(), searchModel.getDatatableFilter());
        if (searchModel.getEntity() != null) {
            Example example = Example.create(searchModel.getEntity())
                    .enableLike(MatchMode.ANYWHERE).ignoreCase();
            getCriteria().add(example);
        }
        return getCriteria();
    }

    public Criteria configPagination(SearchModel<T> searchModel,
                                     Criteria criteria) {
        addBasicFilterRestrictions(criteria, searchModel.getFilter());
        addBasicFilterRestrictions(criteria, searchModel.getDatatableFilter());
        if (searchModel.getEntity() != null) {
            Example example = Example.create(searchModel.getEntity())
                    .enableLike(MatchMode.ANYWHERE).ignoreCase();
            criteria.add(example);
        }
        return criteria;
    }

    public PaginationResult<T> executePagination(SearchModel<T> searchModel,
                                                 final Criteria criteria) {

        int size = criteria(criteria).projection(Projections.rowCount())
                .firstResult(0).maxResult(1).count();
        criteria.setProjection(null).setResultTransformer(Criteria.ROOT_ENTITY);
        String sortField = searchModel.getSortField();
        if (sortField != null) {
            if (searchModel.getSortOrder().equals(SortOrder.UNSORTED)) {
                searchModel.setSortOrder(SortOrder.ASCENDING);
            }
            if (searchModel.getSortOrder().equals(SortOrder.ASCENDING)) {
                criteria.addOrder(Order.asc(sortField));
            } else {
                criteria.addOrder(Order.desc(sortField));
            }
            criteria.addOrder(Order.asc(getSession().getSessionFactory()
                    .getClassMetadata(getPersistentClass())
                    .getIdentifierPropertyName()));

        }

        List<T> data = criteria(criteria).firstResult(searchModel.getFirst())
                .maxResult(searchModel.getPageSize()).list();

        return new PaginationResult<T>(data, size);
    }

    // hibernate session shortcuts

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

    public Criteria getCriteria(boolean reset) {
        Criteria copy = getCriteria();
        criteria = null;
        return copy;
    }


    // general utilities

    /**
     * try to infer the restrictions based on the entity being paged, will work
     * only for basic field for more complex restriction such as relationships
     * override configPagination
     * <p/>
     * the method will add a hibernate <b>ilike</b> for string fields and
     * <b>eq<b/> for int/Integer/Long/long/Date/Calendar fields
     *
     * @param criteria
     * @param externalFilter
     */
    public void addBasicFilterRestrictions(Criteria criteria, Map externalFilter) {
        if (criteria != null && externalFilter != null
                && !externalFilter.isEmpty()) {
            for (Iterator<Map.Entry<String, Object>> it = (Iterator<Map.Entry<String, Object>>) externalFilter
                    .entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> entry = it.next();
                if (entry != null && entry.getValue() != null
                        && !"".equals(entry.getValue())) {
                    Field f;
                    try {
                        f = getField(persistentClass, entry.getKey());
                        if (f.getType().isAssignableFrom(String.class)) {
                            criteria.add(Restrictions.ilike(entry.getKey(),
                                    (String) entry.getValue(),
                                    MatchMode.ANYWHERE));
                        } else if (f.getType().isAssignableFrom(Integer.class)
                                || f.getType().isAssignableFrom(int.class)) {
                            criteria.add(Restrictions.eq(entry.getKey(),
                                    Integer.parseInt((String) entry.getValue())));
                        } else if (f.getType().isAssignableFrom(Long.class)
                                || f.getType().isAssignableFrom(long.class)) {
                            criteria.add(Restrictions.eq(entry.getKey(),
                                    Long.parseLong((String) entry.getValue())));
                        } else if (f.getType().isAssignableFrom(Date.class)) {
                            criteria.add(Restrictions.eq(entry.getKey(),
                                    (Date) entry.getValue()));
                        } else if (f.getType().isAssignableFrom(Calendar.class)) {
                            criteria.add(Restrictions.eq(entry.getKey(),
                                    (Calendar) entry.getValue()));
                        }
                    } catch (NoSuchFieldException ex) {
                        log.warning("Could not addBasicRestriction from field:"
                                + entry.getKey() + "." + ex.getMessage());
                    } catch (SecurityException ex) {
                    }
                }
            }
        }

    }

    private Field getField(Class clazz, String fieldName)
            throws NoSuchFieldException {
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