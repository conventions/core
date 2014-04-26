/*
 * Copyright 2011-2013 Conventions Framework.  
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");  
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at  
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0  
 *  
 * Unless required by applicable law or agreed to in writing, software  
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and  
 * limitations under the License.
 * 
 */
package org.conventionsframework.bean;

import org.conventionsframework.model.BaseEntity;
import org.conventionsframework.qualifier.*;
import org.conventionsframework.security.SecurityContext;
import org.conventionsframework.util.ResourceBundle;
import org.conventionsframework.util.BeanManagerController;
import org.conventionsframework.util.MessagesController;
import org.conventionsframework.util.AnnotationUtils;
import org.conventionsframework.event.ModalInitialization;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.conventionsframework.bean.state.CrudState;
import org.conventionsframework.bean.state.State;
import org.conventionsframework.producer.ResourceBundleProvider;
import org.conventionsframework.service.BaseService;
import java.lang.annotation.Annotation;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Instance;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.conventionsframework.event.ModalCallback;
import org.conventionsframework.paginator.Paginator;

/**
 * Base implementation of managedBeans
 *
 * @author Rafael M. Pestano Mar 17, 2011 10:18:44 PM
 */
public abstract class BaseMBean<T extends BaseEntity> implements Serializable {

    private T entity;
    private T entityAux;
    private T[] entityAuxList;
    private BaseService baseService;
    private State beanState;
    @Inject
    private Paginator<T> paginator;
    private Object modalResponse;
    private String createMessage;
    private String deleteMessage;
    private String updateMessage;
    @Inject
    private Event<ModalInitialization> modalInitEvent;
    @Inject
    private ResourceBundleProvider resourceBundleProvider;
    @Inject
    @Log
    protected transient Logger log;

    private boolean resetBean;//reset bean state via viewParam(resetBean=true/false)

    @Inject
    protected SecurityContext securityContext;

    @Inject
    @Config
    protected transient Instance<FacesContext> facesContext;

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

    public T[] getEntityAuxList() {
        return entityAuxList;
    }

    public void setEntityAuxList(T[] entityAuxList) {
        this.entityAuxList = entityAuxList;
    }

    public String getCreateMessage() {
        if(createMessage == null){
            createMessage = "Record created successfully";
        }
        return createMessage;
    }

    public void setCreateMessage(String createMessage) {
        this.createMessage = createMessage;
    }

    public String getDeleteMessage() {
        if(deleteMessage == null){
            deleteMessage = "Record deleted successfully";
        }
        return deleteMessage;
    }

    public void setDeleteMessage(String deleteMessage) {
        this.deleteMessage = deleteMessage;
    }

    public String getUpdateMessage() {
        if(updateMessage == null){
            updateMessage = "Record updated successfully";
        }
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    @PostConstruct
    public void init() {
        setEntity(this.create());
        if (initializeService()) { //baseService must be set to create paginator
            paginator.setBaseService(baseService);
        }
        else{
            log.warning("Service was not initialized for bean:"+getClass().getSimpleName());
        }
        setBeanState(getInitialState());
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
                log.log(Level.WARNING, "Could not create entity for mbean:" + this.getClass().getSimpleName());
                ex.printStackTrace();
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

    public Object getModalResponse() {
        return modalResponse;
    }

    public boolean isResetBean() {
        return resetBean;
    }

    public void setResetBean(boolean resetBean) {
        this.resetBean = resetBean;
    }

    //actions
    public void save() {
        baseService.store(entity);
        if (isInsertState()) {
            MessagesController.addInfo(getCreateMessage());
        } else {
            MessagesController.addInfo(getUpdateMessage());
        }
        setBeanState(CrudState.UPDATE);
    }

    public void save(String msg) {
        baseService.store(entity);
        MessagesController.addInfo(msg);
        setBeanState(CrudState.UPDATE);
    }

    /**
     * called by framework:removeButton
     * invoke service.remove using entityAux as param
     */
    public void delete() {
            this.delete(entityAux);
    }

    /**
     * invoke service.remove using entity as param
     * @param entity
     */
    public void delete(T entity) {
        this.delete(entity,deleteMessage);
    }

    public void delete(T entity, String msg) {
        baseService.remove(entity);
        MessagesController.addInfo(msg);
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
     * @return view id default is the calling outcome
     */
    public String afterPrepareInsert() {
        return null;
    }

    /**
     * @return view id default is the calling outcome
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
     * @param parameters parameters to send
     */
    public void initModal(Map<String, Object> parameters) {
        modalInitEvent.fire(new ModalInitialization(parameters));
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

    private boolean initializeService() {
        if (getBaseService() != null) {
            return true;
        }
        boolean initialized = false;
        Service service = AnnotationUtils.findServiceAnnotation(getClass());
        if (service != null) {
             if (!service.value().equals(BaseService.class)) {//inject service by value
                try {
                    this.setBaseService(BeanManagerController.getBeanByType(service.value()));
                    initialized = true;
                } catch (Exception ex) {
                    if (log.isLoggable(Level.WARNING)) {
                        log.log(Level.WARNING, "Conventions: managed bean:" + getClass().getSimpleName() + " service was not initialized. message:" + ex.getMessage());
                    }
                }
            }else if(!service.entity().isPrimitive()){//Managed bean without service, use generic service plus entity attr
                 baseService = BeanManagerController.getBeanByTypeAndQualifier(service.value(), Service.class);
                 baseService.getDao().setPersistentClass(service.entity());
                 initialized = true;
             }

            if (getBaseService() != null && getBaseService().getDao().getPersistentClass() == null) {
                if(!service.entity().isPrimitive()){
                    getBaseService().getDao().setPersistentClass(service.entity());
                }
                else{
                    throw new RuntimeException("Could not find persistent class, user @Service(entity=entity) or @PersistentClass at class level");
                }

            }
        }//end if service != null

        return initialized;
    }
    
    public void modalResponse(@Observes(notifyObserver = Reception.IF_EXISTS) ModalCallback callback) {
        modalResponse = callback.getResult();
        afterModalResponse();
    }

    public void afterModalResponse() {
    }

    public void resetBean(){
        log.info("resetBean:"+resetBean);
        log.info("beanState:"+getBeanState());
        log.info("postBack:"+facesContext.get().isPostback());
        if(!facesContext.get().isPostback() && resetBean && !getBeanState().equals(getInitialState())){
            log.info("resetBean2");
            this.init();
        }
    }

    /**
     * override to init bean with a different State
     * @return
     */
    public State getInitialState() {
        return CrudState.FIND;
    }
}