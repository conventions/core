/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jsf.conventions.component.panelgrid;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.column.Column;
import org.primefaces.component.row.Row;
import org.primefaces.renderkit.CoreRenderer;

/**
 *
 * @author rmpestano
 */
public class PanelGridRenderer extends CoreRenderer {
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        PanelGrid grid = (PanelGrid) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = grid.getClientId(context);
       
        int[] cols = null;
        if (grid.getColumns() != null) {
            String[] tokens = grid.getColumns().split(",");
            cols = getCols(tokens);
        }
        String style = grid.getStyle();
        String styleClass = grid.getStyleClass();
        styleClass = styleClass == null ? PanelGrid.CONTAINER_CLASS : PanelGrid.CONTAINER_CLASS + " " + styleClass;
                
        writer.startElement("table", grid);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if(style != null) {
            writer.writeAttribute("style", style, "style");
        }
        writer.writeAttribute("role", "grid", null);
                
        encodeFacet(context, grid, getBiggerCol(cols), "header", "thead", PanelGrid.HEADER_CLASS);
        encodeFacet(context, grid, getBiggerCol(cols), "footer", "tfoot", PanelGrid.FOOTER_CLASS);
        encodeBody(context, grid, cols);
        
        writer.endElement("table");
    }
    
    public void encodeBody(FacesContext context, PanelGrid grid, int[] cols) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement("tbody", grid);
        
        if(cols != null ) {
          
            encodeDynamicBody(context, grid, cols,getBiggerCol(cols));
        } 
        else {
            encodeStaticBody(context, grid);
        }

        writer.endElement("tbody");
    }
    
     public void encodeDynamicBody(FacesContext context, PanelGrid grid, int[] columns, int higherCol) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tbody", grid);
        int i = 0;
        int colControl = 0;
        for (UIComponent child : grid.getChildren()) {

                int currentCol = columns[colControl];
                if ((i % currentCol) == 0) {
                    writer.startElement("tr", null);
                    writer.writeAttribute("class", PanelGrid.ROW_CLASS, null);
                    writer.writeAttribute("role", "row", null);
                }

               if (child.isRendered()) {
                writer.startElement("td", null);
                writer.writeAttribute("role", "gridcell", null);
                i++;
                if ((i % currentCol) == 0) {
                    int diff = (higherCol - currentCol);
                    if(diff > 0){
                        writer.writeAttribute("colspan", diff+1, null);
                    }
                    child.encodeAll(context);
                    writer.endElement("td");
                    writer.endElement("tr");
                    i = 0;//go to next row
                    if (colControl >= columns.length - 1) {//no more columns? go to first column attribute
                        colControl = 0;
                    } else {
                        colControl++;//go to next column attribute
                    }
                }
                else {
                 child.encodeAll(context);
                 writer.endElement("td");
               }

            }


        }

        writer.endElement("tbody");
    }
    
    public void encodeStaticBody(FacesContext context, PanelGrid grid) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        for(UIComponent child : grid.getChildren()) {
            if(child instanceof Row && child.isRendered()) {
                encodeRow(context, (Row) child, "gridcell", PanelGrid.ROW_CLASS, null);
            }
        }
    }
    
    public void encodeRow(FacesContext context, Row row, String columnRole, String rowClass, String columnClass) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement("tr", null);
        writer.writeAttribute("class", rowClass, null);
        writer.writeAttribute("role", "row", null);
        
        for(UIComponent child : row.getChildren()) {
            if(child instanceof Column && child.isRendered()) {
                Column column = (Column) child;
                String styleClass = column.getStyleClass();
                styleClass = styleClass == null ? columnClass : columnClass == null ? styleClass : (styleClass + " " + columnClass);
                
                writer.startElement("td", null);
                writer.writeAttribute("role", columnRole, null);
                
                if(column.getStyle() != null) writer.writeAttribute("style", column.getStyle(), null);
                if(styleClass != null) writer.writeAttribute("class", styleClass, null);
                if(column.getColspan() > 1) writer.writeAttribute("colspan", column.getColspan(), null);
                if(column.getRowspan() > 1) writer.writeAttribute("rowspan", column.getRowspan(), null);
                
                column.encodeAll(context);
                
                writer.endElement("td");
            }
         else{
                System.out.println("ELSE");
         }
        }
        
        writer.endElement("tr");
    }
    
    public void encodeFacet(FacesContext context, PanelGrid grid, int columns, String facet, String tag, String styleClass) throws IOException {
        UIComponent component = grid.getFacet(facet);
        
        if(component != null && component.isRendered()) {
            ResponseWriter writer = context.getResponseWriter();
            writer.startElement(tag, null);
            writer.writeAttribute("class", styleClass, null);
            
            if(columns > 0) {
                writer.startElement("tr", null);
                writer.writeAttribute("class", "ui-widget-header", null);
                writer.writeAttribute("role", "row", null);

                writer.startElement("td", null);
                writer.writeAttribute("colspan", columns, null);
                writer.writeAttribute("role", "columnheader", null);
                
                component.encodeAll(context);
                
                writer.endElement("td");
                writer.endElement("tr");
            }
            else {
                if(component instanceof Row && component.isRendered()) {
                    encodeRow(context, (Row) component, "columnheader", "ui-widget-header", "ui-widget-header");
                }
                else if(component instanceof UIPanel && component.isRendered()){
                    for (UIComponent row : component.getChildren()) {
                        if(row instanceof Row && row.isRendered()) {
                            encodeRow(context, (Row) row, "columnheader", "ui-widget-header", "ui-widget-header");
                        }
                    }
                }
            }
            
            writer.endElement(tag);
        }
    }
    
    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

      private int[] getCols(String[] tokens) {
        int[] cols = new int[tokens.length];
        for (int i=0;i<tokens.length;i++) {
            cols[i] = Integer.parseInt(tokens[i]);
        }
        return cols;
    }
    private int getBiggerCol(int[]cols){
        if(cols == null){
            return 0;
        }
        int maxCol = 0;
        for (int i  : cols) {
            if(i > maxCol){
                maxCol = i;
            }
        }
        return maxCol;
    }
}