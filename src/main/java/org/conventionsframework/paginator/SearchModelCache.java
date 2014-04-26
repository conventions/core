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

import org.conventionsframework.model.SearchModel;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rafael M. Pestano - Apr 7, 2013 10:59:19 AM
 *
 * gather all paginator filters in a sessionMap where the key is the paginator
 * service
 */
@SessionScoped
public class SearchModelCache implements Serializable {

    private Map<String, SearchModel<?>> searchCache = new HashMap<String, SearchModel<?>>();

    public void addSearchModel(String key, SearchModel<?> searchModel) {
        searchCache.put(key, searchModel);
    }
    
    public void resetSearchModel(String key) {
        if (searchCache.containsKey(key)) {
            searchCache.put(key, null);
        }
    }
    
    public SearchModel<?> getSearchModel(String key){
        if(searchCache.containsKey(key)){
            return searchCache.get(key);
        }
        else {
            return null;
        }
    }

}
