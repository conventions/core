/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.conventionsframework.converter;

import org.conventionsframework.service.BaseService;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author rafael.pestano
 */
public abstract class AbstractBaseConverter implements Converter {
    
    private BaseService baseService;

    public BaseService getBaseService() {
        return baseService;
    }

    public void setBaseService(BaseService baseService) {
        this.baseService = baseService;
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value == null || "".equals(value)){
            return null;
        }
         try {
            Long id = new Long(value);
            return getBaseService().get(id);
        } catch (ClassCastException ex) {
            ex.printStackTrace();
            return value;
        }
        catch(NumberFormatException ne){
            ne.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if(o != null){
            return o.toString();
        }
        else{
            return "";
        }
    }

}
