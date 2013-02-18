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
package org.conventionsframework.model;

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
