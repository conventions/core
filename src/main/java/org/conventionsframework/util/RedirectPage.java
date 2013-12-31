package org.conventionsframework.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rmpestano on 12/27/13.
 */
public class RedirectPage {


    String pageToRedirect;


    public RedirectPage() {
    }

    public RedirectPage(String pageToRedirect) {
        this.pageToRedirect = pageToRedirect;
    }

    public void redirect(){

        ExternalContext ec =  FacesContext.getCurrentInstance().getExternalContext();
        try {
            if (ec.isResponseCommitted()) {
                Logger.getLogger(RedirectPage.class.getName()).log(Level.WARNING, null, "Could not redirect to outcome:"+pageToRedirect +" cause response is already commited");
                return; // redirect is not possible
            }
            ec.redirect(ec.getRequestContextPath() + (pageToRedirect));
        } catch (Exception e) {
            Logger.getLogger(RedirectPage.class.getName()).log(Level.SEVERE, null, e);
        }

    }
}
