package org.conventionsframework.filter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rmpestano
 */

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.conventionsframework.qualifier.Log;
import org.conventionsframework.security.SecurityContext;
import org.conventionsframework.util.Constants;

@WebFilter(urlPatterns={"/*"})
public class ConventionsFilter implements Filter {
    
    private static final String FACES_RESOURCES = "javax.faces.resource";
    private static final String INDEX = "index.html";
    
    @Inject
    protected SecurityContext securityContext;
    
    @Inject
    @Log
    transient Logger log;


    String initialPage;

    String errorPage;


    String ignoredResource = null;

    boolean disableFilter = false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String disableConventionsFilter = filterConfig.getServletContext().getInitParameter(Constants.InitialParameters.DISABLE_CONVENTIONS_FILTER);
        if(!"".equals(disableConventionsFilter) && Boolean.valueOf(disableConventionsFilter)){
            disableFilter = true;
        }
        String ignoredResource = filterConfig.getServletContext().getInitParameter(Constants.InitialParameters.IGNORE_RESOURCE);

        if(!"".equals(ignoredResource)){
            this.ignoredResource = ignoredResource;
        }

        initialPage =  filterConfig.getServletContext().getInitParameter(Constants.InitialParameters.INITIAL_PAGE);
        if(initialPage == null || "".equals(initialPage)){
            this.initialPage = "login.xhtml";
        }
        errorPage =  filterConfig.getServletContext().getInitParameter(Constants.InitialParameters.ERROR_PAGE);
        if(errorPage == null || "".equals(errorPage)){
            this.errorPage = "/error.xhtml";
        }
        
        this.errorPage = this.errorPage.replaceAll("/", "");
        this.initialPage = this.initialPage.replaceAll("/", "");
        log.info("initialPage:"+this.initialPage);
        log.info("errorPage:"+this.errorPage);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if(disableFilter){
            return;
        }
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) req;
         if (skipResource(request)
        			|| securityContext.loggedIn()) {
        		chain.doFilter(req, resp);
        } else  {
             request.setAttribute("logoff", "true");//let ConventionsExceptionHandler redirect to logon
             request.setAttribute("queryString", request.getQueryString());
             chain.doFilter(req,resp);
        }
        return;
        
    }

    @Override
    public void destroy() {
    }
    
    private boolean skipResource(HttpServletRequest request) {
		String path = request.getServletPath().replaceAll("/", "");
		boolean skip =  path.startsWith(FACES_RESOURCES) || path.equalsIgnoreCase(initialPage) || path.equalsIgnoreCase(INDEX) || path.equalsIgnoreCase(errorPage)
				 || (ignoredResource != null && path.contains(ignoredResource));
        return skip;
	}
    
    
}
