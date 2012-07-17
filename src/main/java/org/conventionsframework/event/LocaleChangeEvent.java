/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventionsframework.event;

import java.io.Serializable;

/**
 *
 * @author Rafael M. Pestano Apr 22, 2011 4:29:59 PM
 */

public class LocaleChangeEvent implements Serializable{
    
    private String locale;

    public LocaleChangeEvent(String locale) {
        this.locale = locale;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    
    
}
