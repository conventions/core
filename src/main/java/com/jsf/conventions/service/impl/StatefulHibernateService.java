/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jsf.conventions.service.impl;

import com.jsf.conventions.qualifier.StatefulService;
import com.jsf.conventions.dao.HibernateDao;
import com.jsf.conventions.qualifier.Log;
import com.jsf.conventions.qualifier.StatefulHibernateDao;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * Stateful EJB based service
 * @author rmpestano
 */
@StatefulService
@Named(value="statefulHibernateService")
public class StatefulHibernateService<T,K extends Serializable> extends BaseServiceImpl<T, K>{

    @Inject @Log
    private transient Logger log;
    
    public StatefulHibernateService() {
    }

    @Inject
    public void StatefulHibernateService(@StatefulHibernateDao HibernateDao<T, K> hibernateDao,InjectionPoint ip) {
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
