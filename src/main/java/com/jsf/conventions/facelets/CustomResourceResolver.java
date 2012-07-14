/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.facelets;

import com.sun.faces.facelets.impl.DefaultResourceResolver;
import java.net.URL;

/**
 *
 * @author rmpestano
 */
public class CustomResourceResolver extends DefaultResourceResolver 
{
    @Override
    public URL resolveUrl(String resource)
    {
        URL resourceUrl = super.resolveUrl(resource);
        if (resourceUrl == null)
        {
            if (resource.startsWith("/"))
            {
                resource = resource.substring(1);
            }
            resourceUrl = Thread.currentThread().getContextClassLoader().getResource(resource);
        }
        return resourceUrl;
    }
}