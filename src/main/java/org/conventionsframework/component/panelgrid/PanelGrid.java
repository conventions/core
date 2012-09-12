/*
 * Copyright 2012 Conventions Framework.  
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

package org.conventionsframework.component.panelgrid;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIPanel;

/**
 *
 * @author rmpestano
 */
@ResourceDependency(library = "css", name = "panelGrid/panelgrid.css")
public class PanelGrid extends UIPanel {

    private static final String RENDERER_TYPE = "org.conventionsframework.component.PanelGridRenderer";
    private static final String COMPONENT_TYPE = "org.conventionsframework.component.PanelGrid";
    private static final String COMPONENT_FAMILY = "org.conventionsframework.component";
    public final static String CONTAINER_CLASS = "ui-panelgrid ui-widget";
    public final static String HEADER_CLASS = "ui-panelgrid-header";
    public final static String FOOTER_CLASS = "ui-panelgrid-footer";
    public final static String ROW_CLASS = "ui-widget-content";

    protected enum PropertyKeys {

        columns, style, styleClass, columnClasses, noBorders;
        String toString;

        PropertyKeys(String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return ((this.toString != null) ? this.toString : super.toString());
        }
    }

    public PanelGrid() {
        setRendererType(RENDERER_TYPE);
    }


    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    

    public String getColumns() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.columns);
    }

    public void setColumns(String _columns) {
        getStateHelper().put(PropertyKeys.columns, _columns);
    }
      public Boolean getNoBorders() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.noBorders);
    }

    public void setNoBorders(Boolean noBorder) {
        getStateHelper().put(PropertyKeys.noBorders, noBorder);
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
    }

    public java.lang.String getColumnClasses() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.columnClasses, null);
    }

    public void setColumnClasses(java.lang.String _columnClasses) {
        getStateHelper().put(PropertyKeys.columnClasses, _columnClasses);
    }

}