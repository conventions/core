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
package org.conventionsframework.bean.state;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 *
 * @author Rafael M. Pestano Mar 19, 2011 5:59:22 PM
 */
public enum CrudState implements State{
    INSERT("insert"),UPDATE("update"),FIND("find");
    
    private final String stateName ;
    
    CrudState(String stateName){
        this.stateName = stateName;
    }

    @Override
    public String getStateName() {
        return this.stateName;
    }

    @Override
    public String toString() {
        return this.stateName;
    }
    
   
    
}
