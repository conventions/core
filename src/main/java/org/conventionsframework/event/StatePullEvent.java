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

package org.conventionsframework.event;

import org.conventionsframework.bean.StateController;
import org.conventionsframework.model.StateItem;
import java.io.Serializable;

/**
 *
 * @author Rafael M. Pestano Ago 08, 2011 7:28:32 PM
 * 
 * 
 * Event of pulling an item from the <code>StateController</code>
 * @see StateController#stateItens
 */
public class StatePullEvent  implements Serializable {
    
    private StateItem stackItem;

    public StatePullEvent() {
    }

    public StatePullEvent(StateItem stackItem) {
        this.stackItem = stackItem;
    }

    public StateItem getStackItem() {
        return stackItem;
    }

    public void setStackItem(StateItem stackItem) {
        this.stackItem = stackItem;
    }

    
}
