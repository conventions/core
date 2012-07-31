/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventionsframework.service.impl;

import org.conventionsframework.qualifier.Log;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import org.conventionsframework.dao.impl.BaseHibernateDaoImpl;
import org.conventionsframework.entitymanager.EntityManagerProvider;
import org.conventionsframework.qualifier.*;

/**
 * Stateless EJB based service
 * 
 * @author rmpestano Dec 4, 2011 9:40:01 PM
 */
@Service(type= Type.STATELESS)
@Named(value=Service.STATELESS)
public class StatelessHibernateService<T,K extends Serializable> extends BaseServiceImpl<T, K> {
    
    @Inject @Log
    private transient Logger log;
    
    @Inject @ConventionsEntityManager(type= Type.STATELESS)
    private EntityManagerProvider entityManagerProvider;
    
    public StatelessHibernateService() {
    }
    
    @Inject
    public void StatelessHibernateService(InjectionPoint ip){
        try {
             getDao().setPersistentClass(this.findPersistentClass(ip));
             getDao().setEntityManager(entityManagerProvider.getEntityManager());
        } catch (Exception ex) {
            if(log.isLoggable(Level.FINE)){
                log.log(Level.FINE, "Conventions:could not resolve persistent class for service:" + this.getClass().getSimpleName() + ", message:"+ex.getMessage());
                
            }
        }
    }

   
}

