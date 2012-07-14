/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.util;

import java.util.Map;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Named;

/**
 *
 * @author rmpestano Apr 8, 2012 at 6:34:19 PM
 */
public class JSFUtils {

    public static Map<String, Object> getRequestMap() {
        return getExternalContext().getRequestMap();
    }

    public static Map<String, Object> getSessionMap() {
        return getExternalContext().getSessionMap();
    }

    public static Object getRequestParam(String key) {
        return getRequestMap().get(key);
    }

    public static Object getSessionParam(String key) {
        return getSessionMap().get(key);
    }

    public static Flash getFlash() {
        return getExternalContext().getFlash();
    }

    public static Object getFlashParam(String key) {
        return getFlash().get(key);
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }
}
