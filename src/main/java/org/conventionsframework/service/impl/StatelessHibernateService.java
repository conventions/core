/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventionsframework.service.impl;

import org.conventionsframework.dao.HibernateDao;
import org.conventionsframework.qualifier.Log;
import org.conventionsframework.qualifier.StatelessHibernateDao;
import org.conventionsframework.qualifier.StatelessService;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Stateless EJB based service
 * 
 * @author rmpestano Dec 4, 2011 9:40:01 PM
 */
@StatelessService
@Named(value="statelessHibernateService")
public class StatelessHibernateService<T,K extends Serializable> extends BaseServiceImpl<T, K> {
    
    @Inject @Log
    private transient Logger log;
    
    
    public StatelessHibernateService() {
    }
    
    @Inject
    public void StatelessHibernateService(@StatelessHibernateDao HibernateDao<T, K> hibernateDao,InjectionPoint ip){
        try {
             hibernateDao.setPersistentClass(this.findPersistentClass(ip));
        } catch (Exception ex) {
            if(log.isLoggable(Level.WARNING)){
                log.log(Level.WARNING, "Conventions:could not resolve persistent class for service:" + this.getClass().getSimpleName() + ", message:"+ex.getMessage());
                ex.printStackTrace();
                
            }
        }
        super.setDao(hibernateDao);
    }

   
}

