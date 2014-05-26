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
package org.conventionsframework.util;

import org.conventionsframework.qualifier.ConventionsBundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

@ConventionsBundle//just to disambiguate, resourceBundle is produced by ResourceBundleProvider
public class ResourceBundle extends java.util.ResourceBundle implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Control UTF8_CONTROL = new UTF8Control();

    private java.util.ResourceBundle bundle;


    public ResourceBundle() {
        bundle = java.util.ResourceBundle.getBundle("messages", Locale.getDefault(),UTF8_CONTROL);
        setParent(bundle);
    }

    public ResourceBundle(String baseName) {
        bundle = java.util.ResourceBundle.getBundle(baseName, Locale.getDefault(),UTF8_CONTROL);
        setParent(bundle);
    }

    public ResourceBundle(String baseName, Locale locale) {
        bundle = java.util.ResourceBundle.getBundle(baseName, locale,UTF8_CONTROL);
        setParent(bundle);
    }

    public ResourceBundle(java.util.ResourceBundle bundle) throws IOException {
        this.bundle = bundle;
        setParent(bundle);
    }


    @Override
    protected Object handleGetObject(String key) {
        try {
            return parent.getString(key);
        } catch (MissingResourceException e) {
            return "??" + key + "??";
        }
    }

    @Override
    public Enumeration<String> getKeys() {
        return parent.getKeys();
    }



	public String getString(String key, Object... params) {
        try {
            return MessageFormat.format(this.getString(key), params);
        } catch (MissingResourceException e) {
            return "??" + key + "??";
        }
    }

    public static class UTF8Control extends Control {
    	public java.util.ResourceBundle newBundle
    	(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
    	throws IllegalAccessException, InstantiationException, IOException
    	{
    	    // The below code is copied from default Control#newBundle() implementation.
    	    // Only the PropertyResourceBundle line is changed to read the file as UTF-8.

    	    String bundleName = toBundleName(baseName, locale);
    	    String resourceName = toResourceName(bundleName, "properties");
    	    java.util.ResourceBundle bundle = null;
    	    InputStream stream = null;
    	    if (reload) {
    	        URL url = loader.getResource(resourceName);
    	        if (url != null) {
    	            URLConnection connection = url.openConnection();
    	            if (connection != null) {
    	                connection.setUseCaches(false);
    	                stream = connection.getInputStream();
    	            }
    	        }
    	    } else {
    	        stream = loader.getResourceAsStream(resourceName);
    	    }
    	    if (stream != null) {
    	        try {
    	            bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
    	        } finally {
    	            stream.close();
    	        }
    	    }
    	    return bundle;
    	}
    	}
}
