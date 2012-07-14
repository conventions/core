/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.event;

import java.io.Serializable;

/**
 *
 * @author Rafael M. Pestano Apr 22, 2011 4:29:59 PM
 */

public class LanguageChangeEvent implements Serializable{
    
    private String language;

    public LanguageChangeEvent(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
}
