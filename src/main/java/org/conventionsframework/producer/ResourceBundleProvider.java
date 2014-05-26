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

import org.conventionsframework.event.LocaleChangeEvent;
import org.conventionsframework.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Rafael M. Pestano
 */
@Named
@ApplicationScoped
public class ResourceBundleProvider implements Serializable {

	private static final long serialVersionUID = 1L;
	private String baseName = "messages";
	private String currentLanguage;
	private Map<String, ResourceBundle> bundleMap = new HashMap<String, ResourceBundle>();
	private Logger log = Logger.getLogger(getClass().getSimpleName());
	private ResourceBundle currentBundle;

	public ResourceBundleProvider() {
		FacesContext context = FacesContext.getCurrentInstance();
		if(context != null){
            baseName = context.getExternalContext().getInitParameter("BUNDLE_BASE_NAME");
            if(baseName == null){
            	baseName = "messages";
            }
            currentLanguage = context.getViewRoot().getLocale().getLanguage();
		}
        else{
            currentLanguage = Locale.getDefault().getLanguage();
        }
		this.loadResourceBundle();
	}

	/**
	 * sets the currentBundle based on language
	 */
	private void loadResourceBundle() {
        Locale locale = Locale.forLanguageTag(currentLanguage);
        FacesContext context = FacesContext.getCurrentInstance();
        if(context != null){
            context.getViewRoot().setLocale(locale);
        }
		if (bundleMap.containsKey(currentLanguage)) {
			currentBundle = bundleMap.get(currentLanguage);

		} else {
			try {
				currentBundle = new ResourceBundle(baseName,locale);
				bundleMap.put(currentLanguage, currentBundle);
				log.fine("Conventions: loaded resource bundle:" + baseName
						+ "_" + currentLanguage + ".properties");
			} catch (Exception e) {
				log.log(Level.SEVERE,
						"Conventions: problems trying to find resource bundle:"
								+ baseName + "_" + currentLanguage
								+ ".properties", e);
			}
		}

	}

	public String getCurrentLocale() {
		return currentLanguage;
	}

	public void setCurrentLocale(String locale) {
		this.currentLanguage = locale;
		this.loadResourceBundle();
	}

	@Produces
	@Default
	@Any
	public ResourceBundle getCurrentBundle() {
		return currentBundle;
	}

	public void setCurrentBundle(ResourceBundle currentBundle) {
		this.currentBundle = currentBundle;
	}

	public void onLocaleChange(@Observes LocaleChangeEvent localeChangeEvent) {
		this.setCurrentLocale(localeChangeEvent.getLocale());
	}

	public Map<String, ResourceBundle> getBundleMap() {
		return bundleMap;
	}

	public void setBundleMap(Map<String, ResourceBundle> map) {
		this.bundleMap = map;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
}
