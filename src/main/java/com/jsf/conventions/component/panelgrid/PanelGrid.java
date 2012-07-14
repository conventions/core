/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsf.conventions.component.panelgrid;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIPanel;

/**
 *
 * @author rmpestano
 */
@ResourceDependency(library = "css", name = "panelGrid/panelgrid.css")
public class PanelGrid extends UIPanel {

    private static final String RENDERER_TYPE = "com.jsf.conventions.component.PanelGridRenderer";
    private static final String COMPONENT_TYPE = "com.jsf.conventions.component.PanelGrid";
    private static final String COMPONENT_FAMILY = "com.jsf.conventions.component";
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