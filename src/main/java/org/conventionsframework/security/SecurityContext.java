package org.conventionsframework.security;

/**
 * Created by rmpestano on 1/5/14.
 */
public interface SecurityContext {

    Boolean loggedIn();

    Boolean hasRole(String role);

    Boolean hasAnyRole(String[] role);

}
