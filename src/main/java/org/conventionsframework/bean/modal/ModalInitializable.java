/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.conventionsframework.bean.modal;

import org.conventionsframework.event.ModalInitialization;
import javax.enterprise.event.Observes;

/**
 *
 * @author rmpestano Jul 9, 2011 9:45:04 AM
 */
public interface ModalInitializable {
    
    void beforeOpen(@Observes ModalInitialization modalInit);
    
}
