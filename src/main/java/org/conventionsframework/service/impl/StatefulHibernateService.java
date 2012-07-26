/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.conventionsframework.service.impl;

import org.conventionsframework.dao.BaseDao;
import org.conventionsframework.qualifier.Log;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
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
 *
 * Stateful EJB based service
 * @author rmpestano
 */
@Stateful
@Service(type= Type.STATEFUL)
@Named(value=Service.STATEFUL)
@Dependent
public class StatefulHibernateService<T,K extends Serializable> extends BaseServiceImpl<T, K> implements SessionSynchronization {

    @Inject @Log
    private transient Logger log;
    
    @PersistenceContext(type= PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    
    @Inject
    private BaseDaoImpl<T, K> hibernateDao;
    
    public StatefulHibernateService() {
    }

    @Inject
    public void StatefulHibernateService(InjectionPoint ip) {
          try {
             hibernateDao.setPersistentClass(this.findPersistentClass(ip));
             hibernateDao.setEntityManager(entityManager);
        } catch (Exception ex) {
            if(log.isLoggable(Level.WARNING)){
                log.log(Level.WARNING, "Conventions:could not resolve persistent class for service:" + this.getClass().getSimpleName() + ", message:"+ex.getMessage());
            }
        }
         
        super.setDao(hibernateDao);
       
    }
    
    
    
    
     public void afterBegin() throws EJBException, RemoteException {
        System.out.println("====== afterBegin() ======");
    }

    public void beforeCompletion() throws EJBException, RemoteException {
         System.out.println("====== beforeCompletion() ======");
    }

    public void afterCompletion(boolean committed) throws EJBException, RemoteException {
       System.out.println("====== afterCompletion() ======");
    }

}
