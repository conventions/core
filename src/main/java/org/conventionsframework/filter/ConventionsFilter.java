package org.conventionsframework.filter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rmpestano
 */

import org.conventionsframework.security.SecurityContext;
import org.conventionsframework.util.Constants;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns={"/*"})
public class ConventionsFilter implements Filter {
    
    private static final String FACES_RESOURCES = "/javax.faces.resource";
    private static final String INDEX = "/index.html";
    
    @Inject
    protected SecurityContext securityContext;


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

        String initialPage =  filterConfig.getServletContext().getInitParameter(Constants.InitialParameters.INITIAL_PAGE);
        if(initialPage == null || "".equals(initialPage)){
            this.initialPage = "/login.xhtml";
        }
        else{
            this.initialPage = "/"+initialPage;
        }
        String errorPage =  filterConfig.getServletContext().getInitParameter(Constants.InitialParameters.ERROR_PAGE);
        if(errorPage == null || "".equals(errorPage)){
            this.errorPage = "/error.xhtml";
        }
        else{
            this.errorPage = "/"+errorPage;
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if(disableFilter){
            return;
        }
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
         if (skipResource(request)
        			|| securityContext.loggedIn()) {
        		chain.doFilter(req, resp);
        } else {
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
		String path = request.getServletPath();
        return  path.startsWith(FACES_RESOURCES) || path.equalsIgnoreCase(initialPage) || path.equalsIgnoreCase(INDEX) || path.equalsIgnoreCase(errorPage)
				 || (ignoredResource != null && path.contains(ignoredResource));
	}
}
