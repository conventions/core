/*
 * Copyright 2011-2014 Conventions Framework.
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
public class PaginationResult<T> {

    private List<T> page;//database page returned by pagination
    private Integer rowCount;//rowCount rows returned by pagination

    public PaginationResult() {
    }

    public PaginationResult(List<T> data, Integer rowCount) {
        this.page = data;
        this.rowCount = rowCount;
    }


    public List<T> getPage() {
        return page;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setPage(List<T> page) {
        this.page = page;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
    
    
}
