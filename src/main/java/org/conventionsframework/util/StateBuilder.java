package org.conventionsframework.util;

import org.conventionsframework.bean.state.State;
import org.conventionsframework.event.StatePushEvent;
import org.conventionsframework.model.AbstractBaseEntity;
import org.conventionsframework.model.StateItem;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author rafael-pestano 11/01/2014 10:06:21
 */
@Dependent
public class StateBuilder implements Serializable {

    @Inject
    private Event<StatePushEvent> statePushEvent;

    private static final long serialVersionUID = 1L;

    private StateBuilder INSTANCE;

    private Class<?> type;

    private String outcome = "";

    private String value;

    private String title = "";

    private AbstractBaseEntity entity;

    private State state;

    private boolean ajax = true;

    private String callback = "";

    private boolean global = true;

    private boolean immediate = true;

    private boolean resetValue = false;

    private boolean addEntityIdParam;

    private String oncomplete = "";

    private String update = "@none";

    private boolean facesRedirect = true;


    public StateBuilder(){

    }

    @Inject
    public StateBuilder(InjectionPoint ip){
        if(ip != null) {
           this.type = ip.getMember().getDeclaringClass();
            INSTANCE = this;
        }
    }

    public StateBuilder(Class<?> type) {
        this.type = type;
        INSTANCE = this;
    }

    public StateBuilder type (Class<?> type) {
        this.type = type;
        return INSTANCE;
    }

    public StateBuilder outcome(String outcome) {
        this.outcome = outcome;
        return INSTANCE;
    }

    public StateBuilder value(String name) {
        value = name;
        return INSTANCE;
    }

    public StateBuilder title(String title) {
        this.title = title;
        return INSTANCE;
    }

    public StateBuilder addFacesRedirect() {
        facesRedirect = true;
        return INSTANCE;
    }

    public StateBuilder entity(AbstractBaseEntity entity) {
        this.entity = entity;
        return INSTANCE;
    }

    public StateBuilder state(State beanState) {
        this.state = beanState;
        return INSTANCE;
    }

    public StateBuilder ajax() {
        ajax = true;
        return INSTANCE;
    }

    public StateBuilder callback(String callback){
        this.callback = callback;
        return INSTANCE;
    }

    public StateBuilder update(String update){
        this.update = update;
        return INSTANCE;
    }

    public StateBuilder global(){
        this.global = true;
        return INSTANCE;
    }

    public StateBuilder resetValues(){
        this.resetValue = true;
        return INSTANCE;
    }

    public StateBuilder immediate(){
        this.immediate = true;
        return INSTANCE;
    }

    public StateBuilder oncomplete(String oncomplete){
        this.oncomplete = oncomplete;
        return INSTANCE;
    }

    public StateBuilder addEntityIdParam(){
        this.addEntityIdParam = true;
        return INSTANCE;
    }


    public StateItem build() {
        if(type == null){
            throw new RuntimeException("cannot build StateItem without a type class");
        }

        if(value == null || "".equals(value.trim())){
            throw new RuntimeException("cannot build StateItem without a value");
        }

        if(state == null || "".equals(state.getStateName().trim())){
            throw new RuntimeException("cannot build StateItem without a state");
        }
        if (facesRedirect) {
            addFacesRedirect();
        }
        return new StateItem(outcome,entity, state,value,title,type,ajax,callback,update,global,resetValue,immediate, oncomplete,addEntityIdParam);
    }


    public void buildAndRegister(){
        statePushEvent.fire(new StatePushEvent(build()));
    }


    private void facesRedirect() {
        if (!outcome.contains("faces-redirect")) {
            if (!hasParam(outcome)) {
                outcome += "?faces-redirect=true";
            } else {
                outcome += "&faces-redirect=true";
            }
        }
    }

    private boolean hasParam(String url) {
        return url.contains("=");
    }
}
