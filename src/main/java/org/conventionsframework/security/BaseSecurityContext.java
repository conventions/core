package org.conventionsframework.security;

import org.conventionsframework.qualifier.LoggedIn;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by rmpestano on 12/31/13.
 */
public abstract class BaseSecurityContext implements Serializable {

    @Produces
    @LoggedIn //used by ConventionsFilter
    @Named
    public Boolean loggedIn() {
        return isLoggedIn();
    }

    /**
     * must be overriden to decide weather user is logged in or not
     * @return <code>true</code> if user is logged in, <code>false</code> otherwise
     */
    protected  Boolean isLoggedIn(){
        return Boolean.TRUE;//by default user is always logged in
    }
}
