/*
 * Copyright 2011-2013 Conventions Framework.  
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
package org.conventionsframework.paginator;

import org.conventionsframework.util.*;
import org.conventionsframework.model.WrappedData;
import org.conventionsframework.service.BaseService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import org.conventionsframework.model.LazyDataModel;
import org.conventionsframework.qualifier.PaginatorService;
import org.primefaces.model.SortOrder;

/**
 *
 * @author rmpestano 
 */

public class Paginator<T> implements Serializable{
     private LazyDataModel<T> dataModel;
     private BaseService baseService;
     private Integer rowCount;
     private Map<String,Object> filter = new HashMap<String, Object>();;
     private List<T> filteredValue;//datatable filteredValue attribute
     private List<T> selection;//datatable selection attribute
     private T singleSelection;//datatable single selection
     
    public Paginator() {
    }
     
    @Inject 
    public void Paginator(InjectionPoint ip){
        if(ip != null && ip.getAnnotated().isAnnotationPresent(PaginatorService.class)){
            PaginatorService paginatorService = ip.getAnnotated().getAnnotation(PaginatorService.class);
            try{
                if(!paginatorService.type().isPrimitive()){
                    //set paginator service by type
                    baseService = (BaseService)BeanManagerController.getBeanByType(paginatorService.type());
                 }
                else if(!"".equals(paginatorService.name()) ){
                    //set paginator service by name
                    baseService = (BaseService)BeanManagerController.getBeanByName(paginatorService.name());
                }
                else{
                    throw new IllegalArgumentException("Could not initialize Paginator of " + ip.getMember().getDeclaringClass() +".You need to provide paginatorService name or type attribute");
                }
                 if(!paginatorService.entity().isPrimitive() && (getBaseService().getPersistentClass() == null || getBaseService().getPersistentClass().isPrimitive())){
                    getBaseService().getDao().setPersistentClass(paginatorService.entity());
                }
                 initDataModel();
            }catch(Exception ex){
                Logger.getLogger(Paginator.class.getSimpleName()).log(Level.WARNING, "Conventions: problens setting paginator service "+ex.getMessage());
                ex.printStackTrace();
            }
        }
    }   
     
    public Paginator(BaseService service) {
        this.baseService = service;
        initDataModel();
    }
    
    private void initDataModel(){
        dataModel = new LazyDataModel<T>() {
                @Override
                public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> dataTableFilters) {
                    WrappedData<T> wrappedData;
                    wrappedData = baseService.findPaginated(first, pageSize, sortField, sortOrder, dataTableFilters, filter);
                    rowCount = wrappedData.getRowCount();
                    this.setRowCount(rowCount);//datatable rowCount
                    return wrappedData.getData();
                }
            };
    }

    public BaseService getBaseService() {
        return baseService;
    }

    public void setBaseService(BaseService baseService) {
        this.baseService = baseService;
    }

    public LazyDataModel<T> getDataModel() {
        return dataModel;
    }

    public void setDataModel(LazyDataModel<T> dataModel) {
        this.dataModel = dataModel;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filters) {
        this.filter = filters;
    }

    public T getSingleSelection() {
        return singleSelection;
    }

    public void setSingleSelection(T singleSelection) {
        this.singleSelection = singleSelection;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
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
    
    public void pageListener(){
        //override to perform an action on datatable page event
    }
    
    public void sortListener(){
        //override to perform an action on datatable sort event
    }
    
    public void filterListener(){
        //override to perform an action on datatable filter event
    }

   
}
