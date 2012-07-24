/**
 *
 */
package org.conventionsframework.dao.impl;

import org.conventionsframework.qualifier.ConventionsEntityManager;
import java.io.Serializable;
import javax.inject.Named;
import org.conventionsframework.entitymanager.provider.EntityManagerProvider;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.conventionsframework.qualifier.Dao;
import org.conventionsframework.qualifier.Type;


/**
 *
 * Non JavaEE dependent Dao
 * 
 * for this service work properly
 * an EntityManagerProvider implementation should be provided 
 *  
 * an example can be found here: {@link https://github.com/rmpestano/conventions-issuetracker-weld/blob/master/src/br/com/triadworks/issuetracker/entitymanager/provider/IssueTrackerProvider.java}
 * and here: {@link http://code.google.com/p/jsf-conventions-framework/wiki/services}
 * 
 * @author Rafael M. Pestano Jun 12, 2012 7:13:49 PM
 * 
 */
@Named
@Dependent
@Dao(type= Type.CUSTOM)
public class CustomHibernateDao<T, Id extends Serializable> extends BaseDaoImpl<T, Id> {

    @Inject @ConventionsEntityManager(type= Type.CUSTOM)
    private EntityManagerProvider entityManagerProvider;
    
    @Override
    public EntityManagerProvider getEntityManagerProvider() {
        return entityManagerProvider;
    }

}
