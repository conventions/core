/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.bean;

import com.jsf.conventions.bean.state.State;
import com.jsf.conventions.event.StatePullEvent;
import com.jsf.conventions.event.StatePushEvent;
import com.jsf.conventions.model.StateItem;
import com.jsf.conventions.qualifier.BeanState;
import com.jsf.conventions.qualifier.BeanStates;
import com.jsf.conventions.util.AnnotationUtils;
import java.io.Serializable;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author rmpestano Aug 13, 2011 10:50:42 AM
 *
 * Managed beans with the "power" of being tracked by the StatekController
 * @see StateController
 */
public abstract class StateMBean<T> extends BaseMBean<T> implements Serializable {

    @Inject
    private Event<StatePushEvent> statePushEvent;
    private BeanStates beanStates;

    /**
     * push this bean as an item in {@see StateController#stateItens}
     */
    public void firePushStateEvent() {
        if (beanStates != null) {//avoid unncessary looping
            this.matchState(beanStates.value());
            return;
        }
        BeanStates h = AnnotationUtils.findStatesAnnotation(getClass());
        if (h != null) {
            this.setStates(h);
            this.matchState(h.value());
            return;
        } else {//only a state annotation is present
            BeanState beanState = AnnotationUtils.findStateAnnotation(getClass());
            if (beanState != null) {
                if (beanState.beanState().equals(this.getBeanState().getStateName())) {
                    statePushEvent.fire(new StatePushEvent(new StateItem(beanState.page(), getEntity(), getBeanState(), beanState.title(), this.getClass(), beanState.ajax(), beanState.callback(), beanState.update())));
                }
            }
        }
    }

    /**
     * push this bean as an item in {@see StateController#stateItens}
     *
     */
    public void firePushStateEvent(State beanState) {
        /**
         * this method is fired by statePusher component and it is attached to
         * preRenderView event which is fired by every ajax call, so to avoid
         * this behavior we check if it is not a postback
         */
        if (!FacesContext.getCurrentInstance().isPostback() && beanState != null) {//non ajaxCall
            this.setBeanState(beanState);
            if (beanStates != null) {//avoid unncessary looping
                this.matchState(beanStates.value());
                return;
            }
            BeanStates states = AnnotationUtils.findStatesAnnotation(getClass());
            if (states != null) {
                this.setStates(states);
                this.matchState(states.value());
                return;

            } else {//only a state annotation is present
                BeanState state = AnnotationUtils.findStateAnnotation(getClass());
                if (states != null) {
                    if (state.beanState().equals(this.getBeanState().getStateName())) {
                        statePushEvent.fire(new StatePushEvent(new StateItem(state.page(), getEntity(), getBeanState(), state.title(), this.getClass(), state.ajax(), state.callback(), state.update())));
                    }
                }
            }
        }
    }

    @Override
    public String prepareInsert() {
        setInsertState();
        return super.prepareInsert();
    }

    @Override
    public String prepareUpdate() {
        setUpdateState();
        return super.prepareUpdate();
    }

    public void onStackPull(@Observes StatePullEvent stackPullEvent) {
        if (stackPullEvent.getStackItem().getInvokerClass().equals(this.getClass())) {
            T entity = (T) stackPullEvent.getStackItem().getEntity();
            this.setEntity(entity);
            super.setBeanState(stackPullEvent.getStackItem().getBeanState());
            this.afterItemPull();
        }
    }

    @Override
    public void resetEntity() {
        super.resetEntity();
        this.firePushStateEvent();
    }

    /**
     * override this method if you want to perform any action when a stackitem is
     * pulled
     *
     */
    public void afterItemPull() {
    }

    public BeanStates getBeanStates() {
        return beanStates;
    }

    public void setStates(BeanStates beanStates) {
        this.beanStates = beanStates;
    }

    private void matchState(BeanState[] value) {

        for (BeanState beanState : value) {
            if (beanState.beanState().equals(this.getBeanState().getStateName())) {
                statePushEvent.fire(new StatePushEvent(new StateItem(beanState.page(), getEntity(), getBeanState(), beanState.title(), this.getClass(), beanState.ajax(), beanState.callback(), beanState.update())));
            }
        }
    }

    /**
     * every time the state of the bean is changed
     * push the bean to the top of {@see StateController#stateItens}
     * @param beanState 
     */
    @Override
    public void setBeanState(State beanState) {
        if(!beanState.equals(this.getBeanState())){
            super.setBeanState(beanState);
            firePushStateEvent();
        }
    }

    
    
}
