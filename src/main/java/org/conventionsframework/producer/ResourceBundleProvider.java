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
package org.conventionsframework.producer;

import org.conventionsframework.event.LocaleChangeEvent;
import org.conventionsframework.util.ResourceBundle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * 
 * @author Rafael M. Pestano
 */
@Named
@ApplicationScoped
public class ResourceBundleProvider implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_BASE_NAME = "messages";
	private String baseName;
	private String currentLocale;
	private Map<String, ResourceBundle> bundleMap = new HashMap<String, ResourceBundle>();
	private Logger log = Logger.getLogger(getClass().getSimpleName());
	private ResourceBundle currentBundle;

	public ResourceBundleProvider() {
		String baseNameParam = null;
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			baseNameParam = (String) FacesContext.getCurrentInstance()
					.getExternalContext().getInitParameterMap()
					.get("BUNDLE_BASE_NAME");

		}
		if (baseNameParam == null) {
			baseName = DEFAULT_BASE_NAME;
		} else {
			baseName = baseNameParam;
		}
		if(context != null){
			currentLocale = FacesContext.getCurrentInstance().getViewRoot()
					.getLocale().getLanguage();
		}
		else{
			currentLocale = Locale.ENGLISH.getLanguage();
		}
		this.changeResourceBundle();
	}

	/**
	 * sets the currentBundle based on the baseName and language
	 */
	private void changeResourceBundle() {
		if (bundleMap.containsKey(currentLocale)) {
			currentBundle = bundleMap.get(currentLocale);

		} else {
			try {
				currentBundle = new ResourceBundle(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(
								baseName + "_" + currentLocale + ".properties"));
				bundleMap.put(currentLocale, currentBundle);
				log.fine("Conventions: loaded resource bundle:" + baseName
						+ "_" + currentLocale + ".properties");
			} catch (Exception e) {
				log.log(Level.SEVERE,
						"Conventions: problems trying to find resource bundle:"
								+ baseName + "_" + currentLocale
								+ ".properties", e);
			}
		}

	}

	public String getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(String locale) {
		this.currentLocale = locale;
		this.changeResourceBundle();
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
