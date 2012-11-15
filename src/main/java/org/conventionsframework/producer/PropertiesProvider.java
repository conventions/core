/*
 * Copyright 2011-2012 Conventions Framework.  
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

import org.conventionsframework.qualifier.PropertyFile;
import org.conventionsframework.qualifier.PropertyKey;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Rafaem M. Pestano - Jul 7, 2012 7:03:43 PM
 * 
 */
@Named
@ApplicationScoped
public class PropertiesProvider implements Serializable {
    
    private String defaultPropertiesPath;
    private Map<String,Properties> propsMap = new HashMap<String, Properties>();
    private Properties currentProps;
    private Logger log = Logger.getLogger(PropertiesProvider.class.getSimpleName());

    
    public PropertiesProvider() {
        loadDefaultProperties();
        if (defaultPropertiesPath != null) {
            this.loadProperties(defaultPropertiesPath);
            if(log.isLoggable(Level.FINE)){
                log.log(Level.INFO, "Conventions properties provider loaded successfully.");
            }
        }
    }

    public PropertiesProvider(String pathToFile) {
        loadProperties(pathToFile);
    }
     
    
    public void loadProperties(String pathToFile) {
        if("".endsWith(pathToFile)){//if no file is provided use the default file
            loadDefaultProperties();
            pathToFile = defaultPropertiesPath;
        }
        try {
            if(!propsMap.containsKey(pathToFile)){
                Properties props = new Properties();
                props.load(PropertiesProvider.class.getResourceAsStream(pathToFile));
                propsMap.put(pathToFile, props);
            }
           currentProps = propsMap.get(pathToFile);
        } catch (IOException e) {
            if(log.isLoggable(Level.WARNING)){
                log.log(Level.WARNING, "Conventions could not load property file at " + pathToFile);
                e.printStackTrace();
                
            }
        }
    }

    @Produces
    @PropertyKey
    public String produceKey(InjectionPoint ip) {
        
        if(ip.getAnnotated().isAnnotationPresent(PropertyKey.class)){
            PropertyKey property = ip.getAnnotated().getAnnotation(PropertyKey.class);
            this.loadProperties(property.file());
            return currentProps.getProperty(property.key());
        }
        return "";
    }
    
    @Produces
    @PropertyFile
    public Properties produceProperty(InjectionPoint ip) {
        
        if(ip.getAnnotated().isAnnotationPresent(PropertyFile.class)){
            PropertyFile property = ip.getAnnotated().getAnnotation(PropertyFile.class);
            if(!"".endsWith(property.file())){
                this.loadProperties(property.file());
            }
        }
        return currentProps;
    }

    public Properties getCurrentProps() {
        return currentProps;
    }

    public void setCurrentProps(Properties currentProps) {
        this.currentProps = currentProps;
    }

    public Map<String, Properties> getPropsMap() {
        return propsMap;
    }

    public void setPropsMap(Map<String, Properties> propsMap) {
        this.propsMap = propsMap;
    }

    private void loadDefaultProperties() {
         try{
           defaultPropertiesPath = (String)FacesContext.getCurrentInstance().getExternalContext().getInitParameterMap().get("DEFAULT_PROPERTIES_PATH");
        }catch(NullPointerException ex){
            
        }
    }
    
    

}
