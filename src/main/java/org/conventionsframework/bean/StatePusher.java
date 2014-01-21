package org.conventionsframework.bean;

import org.conventionsframework.bean.state.State;
import org.conventionsframework.model.BaseEntity;
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

    public void pushState(boolean ignoreEvent, String dinamic, Object bean, String callback, BaseEntity entity, String global, State state, String outcome, String value, String oncomplete, String update, String title, String ajax, String immediate, String resetValues, String addEntityIdParam) {
        if(ignoreEvent){
            return;
        }

        if(Boolean.valueOf(dinamic)){
            StateBuilder builder = stateBuilder.type(bean.getClass()).callback(callback).entity(entity).state(state).value(value)
                    .oncomplete(oncomplete).update(update).outcome(outcome).title(title);
            if (Boolean.valueOf(global)) {
                builder.global();
            }
            if (Boolean.valueOf(ajax)) {
                builder.ajax();
            }
            if (Boolean.valueOf(immediate)) {
                builder.immediate();
            }
            if (Boolean.valueOf(resetValues)) {
                builder.resetValues();
            }
            if (Boolean.valueOf(addEntityIdParam)) {
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
