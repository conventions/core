package org.conventionsframework.producer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@RequestScoped
public class DefaultEntityManagerProducer {
	
	@PersistenceContext
	EntityManager em;

	@Produces
	@RequestScoped
	public EntityManager produce() {
		return em;
	}
	
	
	

}
