/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jsf.conventions.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Rafael M. Pestano
 */
  
  public class MessagesController {
     
     public static void addInfo(String summary,String detail) {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,summary, detail));
      }
     public static void addInfo(String summary) {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,summary,""));
      }

      public static void addWarn(String summary,String detail) {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,summary, detail));
      }
      public static void addWarn(String summary) {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,summary, ""));
      }

     public static void addError(String summary,String detail) {
         addBusinessErroCallbackParam();
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,summary,detail ));
      }
     public static void addError(String summary) {
          /* 
             * businessError param is used by framework:messageHide component
             * also can be used to keep a dialog open on error
             */
         addBusinessErroCallbackParam();
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,summary,"" ));
      }

      public static void addFatal(String summary) {
          addBusinessErroCallbackParam();
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,summary, ""));
       }
      public static void addFatal(String summary,String detail) {
          RequestContext.getCurrentInstance().addCallbackParam("businessError", true);
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,summary, detail));
       }

    private static void addBusinessErroCallbackParam() {
        RequestContext rc = RequestContext.getCurrentInstance();
        if(rc != null){
             rc.addCallbackParam("businessError", true);
         }
    }


  }
