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
package org.conventionsframework.converter;

import org.conventionsframework.model.BaseEntity;
import org.conventionsframework.service.BaseService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Rafael M. Pestano
 */
public abstract class BaseConverter implements Converter {

    BaseService<?> baseService;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value == null || "".equals(value)) {
            return null;
        }
        try {
            Long id = new Long(value);
            return baseService.getDao().get(id);
        } catch (ClassCastException ex) {
            ex.printStackTrace();
            return value;
        } catch (NumberFormatException ne) {
            ne.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o != null && o instanceof BaseEntity && ((BaseEntity) o).getId() != null) {
                return ((BaseEntity) o).getId().toString();
        } else {
            return "";
        }
    }


    public <T extends BaseEntity> void setBaseService(BaseService<T> baseService) {
        this.baseService = baseService;
    }
}
