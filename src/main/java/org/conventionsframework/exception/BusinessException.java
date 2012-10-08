/*
 * Copyright 2011-2012 Conventions Framework.  
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
package org.conventionsframework.exception;

import java.io.Serializable;
import javax.ejb.ApplicationException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

/**
 *
 * @author Rafael M. Pestano Jun 21, 2011 11:01:06 PM
 */
@ApplicationException
public class BusinessException extends RuntimeException implements Serializable {

    private String summary;
    private String detail;
    private String id;
    private FacesMessage.Severity severity;

    public BusinessException() {
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    /**
     * 
     * @param summary exception summary
     */
    public BusinessException(String summary) {
        super(summary);
        this.summary = summary;
    }

    
    /**
     * @param summary exception summary
     * @param idToFocus view component id to scroll to when exception occurs
     */
    public BusinessException(String summary, String idToFocus) {
        super(summary);
        this.summary = summary;
        this.id = idToFocus;
    }

    /**
     * @param summary exception summary
     * @param severity Faces message severity
     */
    public BusinessException(String summary, FacesMessage.Severity severity) {
        super(summary);
        this.summary = summary;
        this.severity = severity;
    }
    
    /**
     * @param summary exception summary
     * @param severity Faces message severity
     * @param idToFocus view component id to scroll to when exception occurs
     */
    public BusinessException(String summary, FacesMessage.Severity severity, String idToFocus) {
        super(summary);
        this.summary = summary;
        this.severity = severity;
        this.id = idToFocus;
    }

    /**
     * @param summary exception summary
     * @param detail exception detail
     * @param idToFocus view component id to scroll to when exception occurs
     */
    public BusinessException(String summary, String detail, String idToFocus) {
        super(summary);
        this.detail = detail;
        this.summary = summary;
        this.id = idToFocus;
    }

    /**
     * @param summary exception summary
     * @param detail exception detail
     * @param severity Faces message severity
     */
    public BusinessException(String summary, String detail, FacesMessage.Severity severity) {
        super(summary);
        this.detail = detail;
        this.summary = summary;
        this.severity = severity;
    }

    /**
     * @param summary exception summary
     * @param detail exception detail
     * @param severity Faces message severity
     * @param idToFocus view component id to scroll to when exception occurs
     */
    public BusinessException(String summary, String detail, FacesMessage.Severity severity, String idToFocus) {
        super(summary);
        this.detail = detail;
        this.summary = summary;
        this.severity = severity;
        this.id = idToFocus;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
