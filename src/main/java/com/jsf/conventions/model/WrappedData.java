/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jsf.conventions.model;

import java.util.List;

/**
 *  
 * @author Rafael M. Pestano Mar 21, 2011 11:50:09 PM
 */
public class WrappedData<T> {

    private List<T> data;
    private Integer rowCount;
    private boolean selected;

    public WrappedData() {
    }

    public WrappedData(List<T> data, Integer rowCount) {
        this.data = data;
        this.rowCount = rowCount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    
    public List<T> getData() {
        return data;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
    
    
}
