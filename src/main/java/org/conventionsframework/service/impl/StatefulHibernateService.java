/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.conventionsframework.service.impl;

import org.conventionsframework.qualifier.Log;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.ejb.SessionSynchronization;
import javax.ejb.TransactionAttribute;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import org.conventionsframework.dao.BaseHibernateDao;
import org.conventionsframework.entitymanager.EntityManagerProvider;
import org.conventionsframework.qualifier.*;

/**
 *
 * Stateful EJB based service
 * @author rmpestano
 */
@Service(type= Type.STATEFUL)
@Named(value=Service.STATEFUL)
public class StatefulHibernateService<T,K extends Serializable> extends BaseServiceImpl<T, K> {

    @Inject @Log
    private transient Logger log;
    

    @Inject
    public void StatefulHibernateService(@ConventionsEntityManager(type= Type.STATEFUL) EntityManagerProvider entityManagerProvider,InjectionPoint ip) {
        try {
            getDao().setPersistentClass(this.findPersistentClass(ip));
            getDao().setEntityManagerProvider(entityManagerProvider);
        } catch (Exception ex) {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "Conventions:could not resolve persistent class for service:" + this.getClass().getSimpleName() + ", message:" + ex.getMessage());
            }
        }
    }

    public void afterBegin() throws EJBException, RemoteException {
        System.out.println("afterBegin()");
    }

    public void beforeCompletion() throws EJBException, RemoteException {
        System.out.println("beforeCompletion()");
    }

    public void afterCompletion(boolean committed) throws EJBException, RemoteException {
        System.out.println("afterCompletion()");
    }

    
    
    
     

}
