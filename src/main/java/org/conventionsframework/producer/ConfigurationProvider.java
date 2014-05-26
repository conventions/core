/*
 * Copyright 2011-2014 Conventions Framework.
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
package org.conventionsframework.producer;

import org.conventionsframework.qualifier.Config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rafaem M. Pestano - Dez 27, 2013 7:03:43 PM
 *
 * gather app config
 * to get a map entry use dependency injection where map key is injectionPoint member
 * ex:
 * @inject
 * @Config
 * String myConfig; //will call (String)configMap.get("myConfig");
 */
@ApplicationScoped
public class ConfigurationProvider implements Serializable {

    private Map<String, Object> configMap = new HashMap<String, Object>();


    protected @Produces @Config
    String produceStringEntry(InjectionPoint ip) {

        String key = ip.getMember().getName();
        if (configMap.containsKey(key)) {
            return (String) configMap.get(key);
        }
        return null;
    }


    protected @Produces @Config
    Boolean produceBooleanEntry(InjectionPoint ip) {

        String key = ip.getMember().getName();
        if (configMap.containsKey(key)) {
            return (Boolean) configMap.get(key);
        }
        return null;
    }


    protected @Produces @Config
    Integer produceIntegerEntry(InjectionPoint ip) {

        String key = ip.getMember().getName();
        if (configMap.containsKey(key)) {
            return (Integer) configMap.get(key);
        }
        return null;
    }

    protected @Produces @Config
    Calendar produceCalendarEntry(InjectionPoint ip) {
        String key = ip.getMember().getName();
        if (configMap.containsKey(key)) {
            return (Calendar) configMap.get(key);
        }
        return null;
    }

    protected @Produces @Config
    Date produceDateEntry(InjectionPoint ip) {
        String key = ip.getMember().getName();
        if (configMap.containsKey(key)) {
            return (Date) configMap.get(key);
        }
        return null;
    }


    protected @Produces @Config
    Long produceLongEntry(InjectionPoint ip) {

        String key = ip.getMember().getName();
        if (configMap.containsKey(key)) {
            return (Long) configMap.get(key);
        }
        return null;
    }


    protected  @Produces @Config
    Double produceDoubleEntry(InjectionPoint ip) {

        String key = ip.getMember().getName();
        if (configMap.containsKey(key)) {
            return (Double) configMap.get(key);
        }
        return null;
    }


    protected @Produces @Config FacesContext produceFacesContext(){
          return FacesContext.getCurrentInstance();
    }

    protected @Produces @Config
    ExternalContext produceExternalContext(){
        return produceFacesContext() != null ? produceFacesContext().getExternalContext() : null;
    }

    protected @Produces @Config
    HttpServletRequest produceServletRequest(){
        return produceFacesContext().getExternalContext() != null ? (HttpServletRequest) produceExternalContext().getRequest() : null;
    }

    protected @Produces @Config
    HttpServletResponse produceServletResponse(){
        return produceFacesContext().getExternalContext() != null ? (HttpServletResponse) produceExternalContext().getResponse() : null;
    }

    protected @Produces @Config
    HttpSession produceHttpSession(){
        return produceServletRequest() != null ? produceServletRequest().getSession(false) : null;
    }


    public void addConfigEntry(String key, Object value) {
        configMap.put(key, value);
    }
}
