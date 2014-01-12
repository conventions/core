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
package org.conventionsframework.qualifier;

import javax.enterprise.util.Nonbinding;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD, METHOD, PARAMETER})
public @interface BeanState {


    /**
     * @return outcome to return when clicking in a stateItem,
     * use "" to return to the same outcome
     */
    @Nonbinding String outcome() default "";

    /**
     * @return breadCrumb title for this state
     */
    @Nonbinding String title() default "";

    /**
     *
     * @return breadCrumb link label
     */
    @Nonbinding String value();

    @Nonbinding String beanState();

    /**
     * @return name of the action to be called when
     * clicking in an item, use in the form of #{bean.method}
     * with String return type,
     * use "" to call no method
     * <p/>
     * NOTE: when using callback the 'outcome' attribute will
     * be ignore so if you want to navigate to another outcome after the callback
     * specify that in the return of the callback method
     */
    @Nonbinding String callback() default "";

    @Nonbinding boolean ajax() default true;

    @Nonbinding boolean global() default true;

    @Nonbinding boolean resetValues() default false;

    /**
     *
     * add id param to url using entity.getId() as value
     */
    @Nonbinding boolean addEntityIdParam() default false;

    /**
     * @return id of component to update on ajax call
     * use only in ajaxifyed stateItens
     */
    @Nonbinding String update() default "@none";

    @Nonbinding boolean immediate() default true;

    String oncomplete() default "";
}
