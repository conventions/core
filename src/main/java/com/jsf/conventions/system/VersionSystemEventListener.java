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
package com.jsf.conventions.system;

import com.jsf.conventions.util.VersionUtils;
import com.jsf.conventions.util.VersionUtils.Version;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.inject.Inject;

/**
 *
 * @author rmpestano
 */
public class VersionSystemEventListener implements SystemEventListener {
    
    private static final Logger LOGGER = Logger.getLogger(VersionSystemEventListener.class.getName());
    
     
    @Override
    public void processEvent(SystemEvent se) throws AbortProcessingException {
         LOGGER.log(Level.INFO, "Running Conventions {0}",  VersionUtils.INSTANCE.getCoreVersion().getVersion());
    }

    @Override
    public boolean isListenerForSource(Object o) {
        return true;
    }
    
}
