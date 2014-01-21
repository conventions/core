/*
 * Copyright 2011-2013 Conventions Framework.  
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
package org.conventionsframework.bean;

import org.conventionsframework.event.StatePullEvent;
import org.conventionsframework.event.StatePushEvent;
import org.conventionsframework.model.StateItem;
import org.conventionsframework.producer.ResourceBundleProvider;
import org.conventionsframework.qualifier.Config;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DynamicMenuModel;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.conventionsframework.model.BaseEntity;

/**
 * @author rmpestano Aug 8, 2011 7:44:09 PM
 */

@Named("stateController")
@SessionScoped
public class StateController implements Serializable {
    private int STACK_SIZE;

    private LinkedList<StateItem> stateItens;
    @Inject
    private Event<StatePullEvent> statePullEvent;

    @Inject
    @Config
    transient Instance<FacesContext> context;


    @Inject
    private ResourceBundleProvider resourceBundleProvider;

    private DynamicMenuModel stateModel;

    public StateController() {
        stateItens = new LinkedList<StateItem>();
        String stackSize = FacesContext.getCurrentInstance()
                .getExternalContext().getInitParameter("STACK_SIZE");
        if (stackSize != null) {
            try {
                STACK_SIZE = Integer.parseInt(stackSize);
            } catch (Exception e) {
                STACK_SIZE = 6;
            }
        }
        buildStateModel();
    }

    public void onStatePush(@Observes StatePushEvent stackPushEvent) {
        if (stateItens.contains(stackPushEvent.getStateItem())) {
            stateItens.remove(stackPushEvent.getStateItem());
        }
        if (stateItens.size() == STACK_SIZE) {
            stateItens.removeFirst();
        }
        stateItens.add(stackPushEvent.getStateItem());
        buildStateModel();
    }

    /**
     * Remove all itens in 'front' of the clicked item
     *
     * @param itemIndex
     */
    public void pullStateItem(int itemIndex) {
        if (stateItens.isEmpty()) {
            return;
        }
        if (itemIndex != -1) {
            Iterator<StateItem> i = stateItens.iterator();
            while (i.hasNext()) {
                StateItem stateItem = i.next();
                if (stateItens.indexOf(stateItem) > itemIndex) {
                    i.remove();
                }
            }
        }
        StateItem item = stateItens.get(itemIndex);
        statePullEvent.fire(new StatePullEvent(item));
        buildStateModel();
        invokeCallback(item);
    }

    public void clearState() {
        stateItens.clear();
    }

    public void clearStateAndGoHome() {
        stateItens.clear();
        try {
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            FacesContext.getCurrentInstance()
                                    .getExternalContext()
                                    .getRequestContextPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getStateItensSize() {
        return stateItens.size();
    }

    /**
     * action called by backButton
     *
     * @return outcome to redirect
     */
    public String goBack() {
        stateItens.removeLast();
        if (stateItens.isEmpty()) {
            try {
                // if stateItens is empty go to index
                FacesContext
                        .getCurrentInstance()
                        .getExternalContext()
                        .redirect(
                                FacesContext.getCurrentInstance()
                                        .getExternalContext()
                                        .getRequestContextPath());
                return null;
            } catch (IOException ex) {
                Logger.getLogger(StateController.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }

        // update data in the managed bean
        StateItem item = stateItens.peekLast();
        statePullEvent.fire(new StatePullEvent(item));
        buildStateModel();
        invokeCallback(item);
        return item.getOutcome();
    }

    private void invokeCallback(StateItem item) {
        if (!"".equals(item.getCallback())) {
            Application application = FacesContext.getCurrentInstance()
                    .getApplication();
            ExpressionFactory expressionFactory = application
                    .getExpressionFactory();
            ELContext el = FacesContext.getCurrentInstance().getELContext();
            try {
                MethodExpression me = expressionFactory.createMethodExpression(
                        el, item.getCallback(), Void.class, new Class[]{});

                me.invoke(el, null);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pullStateItem(Long index) {
        this.pullStateItem(index.intValue());
    }

    public void pullStateItem() {
        FacesContext facesContext = context.get();
        if (facesContext != null && !facesContext.isPostback() && facesContext.getExternalContext().getRequestParameterMap().get("itemIndex") != null) {
            Integer itemIndex = Integer.valueOf(facesContext.getExternalContext().getRequestParameterMap().get("itemIndex"));
            this.pullStateItem(itemIndex);
        }
    }

    private void buildStateModel() {
        stateModel = new DynamicMenuModel();
        DefaultMenuItem homeItem = new DefaultMenuItem();
        homeItem.setAjax(false);
        homeItem.setValue("home");
        homeItem.setId(FacesContext.getCurrentInstance().getViewRoot()
                .createUniqueId()
                + "_state_home");
        homeItem.setImmediate(true);
        homeItem.setCommand("#{stateController.clearStateAndGoHome}");
        stateModel.addElement(homeItem);
        for (StateItem stateItem : stateItens) {
            DefaultMenuItem item = new DefaultMenuItem();
            item.setAjax(stateItem.isAjax());
            item.setGlobal(stateItem.isGlobal());
            item.setResetValues(stateItem.isResetValues());
            item.setTitle(stateItem.getTitle());
            item.setImmediate(stateItem.isImmediate());
            item.setValue(getItemValue(stateItem.getValue()));
            if (stateItem.getOncomplete() != null && !"".equals(stateItem.getOncomplete())) {
                item.setOncomplete(stateItem.getOncomplete());
            }
            item.setId(FacesContext.getCurrentInstance().getViewRoot()
                    .createUniqueId()
                    + "_state");
            if (stateItem.getOutcome() != null && !"".equals(stateItem.getOutcome())) {
                item.setIncludeViewParams(true);
                StringBuilder url = new StringBuilder(stateItem.getOutcome());
                if (stateItem.isAddEntityIdParam()) {
                    url.append("?id=").append(((BaseEntity) stateItem.getEntity()).getId());
                }
                if (url.toString().contains("?")) {
                    url.append("&pullState=true");
                } else {
                    url.append("?pullState=true");  //tell statePusher to not call preRenderView event
                }
                url.append("&itemIndex=").append(stateItens.indexOf(stateItem));
                item.setUrl(url.toString());
            } else {//if has not outcome set command, note that they are muttually exclusive: http://stackoverflow.com/questions/16437336/using-both-setactionexpression-and-seturl-on-menuitem-object-is-not-working
                item.setCommand("#{stateController.pullStateItem("
                        + stateItens.indexOf(stateItem) + ")}");
            }
            if (!"".equals(stateItem.getUpdate())) {
                item.setUpdate(stateItem.getUpdate());
            }

            if (stateItens.indexOf(stateItem) == stateItens.size() - 1) {
                item.setDisabled(true);
                item.setStyleClass("ui-state-disabled");
            }
            stateModel.addElement(item);

        }

    }

    public DynamicMenuModel getStateModel() {
        return stateModel;
    }

    public void setStateModel(DynamicMenuModel stateModel) {
        this.stateModel = stateModel;
    }

    public LinkedList<StateItem> getStateItens() {
        return stateItens;
    }

    public void setStateItens(LinkedList<StateItem> stateItems) {
        this.stateItens = stateItems;
    }


    private String getItemValue(String value) {
        if (resourceBundleProvider.getCurrentBundle() == null) {
            return value;
        }
        String i18nTitle = null;
        try {
            i18nTitle = resourceBundleProvider.getCurrentBundle().getString(
                    value);
        } catch (Exception re) {

        }
        if (i18nTitle != null) {
            return i18nTitle;
        }
        return value;
    }

}
