/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventionsframework.exception;

import java.io.Serializable;
import javax.ejb.ApplicationException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

/**
 *
 * @author rmpestano Jun 21, 2011 11:01:06 PM
 */
 
@ApplicationException
public class BusinessException extends RuntimeException implements Serializable {

    private String summary;
    private String detail;
    private FacesMessage.Severity severity; 

    public BusinessException() {
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String summary) {
        super(summary);
        this.summary = summary;
    }
    
    
    public BusinessException(String summary,FacesMessage.Severity severity) {
        super(summary);
        this.summary = summary;
        this.severity = severity;
    }

    /**
     * 
     * @param summary
     * @param detail 
     */
    public BusinessException(String summary, String detail) {
        super(summary);
        this.detail = detail;
        this.summary = summary;
    }
    public BusinessException(String summary, String detail,FacesMessage.Severity severity) {
        super(summary);
        this.detail = detail;
        this.summary = summary;
        this.severity = severity;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
   
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
      this.summary = summary;
    }

    public Severity getSeverity() {
        return severity;
    }
    
}
