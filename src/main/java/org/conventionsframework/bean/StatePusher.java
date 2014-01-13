package org.conventionsframework.bean;

import org.conventionsframework.bean.state.State;
import org.conventionsframework.model.AbstractBaseEntity;
import org.conventionsframework.qualifier.Log;
import org.conventionsframework.util.BeanManagerController;
import org.conventionsframework.util.StateBuilder;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created by rmpestano on 1/12/14.
 */
@Named
@SessionScoped
public class StatePusher implements Serializable {

    @Inject
    StateBuilder stateBuilder;

    @Inject
    @Log
    transient Logger log;

    public void pushState(Boolean ignoreEvent, Boolean dinamic, Object bean, String callback, AbstractBaseEntity entity, boolean global, State state, String outcome, String value, String oncomplete, String update, String title, boolean ajax, boolean immediate, boolean resetValues, boolean addEntityIdParam) {
        if(ignoreEvent){
            return;
        }

        if(dinamic){
            StateBuilder builder = stateBuilder.type(bean.getClass()).callback(callback).entity(entity).state(state).value(value)
                    .oncomplete(oncomplete).update(update).outcome(outcome).title(title);
            if (global) {
                builder.global();
            }
            if (ajax) {
                builder.ajax();
            }
            if (immediate) {
                builder.immediate();
            }
            if (resetValues) {
                builder.resetValues();
            }
            if (addEntityIdParam) {
                builder.addEntityIdParam();
            }

            builder.buildAndRegister();
        }
        else{
            try{
                Object managedBean = BeanManagerController.getBeanByType(bean.getClass().getSuperclass());
                StateMBean stateMBean = (StateMBean)managedBean;
                stateMBean.firePushStateEvent(state);
            }catch (Exception e){
                log.warning("Could not push state on bean: "+bean + ", error:"+e.getMessage());
            }
        }
    }

}
