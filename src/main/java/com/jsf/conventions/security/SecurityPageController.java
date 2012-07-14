    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.security;

import com.jsf.conventions.bean.StateController;
import com.jsf.conventions.util.Constants;
import com.jsf.conventions.util.MessagesController;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author rmpestano Jun 24, 2012 19:23:17 AM
 */
@SessionScoped
@Named(value = "pageController")
public class SecurityPageController implements Serializable {

    private String message;//message to show when user has no access to the page
    private String outcome;//outcome to forward user when user has no access to the page
    @Inject
    private StateController stateController;

    public void checkUserAccess(String rolesAlowed, String outcome, String message, boolean removeLastState) {
        if (!FacesContext.getCurrentInstance().isPostback() && rolesAlowed != null && !"".endsWith(rolesAlowed)) {//do not check on ajax requests
            List<String> userRoles = (List<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Constants.USER_ROLES);
            if (userRoles != null) {
                String[] roles = rolesAlowed.split("[\\s,;]+");//comma,space or semicolon separed list of rolesAllowed
                for (String role : roles) {
                    if (userRoles.contains(role)) {
                        return;
                    }
                }
            }
            //if it gets here user has no access to the page
            if (message != null && !"".endsWith(message)) {
                MessagesController.addFatal(message);
            }
            if (removeLastState) {//will remove the last state from stateList - useful when using securityPage in conjunction with statePusher 
                if (stateController != null && stateController.getStateItens() != null && !stateController.getStateItens().isEmpty()) {
                    stateController.getStateItens().removeLast();
                }
            }

            if (outcome != null && !"".endsWith(outcome)) {
                try {
                    FacesContext context = FacesContext.getCurrentInstance();
                    NavigationHandler navHandler = context.getApplication().getNavigationHandler();
                    navHandler.handleNavigation(context, null, outcome);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(SecurityPageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }//end !isPostback
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return outcome to match a navigation rule when user has no access to the
     * page
     */
    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
