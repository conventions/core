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
 * Non JavaEE(EJB) dependent Service
 * 
 * for this service work properly
 * an EntityManagerProvider implementation should be provided 
 *  
 * an example can be found here: {@link https://github.com/rmpestano/conventions-issuetracker-weld/blob/master/src/br/com/triadworks/issuetracker/entitymanager/provider/IssueTrackerProvider.java}
 * and here: {@link http://code.google.com/p/jsf-conventions-framework/wiki/services}
 * 
 * @author Rafael M. Pestano Jun 12, 2012 7:18:29 PM
 */
@Service(type= Type.CUSTOM)
@Named(value="customHibernateService")
public class CustomHibernateService<T, K extends Serializable> extends BaseCustomServiceImpl<T, K>  {

    @Inject @Log
    private transient Logger log;
    
    public CustomHibernateService() {
    }

    @Inject
    public void CustomHibernateService(@Dao(type= Type.CUSTOM) BaseDao<T,K> hibernateDao, InjectionPoint ip) {
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