/*
 * Copyright 2012 rmpestano.
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
package com.jsf.conventions.util;

import com.jsf.conventions.qualifier.BeanState;
import com.jsf.conventions.qualifier.BeanStates;
import com.jsf.conventions.qualifier.Service;
import java.lang.annotation.Annotation;

/**
 *
 * @author rmpestano
 */
public class AnnotationUtils {

    public static Service findServiceAnnotation(Class c){
         for (Annotation annotation : c.getAnnotations()) {
            if (annotation instanceof Service) {
                return (Service) annotation;
            }
        }
        return null;
    }
    
    public static BeanState findStateAnnotation(Class c){
         for (Annotation annotation : c.getAnnotations()) {
            if (annotation instanceof BeanState) {
                return (BeanState) annotation;
            }
        }
        return null;
    }
    public static BeanStates findStatesAnnotation(Class c){
         for (Annotation annotation : c.getAnnotations()) {
            if (annotation instanceof BeanStates) {
                return (BeanStates) annotation;
            }
        }
        return null;
    }
    
    
}
