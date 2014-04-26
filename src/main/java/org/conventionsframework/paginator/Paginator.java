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

import org.conventionsframework.model.BaseEntity;
import org.conventionsframework.model.SearchModel;
import org.conventionsframework.qualifier.Service;
import org.conventionsframework.util.BeanManagerController;
import org.conventionsframework.model.WrappedData;
import org.conventionsframework.service.BaseService;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
 * @author rmpestano
 */
public class Paginator<T extends BaseEntity> implements Serializable {
    private BaseService baseService;
    private SearchModel<T> searchModel;
    private boolean keepSearchInSession = true;//keep searchModel in user session
    @Inject
    private SearchModelCache searchModelCache;

    public Paginator() {
    }

    @Inject
    public void Paginator(InjectionPoint ip) {
        if (ip != null && ip.getAnnotated().isAnnotationPresent(PaginatorService.class)) {
            PaginatorService paginatorService = ip.getAnnotated().getAnnotation(PaginatorService.class);
            try {
                if (!paginatorService.value().isPrimitive()) {
                    //set paginator service by type
                    baseService = (BaseService) BeanManagerController.getBeanByType(paginatorService.value());
                } else {
                    ParameterizedType type = null;
                    try {
                        type = (ParameterizedType) ip.getType();
                        if (type != null) {
                            Type[] typeArgs = type.getActualTypeArguments();
                            if (typeArgs != null && typeArgs.length == 1) {
                                baseService = BeanManagerController.getBeanByTypeAndQualifier(BaseService.class, Service.class);
                                baseService.getDao().setPersistentClass((Class<T>) typeArgs[0]);
                            }
                        }
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("Could not initialize Paginator of " + ip.getMember().getDeclaringClass() + ".You need to provide paginatorService name or type attribute");
                    }
                }
                if (!paginatorService.entity().isPrimitive() && (getBaseService().getDao().getPersistentClass() == null || getBaseService().getDao().getPersistentClass().isPrimitive())) {
                    getBaseService().getDao().setPersistentClass(paginatorService.entity());
                }
                initDataModel();
            } catch (Exception ex) {
                Logger.getLogger(Paginator.class.getSimpleName()).log(Level.WARNING, "Conventions: problens setting paginator service " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public Paginator(BaseService service) {
        this.baseService = service;
        initDataModel();
    }

    private void initDataModel() {
        if (keepSearchInSession) {
            String searchKey = baseService.getClass().getSimpleName();
            searchModel = (SearchModel<T>) getSearchModelCache().getSearchModel(searchKey);
            if (searchModel == null) {
                searchModel = new SearchModel<T>();
                searchModelCache.addSearchModel(searchKey, searchModel);
            }
        }
        else{
            searchModel = new SearchModel<T>();
        }
       searchModel.setDataModel(new LazyDataModel<T>() {


            @Override
            public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> dataTableFilters) {
                WrappedData<T> wrappedData;
                searchModel.setFirst(first);
                searchModel.setPageSize(pageSize);
                searchModel.setSortField(sortField);
                searchModel.setSortOrder(sortOrder);
                searchModel.getFilter().clear();
                searchModel.getFilter().putAll(dataTableFilters);
                wrappedData = baseService.findPaginated(searchModel);
                searchModel.setRowCount(wrappedData.getRowCount());
                return wrappedData.getData();
            }
        });
    }

    public BaseService getBaseService() {
        return baseService;
    }

    public void setBaseService(BaseService baseService) {
        this.baseService = baseService;
    }

    public LazyDataModel<T> getDataModel() {
        return searchModel.getDataModel();
    }

    public void setDataModel(LazyDataModel<T> dataModel) {
       searchModel.setDataModel(dataModel);
    }


    public boolean isKeepSearchInSession() {
        return keepSearchInSession;
    }

    public void setKeepSearchInSession(boolean keepSearchInSession) {
        this.keepSearchInSession = keepSearchInSession;
    }


    public void pageListener() {
        //override to perform an action on datatable outcome event
    }

    public void sortListener() {
        //override to perform an action on datatable sort event
    }

    public void filterListener() {
        //override to perform an action on datatable filter event
    }

    public SearchModelCache getSearchModelCache() {
        if (searchModelCache == null) {//its null when Paginator is created via new operator
            searchModelCache = (SearchModelCache) BeanManagerController.getBeanByType(SearchModelCache.class);
        }
        return searchModelCache;
    }

    public T getEntity(){
        return searchModel.getEntity();
    }

    public Map<String,Object> getFilter(){
        return searchModel.getFilter();
    }

    public void setFilter(Map<String,Object> filter){
        searchModel.setFilter(filter);
    }

    public SearchModel<T> getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(SearchModel<T> searchModel) {
        this.searchModel = searchModel;
    }

    public int getRowCount(){
        return searchModel.getRowCount();
    }
}
