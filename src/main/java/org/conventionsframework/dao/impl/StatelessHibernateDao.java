/**
 * 
 */
package org.conventionsframework.dao.impl;

import org.conventionsframework.dao.BaseDao;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import org.conventionsframework.model.WrappedData;
import org.conventionsframework.entitymanager.provider.EntityManagerProvider;
import org.conventionsframework.qualifier.ConventionsEntityManager;
import org.conventionsframework.qualifier.Log;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import org.conventionsframework.qualifier.*;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.loader.custom.ScalarReturn;
import org.hibernate.transform.ResultTransformer;
import org.primefaces.model.SortOrder;

/**
 * 
 * @author Rafael M. Pestano Apr 23, 2011 2:34:49 AM
 */
@Named
@Dependent
@Dao(type= Type.STATELESS)
public class StatelessHibernateDao<T, Id extends Serializable> extends BaseDaoImpl<T, Id> {

    @Inject @ConventionsEntityManager(type= Type.STATELESS)
    private EntityManagerProvider entityManagerProvider;
    
    @Override
    public EntityManagerProvider getEntityManagerProvider() {
        return entityManagerProvider;
    }
 
}
