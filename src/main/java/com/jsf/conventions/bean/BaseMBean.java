/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.bean;

import com.jsf.conventions.event.ModalInitialization;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import com.jsf.conventions.bean.state.CrudState;
import com.jsf.conventions.bean.state.State;
import com.jsf.conventions.producer.ResourceBundleProvider;
import com.jsf.conventions.qualifier.Log;
import com.jsf.conventions.qualifier.PersistentClass;
import com.jsf.conventions.qualifier.Service;
import com.jsf.conventions.service.BaseService;
import com.jsf.conventions.util.*;
import java.lang.annotation.Annotation;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * Base implementation of managedBeans
 *
 * @author Rafael M. Pestano Mar 17, 2011 10:18:44 PM
 */
public abstract class BaseMBean<T> implements Serializable {

    private T entity;
    private T entityAux;
    private BaseService baseService;
    private State beanState;
    private Paginator paginator;
    private String createMessage;
    private String deleteMessage;
    private String updateMessage;
    @Inject
    private Event<ModalInitialization> modalInitEvent;
    @Inject
    private ResourceBundleProvider resourceBundleProvider;
    @Inject @Log
    private transient Logger log;

    // getter & setters
    public BaseService getBaseService() {
        return baseService;
    }

    //override in managedBean
    public void setBaseService(BaseService baseService) {
        this.baseService = baseService;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public T getEntityAux() {
        return entityAux;
    }

    public void setEntityAux(T entityAux) {
        this.entityAux = entityAux;
    }

    public State getBeanState() {
        return beanState;
    }

    public void setBeanState(State beanState) {
        this.beanState = beanState;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundleProvider.getCurrentBundle();
    }

    public ResourceBundleProvider getResourceBundleProvider() {
        return resourceBundleProvider;
    }

    public String getCreateMessage() {
        return createMessage;
    }

    public void setCreateMessage(String createMessage) {
        this.createMessage = createMessage;
    }

    public String getDeleteMessage() {
        return deleteMessage;
    }

    public void setDeleteMessage(String deleteMessage) {
        this.deleteMessage = deleteMessage;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    @PostConstruct
    public void init() {
        setEntity(this.create());
        setCreateMessage("Record created successfully");
        setUpdateMessage("Record updated successfully");
        setDeleteMessage("Record deleted successfully");
        if (serviceInitialized()) { //baseService must be set to create paginator
            paginator = new Paginator(baseService);
        }
    }

    public T create() {
        try {
            for (Annotation annotation : getClass().getAnnotations()) {
                if (annotation instanceof PersistentClass) {
                    PersistentClass p = (PersistentClass) annotation;
                    Class c = p.value();
                    return (T) c.newInstance();
                }
            }//if no annotation found then try to create via reflection
            return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
        } catch (Exception ex) {
            if(log.isLoggable(Level.WARNING)){
                log.log(Level.WARNING, "Could not create entity for mbean:"+this.getClass().getSimpleName());
                ex.printStackTrace();
            }
        }
        return null;
    }

    public boolean isInsertState() {
        return CrudState.INSERT.equals(this.beanState);
    }

    public boolean isUpdateState() {
        return CrudState.UPDATE.equals(this.beanState);
    }

    public boolean isFindState() {
        return CrudState.FIND.equals(this.beanState);
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    //actions
    public void store() {
        if (isInsertState()) {
            MessagesController.addInfo(getCreateMessage());
        } else {
            MessagesController.addInfo(getUpdateMessage());
        }
        baseService.store(entity);
        setBeanState(CrudState.UPDATE);
    }

    public void delete() {
        this.remove(entityAux);
        MessagesController.addInfo(deleteMessage);
    }

    public void remove(T entity) {
        baseService.remove(entity);
    }

    /**
     * called by editButton
     */
    public String prepareUpdate() {
        setBeanState(CrudState.UPDATE);
        return afterPrepareUpdate();
    }

    /**
     * called by addButton
     */
    public String prepareInsert() {
        setBeanState(CrudState.INSERT);
        setEntity(this.create());
        return afterPrepareInsert();
    }

    /**
     * @return view id default is the calling page
     */
    public String afterPrepareInsert() {
        return null;
    }

    /**
     * @return view id default is the calling page
     */
    public String afterPrepareUpdate() {
        return null;
    }

    /**
     *
     * called on framework:filterButton action
     */
    public void find() {
    }

    /**
     * called on framework:removeButton with persistentRemove = false action
     *
     */
    public void removeFromList() {
    }

    /**
     * reset entity state
     */
    public void resetEntity() {
        if (isUpdateState()) {
            setBeanState(CrudState.INSERT);
        }
        this.entity = this.create();
        clearMBean();
    }

    /**
     * method responsible for clearing fields referenced in the managed bean and
     * not in the entity
     *
     */
    public void clearMBean() {
    }

    /**
     * used to pass parameters to a modal
     *
     * @param modalName name of the modal which must receive the parameters
     * @param parameters parameters to send
     */
    public void initModal(String modalName, Map<String, Object> parameters) {
        modalInitEvent.fire(new ModalInitialization(parameters, modalName));
    }

    public void setFindState() {
        setBeanState(CrudState.FIND);
    }

    public void setInsertState() {
        setBeanState(CrudState.INSERT);
    }

    public void setUpdateState() {
        setBeanState(CrudState.UPDATE);
    }

    private boolean serviceInitialized() {
        if (getBaseService() != null) {
            return true;
        }
        Service service = AnnotationUtils.findServiceAnnotation(getClass());
        if (service != null && !"".equals(service.name())) {
            try {
                this.setBaseService((BaseService) BeanManagerController.getBeanByName(service.name()));
                if (!service.entity().isPrimitive() && getBaseService().getPersistentClass() == null || getBaseService().getPersistentClass().isPrimitive()) {
                    getBaseService().getDao().setPersistentClass(service.entity());
                }
                return true;
            } catch (Exception ex) {
                if(log.isLoggable(Level.WARNING)){
                    Logger.getLogger(getClass().getSimpleName()).log(Level.WARNING, "Conventions: managed bean:" + getClass().getSimpleName() + " service was not initialized. message:" + ex.getMessage());
                }
            }
        }
        return false;
    }
}