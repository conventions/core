/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventionsframework.service.impl;

import org.conventionsframework.dao.BaseDao;
import org.conventionsframework.qualifier.Log;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.conventionsframework.dao.impl.BaseDaoImpl;
import org.conventionsframework.qualifier.*;

/**
 * Stateless EJB based service
 * 
 * @author rmpestano Dec 4, 2011 9:40:01 PM
 */
@Stateless
@Service(type= Type.STATELESS)
@Named(value=Service.STATELESS)
public class StatelessHibernateService<T,K extends Serializable> extends BaseServiceImpl<T, K> {
    
    @Inject @Log
    private transient Logger log;
    @PersistenceContext(type= PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;
    
    @Inject
    private BaseDaoImpl<T, K> hibernateDao;
    
    public StatelessHibernateService() {
    }
    
    @Inject
    public void StatelessHibernateService(InjectionPoint ip){
        try {
             hibernateDao.setPersistentClass(this.findPersistentClass(ip));
             hibernateDao.setEntityManager(entityManager);
        } catch (Exception ex) {
            if(log.isLoggable(Level.FINEST)){
                log.log(Level.WARNING, "Conventions:could not resolve persistent class for service:" + this.getClass().getSimpleName() + ", message:"+ex.getMessage());
                
            }
        }
        super.setDao(hibernateDao);
    }

   
}

