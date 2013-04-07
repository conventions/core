/*
 * Copyright 2013 Rafael M. Pestano.
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
package org.conventionsframework.paginator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Rafael M. Pestano - Apr 7, 2013 10:59:19 AM
 *
 * gather all paginator filters in a sessionMap where the key is the paginator
 * service
 */
@SessionScoped
public class SessionFilter implements Serializable {

    private Map<String, Map<String, Object>> sessionFilter = new HashMap<String, Map<String, Object>>();

    public void addFilter(String key, Map<String, Object> filter) {
        sessionFilter.put(key, filter);
    }
    
    public void clearFilter(String key) {
        if (sessionFilter.containsKey(key)) {
            sessionFilter.get(key).clear();
        }
    }
    
    public Map<String, Object> getFilter(String key){
        if(sessionFilter.containsKey(key)){
            return sessionFilter.get(key);
        }
        else {
            return null;
        }
    }
    
}
