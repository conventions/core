package org.conventionsframework.security;

import org.conventionsframework.qualifier.LoggedIn;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by rmpestano on 12/31/13.
 */
@SessionScoped
public class DefaultSecurityContext implements Serializable, SecurityContext {

    @Produces
    @LoggedIn
    @Named
    public Boolean loggedIn() {
        return  Boolean.TRUE;
    }




}
