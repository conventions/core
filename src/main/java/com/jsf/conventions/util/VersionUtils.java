/*
 * Copyright 2012 VAIO.
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

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 *
 * @author rmpestano
 */

public final class VersionUtils implements Serializable{
    
    public static final VersionUtils INSTANCE = new VersionUtils();
    
    @Produces @Named(value="conventionsVersion")
    public Version getCoreVersion(){
        try {
            return new Version(new ResourceBundle(getClass().getResourceAsStream("/com/conventions/core/bundle/conventions.properties")).getString("conventions.version"));
        } catch (IOException ex) {
            Logger.getLogger(VersionUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public class Version implements Serializable {
        
        private String version;

        public Version() {
        }

        public Version(String version) {
            this.version = version;
        }
        
        

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
        
        
    }
    
}
