/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.exception;

import java.io.Serializable;

/**
 *
 * @author rmpestano Jun 21, 2011 11:01:06 PM
 */
 
public class BusinessException extends RuntimeException implements Serializable {

    private String summary;
    private String detail;

    public BusinessException() {
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String detail) {
        this.detail = detail;
    }

    /**
     * 
     * @param summary
     * @param detail 
     */
    public BusinessException(String summary, String detail) {
        this.detail = detail;
        this.summary = summary;
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
}
