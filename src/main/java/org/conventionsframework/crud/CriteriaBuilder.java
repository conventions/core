package org.conventionsframework.crud;

import org.conventionsframework.model.BaseEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

/**
 * Null safe injectable generic Hibernate criteria
 *
 * Created by rafael-pestano on 15/06/2014.
 */
@Dependent
public class CriteriaBuilder<T extends BaseEntity> {

    private Class<T> entityClass;

    private DetachedCriteria dc;

    @Inject
    public void init(InjectionPoint ip) {
        if (ip != null && ip.getType() != null) {
            ParameterizedType type = (ParameterizedType) ip.getType();
            Type[] typeArgs = type.getActualTypeArguments();
            entityClass = (Class<T>) typeArgs[0];
        } else {
            throw new IllegalArgumentException("Provide entity at injection point ex: @Inject CriteriaBuilder<Entity> criteriaBuilder");
        }

        resetCriteria();
    }

    public CriteriaBuilder eq(String property, Object value) {
        if (value != null) {
            dc.add(Restrictions.eq(property, value));
        }
        return this;
    }

    public CriteriaBuilder ne(String property, Object value) {
        if (value != null) {
            dc.add(Restrictions.ne(property, value));
        }
        return this;
    }

    public CriteriaBuilder not(Criterion criterion) {
        if (criterion != null) {
            dc.add(Restrictions.not(criterion));
        }
        return this;
    }

    public CriteriaBuilder ilike(String property, String value, MatchMode matchMode) {
        if (value != null) {
            dc.add(Restrictions.ilike(property, value.toString(), matchMode));
        }
        return this;
    }

    public CriteriaBuilder ilike(String property, Object value) {
        if (value != null) {
            dc.add(Restrictions.ilike(property, value));
        }
        return this;
    }

    public CriteriaBuilder like(String property, Object value) {
        if (value != null) {
            dc.add(Restrictions.like(property, value));
        }
        return this;
    }

    public CriteriaBuilder like(String property, String value, MatchMode matchMode) {
        if (value != null) {
            dc.add(Restrictions.like(property, value, matchMode));
        }
        return this;
    }

    public CriteriaBuilder ge(String property, Object value) {
        if (value != null) {
            dc.add(Restrictions.ge(property, value));
        }
        return this;
    }

    public CriteriaBuilder le(String property, Object value) {
        if (value != null) {
            dc.add(Restrictions.le(property, value));
        }
        return this;
    }

    public CriteriaBuilder between(String property, Calendar dtIni, Calendar dtEnd) {
        if (dtIni != null && dtEnd != null) {
            dtIni.set(Calendar.HOUR,0);
            dtIni.set(Calendar.MINUTE,0);
            dtIni.set(Calendar.SECOND,0);
            dtEnd.set(Calendar.HOUR,23);
            dtEnd.set(Calendar.MINUTE,59);
            dtEnd.set(Calendar.SECOND,59);
            dc.add(Restrictions.between(property, dtIni, dtEnd));
        }
        return this;
    }

    public CriteriaBuilder between(String property, Integer ini, Integer end) {
         if(ini != null && end != null){
             dc.add(Restrictions.between(property, ini, end));
         }
        return this;
    }

    public CriteriaBuilder in(String property, List<?> list) {
        if (list != null && !list.isEmpty()) {
            dc.add(Restrictions.in(property, list));
        }
        return this;
    }

    public CriteriaBuilder or(Criterion... criterios) {
        if (criterios != null) {
            dc.add(Restrictions.or(criterios));
        }
        return this;
    }

    public CriteriaBuilder or(Criterion lhs, Criterion rhs) {
        if (lhs != null && rhs != null) {
            dc.add(Restrictions.or(lhs, rhs));
        }
        return this;
    }

    public CriteriaBuilder and(Criterion... criterios) {
        if (criterios != null) {
            dc.add(Restrictions.and(criterios));
        }
        return this;
    }

    public CriteriaBuilder and(Criterion lhs, Criterion rhs) {
        if (lhs != null && rhs != null) {
            dc.add(Restrictions.and(lhs, rhs));
        }
        return this;
    }

    public CriteriaBuilder join(String property, String alias) {
        dc.createAlias(property, alias);
        return this;
    }

    public CriteriaBuilder join(String property, String alias, JoinType type) {
        dc.createAlias(property, alias, type);
        return this;
    }

    public CriteriaBuilder addCriterion(Criterion criterion) {
        if (criterion != null) {
            dc.add(criterion);
        }
        return this;
    }

    public CriteriaBuilder isNull(String property){
        if(property != null){
            dc.add(Restrictions.isNull(property));
        }
        return this;
    }

    public CriteriaBuilder isNotNull(String property){
        if(property != null){
            dc.add(Restrictions.isNotNull(property));
        }
        return this;
    }

    public CriteriaBuilder isEmpty(String property){
        if(property != null){
            dc.add(Restrictions.isEmpty(property));
        }
        return this;
    }

    public CriteriaBuilder isNotEmpty(String property){
        if(property != null){
            dc.add(Restrictions.isNotEmpty(property));
        }
        return this;
    }


    public CriteriaBuilder projection(Projection projection) {
        dc.setProjection(projection);
        return this;
    }

    public DetachedCriteria build() {
        DetachedCriteria detachedCriteria = dc;
        resetCriteria();
        return detachedCriteria;
    }

    public Criteria buildCriteria(Session session) {
        Criteria criteria = dc.getExecutableCriteria(session);
        resetCriteria();
        return criteria;
    }

    public void resetCriteria() {
        dc = DetachedCriteria.forClass(entityClass);
    }

    public void setCriteria(DetachedCriteria dc) {
        this.dc = dc;
    }
}
