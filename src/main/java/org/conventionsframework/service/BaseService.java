/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventionsframework.service;

import org.conventionsframework.dao.BaseDao;
import java.io.Serializable;

/**
 *
 * @author Rafael M. Pestano Mar 19, 2011 9:13:05 AM
 */
public interface BaseService<T, Id extends Serializable> extends BaseDao<T, Id> {

    void store(T Entity);

    void afterStore(T entity);

    void beforeStore(T entity);

    void remove(T entity);

    void beforeRemove(T entity);

    void afterRemove(T entity);
    
    BaseDao getDao();
    
}
