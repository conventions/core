package org.conventionsframework.producer;

import org.conventionsframework.event.LocaleChangeEvent;
import org.conventionsframework.util.ResourceBundle;
import java.io.Serializable;
import java.util.HashMap;
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
 * @author rmpestano
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
        String baseNameParam = (String) FacesContext.getCurrentInstance().getExternalContext().getInitParameterMap().get("BUNDLE_BASE_NAME");
        if(baseNameParam == null){
            baseName = DEFAULT_BASE_NAME;
        }
        else{
            baseName = baseNameParam;
        }
        currentLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
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
                currentBundle = new ResourceBundle(getClass().getResourceAsStream(baseName + "_" + currentLocale + ".properties"));
                bundleMap.put(currentLocale,currentBundle);
                if(log.isLoggable(Level.FINE)){
                	log.fine("Conventions: loaded resource bundle:"+baseName + "_" + currentLocale + ".properties");
                }
            } catch (Exception e) {
            	if(log.isLoggable(Level.WARNING)){
            		log.warning("Conventions: problems trying to find resource bundle:"+baseName + "_" + currentLocale + ".properties");
            	}
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

    @Produces @Default @Any
    public ResourceBundle getCurrentBundle() {
        return currentBundle;
    }

    public void setCurrentBundle(ResourceBundle currentBundle) {
        this.currentBundle = currentBundle;
    }
    
    public void onLocaleChange(@Observes LocaleChangeEvent localeChangeEvent){
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
