package org.conventionsframework.security;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 * Created by rmpestano on 12/31/13.
 */
@SessionScoped
public class DefaultSecurityContext implements Serializable, SecurityContext {


    public Boolean loggedIn() {
        return  Boolean.TRUE;
    }

    public Boolean hasRole(String role){
        return Boolean.TRUE;
    }




}
