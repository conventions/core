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
package com.jsf.conventions.util;

import com.jsf.conventions.model.WrappedData;
import com.jsf.conventions.qualifier.Service;
import com.jsf.conventions.service.BaseService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
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

    public Paginator() {
    }
     
    @Inject 
    public void Paginator(InjectionPoint ip){
        if(ip != null && ip.getAnnotated().isAnnotationPresent(Service.class)){
            Service service = ip.getAnnotated().getAnnotation(Service.class);
            try{
                 baseService = (BaseService)BeanManagerController.getBeanByName(service.name());
                 if(!service.entity().isPrimitive() && getBaseService().getPersistentClass() == null || getBaseService().getPersistentClass().isPrimitive()){
                    getBaseService().getDao().setPersistentClass(service.entity());
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
                    wrappedData = baseService.configFindPaginated(first, pageSize, sortField, sortOrder, dataTableFilters, filter);
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

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
     
    
}
