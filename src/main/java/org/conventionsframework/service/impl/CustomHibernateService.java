/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventionsframework.service.impl;

import org.conventionsframework.qualifier.Log;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.extensions.cdi.jpa.api.Transactional;
import org.conventionsframework.dao.BaseHibernateDao;
import org.conventionsframework.entitymanager.EntityManagerProvider;
import org.conventionsframework.qualifier.*;

/**
 * Non JavaEE(EJB) dependent Service
 *
 * for this service work properly an EntityManagerProvider implementation should
 * be provided
 *
 * an example can be found here: {@link https://github.com/rmpestano/conventions-issuetracker-weld/blob/master/src/br/com/triadworks/issuetracker/entitymanager/provider/IssueTrackerProvider.java}
 * and here: {@link http://code.google.com/p/jsf-conventions-framework/wiki/services}
 *
 * @author Rafael M. Pestano Jun 12, 2012 7:18:29 PM
 */
@Dependent
@Service(type = Type.CUSTOM)
@Named(value = Service.CUSTOM)
public class CustomHibernateService<T, K extends Serializable> extends BaseServiceImpl<T, K> {

    public CustomHibernateService() {
    }
    
    @Inject
    @Log
    private transient Logger log;
    
    
    @Inject
    public void CustomHibernateService(@ConventionsEntityManager(type= Type.CUSTOM) EntityManagerProvider entityManagerProvider,InjectionPoint ip) {
        try {
            getDao().setPersistentClass(this.findPersistentClass(ip));
            getDao().setEntityManagerProvider(entityManagerProvider);
        } catch (Exception ex) {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "Conventions:could not resolve persistent class for service:" + this.getClass().getSimpleName() + ", message:" + ex.getMessage());
            }
        }
    }
    
     

    @Override
    @Transactional
    public void store(T entity) {
        super.store(entity);
    }

    @Override
    @Transactional
    public void remove(T entity) {
        super.remove(entity);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        super.delete(entity);
    }

    @Override
    @Transactional
    public void save(T entity) {
        super.save(entity);
    }

    @Override
    @Transactional
    public void saveOrUpdate(T entity) {
        super.saveOrUpdate(entity);
    }


    @Override
    @Transactional
    public void update(T entity) {
        super.update(entity);
    }
}