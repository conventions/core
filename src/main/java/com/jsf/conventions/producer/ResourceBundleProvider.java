package com.jsf.conventions.producer;

import com.jsf.conventions.event.LanguageChangeEvent;
import com.jsf.conventions.util.ResourceBundle;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author rmpestano
 */
@Named
@ApplicationScoped
public class ResourceBundleProvider implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_BASE_NAME = "messages";
    private String baseName;
    private String language;
    private Map<String, ResourceBundle> bundleMap = new HashMap<String, ResourceBundle>();
    private Logger log = Logger.getLogger(getClass().getSimpleName());
    private ResourceBundle currentBundle;

    public ResourceBundleProvider() {
        String baseNameParam = (String) FacesContext.getCurrentInstance().getExternalContext().getInitParameterMap().get("BUNDLE_BASE_NAME");
        if(baseNameParam == null){
            baseName = DEFAULT_BASE_NAME;
        }
        else{
            baseName = baseNameParam;
        }
        language = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
        this.changeResourceBundle();
    }


    /**
     * sets the currentBundle based on the baseName and language
     */ 
    private void changeResourceBundle() {
        if (bundleMap.containsKey(language)) {
            currentBundle = bundleMap.get(language);

        } else {
            try {
                currentBundle = new ResourceBundle(getClass().getResourceAsStream(baseName + "_" + language + ".properties"));
                bundleMap.put(language,currentBundle);
                if(log.isLoggable(Level.FINE)){
                	log.fine("Conventions: loaded resource bundle:"+baseName + "_" + language + ".properties");
                }
            } catch (Exception e) {
            	if(log.isLoggable(Level.WARNING)){
            		log.warning("Conventions: problems trying to find resource bundle:"+baseName + "_" + language + ".properties");
            	}
            }
        }

    }

    public String getLanguage() {
            return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        this.changeResourceBundle();
    }

    public ResourceBundle getCurrentBundle() {
        return currentBundle;
    }

    public void setCurrentBundle(ResourceBundle currentBundle) {
        this.currentBundle = currentBundle;
    }
    
    public void onLanguageChange(@Observes LanguageChangeEvent languageChangeEvent){
        this.setLanguage(languageChangeEvent.getLanguage());
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
