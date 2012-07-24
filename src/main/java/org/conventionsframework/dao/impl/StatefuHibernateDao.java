/**
 *
 */
package org.conventionsframework.dao.impl;

import java.io.Serializable;
import javax.inject.Named;
import org.conventionsframework.entitymanager.provider.EntityManagerProvider;
import org.conventionsframework.qualifier.ConventionsEntityManager;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.conventionsframework.qualifier.Dao;
import org.conventionsframework.qualifier.Type;


/**
 *
 * @author Rafael M. Pestano Apr 23, 2011 2:34:49 AM
 */
@Named
@Dependent
@Dao(type= Type.STATEFUL)
public class StatefuHibernateDao<T, Id extends Serializable> extends BaseDaoImpl<T, Id> {

    @Inject @ConventionsEntityManager(type= Type.STATEFUL)
    private EntityManagerProvider entityManagerProvider;
    
    @Override
    public EntityManagerProvider getEntityManagerProvider() {
        return entityManagerProvider;
    }

}
