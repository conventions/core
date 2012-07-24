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
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import org.conventionsframework.qualifier.*;

/**
 * Stateless EJB based service
 * 
 * @author rmpestano Dec 4, 2011 9:40:01 PM
 */
@Service(type= Type.STATEFUL)
@Named(value="statelessHibernateService")
public class StatelessHibernateService<T,K extends Serializable> extends BaseServiceImpl<T, K> {
    
    @Inject @Log
    private transient Logger log;
    
    
    public StatelessHibernateService() {
    }
    
    @Inject
    public void StatelessHibernateService(@Dao(type= Type.STATELESS) BaseDao<T, K> hibernateDao,InjectionPoint ip){
        try {
             hibernateDao.setPersistentClass(this.findPersistentClass(ip));
        } catch (Exception ex) {
            if(log.isLoggable(Level.WARNING)){
                log.log(Level.WARNING, "Conventions:could not resolve persistent class for service:" + this.getClass().getSimpleName() + ", message:"+ex.getMessage());
                
            }
        }
        super.setDao(hibernateDao);
    }

   
}

