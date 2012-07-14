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
package com.jsf.conventions.entitymanager.provider;

import com.jsf.conventions.qualifier.ConventionsEntityManager;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 * 
 * @author Rafael M. Pestano jun 19 11:12 PM
 */

@Stateless  
@ConventionsEntityManager(type= Type.TRANSACTION)
public class TransactionEntityManagerProvider implements EntityManagerProvider{
    
     @PersistenceContext(type = PersistenceContextType.TRANSACTION)
     private EntityManager entityManager;
     
     public EntityManager getEntityManager() {
	    return entityManager;
	}
    
}
