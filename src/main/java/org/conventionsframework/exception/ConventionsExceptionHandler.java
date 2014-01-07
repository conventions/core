package org.conventionsframework.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.conventionsframework.util.Constants;
import org.conventionsframework.util.MessagesController;
import org.primefaces.context.RequestContext;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConventionsExceptionHandler extends ExceptionHandlerWrapper {


    private ExceptionHandler wrapped;

    BusinessException be;

    public ConventionsExceptionHandler(ExceptionHandler exception) {
        this.wrapped = exception;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        handleException(FacesContext.getCurrentInstance());
        wrapped.handle();
    }

    private void handleException(FacesContext context) {
        final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        final Iterator<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = getUnhandledExceptionQueuedEvents().iterator();

        if (unhandledExceptionQueuedEvents.hasNext()) {

            Throwable exception = unhandledExceptionQueuedEvents.next().getContext().getException();
            unhandledExceptionQueuedEvents.remove();
            Throwable rootCause = ExceptionUtils.getRootCause(exception);
            if (rootCause instanceof BusinessException) {
                be = (BusinessException) rootCause;
            }

            if (be != null) {
                if (be.getSeverity() != null) {
                    MessagesController.addMessage(be.getSummary(), be.getDetail(), be.getSeverity());
                } else {
                    MessagesController.addError(be.getSummary(), be.getDetail());
                }
                this.scrollAndFocusOnError(be);
                if (be.getRedirectPage() != null) {
                    be.getRedirectPage().redirect();
                }
                return;
            } else if (exception instanceof ViewExpiredException == false) {
                //unexpected exceptions
                handleFatalError(exception, context);
            }

        }

        //remove enqueued exceptions
        while (unhandledExceptionQueuedEvents.hasNext()) {
            unhandledExceptionQueuedEvents.next();
            unhandledExceptionQueuedEvents.remove();
        }

        if (request.getAttribute("logoff") != null && request.getAttribute("logoff").equals("true")) {
            ExternalContext externalContext = context.getExternalContext();
            String initialPage = externalContext.getInitParameter(Constants.InitialParameters.INITIAL_PAGE);
            if (initialPage == null || "".equals(initialPage)) {
                initialPage = "/login.xhtml";
            }
            if (!initialPage.startsWith("/")) {
                initialPage = "/" + initialPage;
            }
            try {
                String referer = request.getHeader("Referer");
                String recoveryUrlParams = "";
                if (referer != null && !"".equals(referer)) {
                    if (referer.contains("?")) {
                        recoveryUrlParams = referer.substring(referer.lastIndexOf("?") + 1);
                    }
                } else { //try to get params from queryString
                    recoveryUrlParams = (String) request.getAttribute("queryString");
                }
                StringBuilder recoveryUrl = new StringBuilder(context.getViewRoot().getViewId());
                if (!"".equals(recoveryUrlParams)) {
                    recoveryUrl.append("?").append(recoveryUrlParams);
                }
                context.getExternalContext().redirect(externalContext.getRequestContextPath() + initialPage + "?page=" + URLEncoder.encode(recoveryUrl.toString(), "UTF-8"));

            } catch (Exception e) {
                throw new RuntimeException("Could not redirect to " + initialPage, e);
            }
        }
        return;
    }

    /**
     * gather information about the exception and pass the info to errorBean so
     * the error details can be displayed to end user.
     *
     * @param ex
     */
    private void handleFatalError(final Throwable ex, FacesContext context) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        String errorName = (rootCause == null) ? ex.getClass().getCanonicalName() : rootCause.getClass().getCanonicalName();
        String errorMessage = ExceptionUtils.getRootCauseMessage(ex);
        String stackTrace = ExceptionUtils.getStackTrace(rootCause == null ? ex : rootCause);
        /*
         * ELFlash.getFlash().put("errorName", errorName); /
         * ELFlash.getFlash().put("errorMessage", errorMessage);
         * ELFlash.getFlash().put("stackTrace", stackTrace); flash doesnt help
         * here, the ideia is to redirect to an error outcome
         */

        ErrorMBean errorMBean = context.getApplication().evaluateExpressionGet(context, "#{convErrorMBean}", ErrorMBean.class);
        if (errorMBean == null) {//will be null when fatal error occour on app startup(before context or beanManager initialized)
            throw new RuntimeException("Found problems while initializing application, errorName:" + errorName + " errorMessage:" + errorMessage + " \nSTACKTRACE: " + stackTrace);
        }
        errorMBean.setErrorMessage(errorMessage);
        errorMBean.setErrorName(errorName);
        errorMBean.setStacktrace(stackTrace);
        goToErrorPage(context);
    }

    /**
     * redirect to an error outcome('errorPage' navigation rule) with exception
     * information to display to the user in a friendly way
     */
    private void goToErrorPage(FacesContext context) {
        try {
            ExternalContext ec = context.getExternalContext();
            if (ec.isResponseCommitted()) {
                return;
            }
            String errorPage = ec.getInitParameter(Constants.InitialParameters.ERROR_PAGE);
            if (errorPage == null || "".equals(errorPage)) {
                errorPage = "/error.xhtml";
            }

            if (!errorPage.startsWith("/")) {
                errorPage = "/" + errorPage;
            }

            ec.redirect(context.getExternalContext().getRequestContextPath() + errorPage);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(ConventionsExceptionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void scrollAndFocusOnError(BusinessException be) {
        if (be.getId() != null && !"".endsWith(be.getId())) {
            RequestContext rc = RequestContext.getCurrentInstance();
            String componentId = be.getId();
            if (rc != null) {
                rc.scrollTo(componentId);
                String js = "if(document.getElementById('" + componentId + "')){document.getElementById('" + componentId + "').focus();}";
                rc.execute(js);
            }
        }
    }

}
