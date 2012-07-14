/*
 * Copyright 2012 rmpestano.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jsf.conventions.component.validationpanel;

import javax.faces.component.UIPanel;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author rmpestano
 */
public class ValidationPanel extends UIPanel {

    private static final String RENDERER_TYPE = "com.jsf.conventions.component.ValidationPanelRenderer";
    private static final String COMPONENT_TYPE = "com.jsf.conventions.component.ValidationPanel";
    private static final String COMPONENT_FAMILY = "com.jsf.conventions.component";
    public static final String defaulErrorStyle = "background: #ffffff;-moz-box-shadow: inset 0 2px 2px #FF9999!important;-webkit-box-shadow: inset 0 2px 2px #FF9999!important;box-shadow: inset 0 2px 2px #FF9999!important;background-color: #fcc!important;background-image:none;padding:5px";
    
    protected enum PropertyKeys {

        style,
        styleClass,
        errorStyle,
        errorStyleClass,
    }

      public ValidationPanel() {
        setRendererType(RENDERER_TYPE);
    }


    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style);

    }

    /**
     * <p>Set the value of the
     * <code>style</code> property.</p>
     */
    public void setStyle(java.lang.String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }
    
      public java.lang.String getErrorStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.errorStyle);

    }

    /**
     * <p>Set the value of the
     * <code>errorStyle</code> property.</p>
     */
    public void setErrorStyle(java.lang.String style) {
        getStateHelper().put(PropertyKeys.errorStyle, style);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass);

    }

    /**
     * <p>Set the value of the
     * <code>styleClass</code> property.</p>
     */
    public void setStyleClass(java.lang.String style) {
        getStateHelper().put(PropertyKeys.styleClass, style);
    }
    
    
    public java.lang.String getErrorStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.errorStyleClass);

    }

    /**
     * <p>Set the value of the
     * <code>errorStyleClass</code> property.</p>
     */
    public void setErrorStyleClass(java.lang.String style) {
        getStateHelper().put(PropertyKeys.errorStyleClass, style);
    }

    
    
    public boolean hasErrorStyle(){
        return(StringUtils.isNotEmpty(getErrorStyle()) || StringUtils.isNotEmpty(getErrorStyleClass()));
    }
}
