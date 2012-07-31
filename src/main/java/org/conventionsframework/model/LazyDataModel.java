/*
 * Copyright 2012 Rafael M. Pestano.
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
package org.conventionsframework.model;

/**
 *
 * Should be removed when http://code.google.com/p/primefaces/issues/detail?id=1544 is fixed
 */
public abstract class LazyDataModel<T> extends org.primefaces.model.LazyDataModel<T> {
    
    @Override
    public void setRowIndex(int rowIndex) {
        if (rowIndex == -1 || getPageSize() == 0) {
           super.setRowIndex(-1);
        }
        else {
              super.setRowIndex(rowIndex % getPageSize());
        }
    }
}