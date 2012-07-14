/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jsf.conventions.util;

import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

 
/**
 * FROM: http://weblogs.java.net/blog/edburns/archive/2009/09/03/dealing-gracefully-viewexpiredexception-jsf2
 * @author rmpestano
 */
public class ViewExpiredExceptionExceptionHandler extends ExceptionHandlerWrapper{
    private ExceptionHandler wrapped;

    public ViewExpiredExceptionExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();
            if (t instanceof ViewExpiredException) {
                FacesContext fc = FacesContext.getCurrentInstance();
                try {
                    fc.getExternalContext().addResponseHeader("redirect", fc.getExternalContext().getRequestContextPath());
                    fc.getExternalContext().setResponseStatus(666);
                } finally {
                    i.remove();
                    t.printStackTrace();
                }
            }
        }

        // At this point, the queue will not contain any ViewExpiredEvents.
        // Therefore, let the parent handle them.
        getWrapped().handle();
    }

   
}
