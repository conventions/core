/*
 * Copyright 2011-2012 Conventions Framework.  
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

package org.conventionsframework.facelets;

import java.util.List;

/**
 *
 * @author rmpestano Nov 19, 2011 2:10:42 AM
 */
public class Functions {

    public static String concat(String form, String string1, String string2) {
        System.out.println("return:"+form.concat(":").concat(string1).concat(":").concat(string1).concat(string2));
        return ":"+form.concat(":").concat(string1).concat(string2);
    }
    
    public static Integer size(List list){
        return list != null ? list.size() : 0;
    } 

}
