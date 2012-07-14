/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
 

/**
 *
 * @author rmpestano May 28, 2011 11:55:07 AM
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER,ElementType.METHOD,ElementType.TYPE })
public @interface Log
{
}

