package org.conventionsframework.model;

import org.primefaces.model.SortOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rmpestano on 4/25/14.
 */
public class SearchModel<T extends BaseEntity> implements Serializable{

    private int first;
    private int pageSize;
    private Map<String, Object> filter;
    private Map datatableFilter;
    private List<T> filteredValue;//datatable filteredValue attribute
    private List<T> selection;//datatable selection attribute
    private T singleSelection;//datatable single selection
    private String sortField;
    private SortOrder sortOrder;
    private T entity;

    public SearchModel() {
       filter = new HashMap<String, Object>();
    }

    public SearchModel(T entity) {
        filter = new HashMap<String, Object>();
        datatableFilter = new HashMap();
        this.entity = entity;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public List<T> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<T> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public List<T> getSelection() {
        return selection;
    }

    public void setSelection(List<T> selection) {
        this.selection = selection;
    }

    public T getSingleSelection() {
        return singleSelection;
    }

    public void setSingleSelection(T singleSelection) {
        this.singleSelection = singleSelection;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void addFilter(String key, Object value){
        getFilter().put(key,value);
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Map getDatatableFilter() {
        return datatableFilter;
    }

    public void setDatatableFilter(Map datatableFilter) {
        this.datatableFilter = datatableFilter;
    }
}
