package com.enewschamp.app.admin.reconciliation.dto;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Service;

@Service
public class ReconcilationServiceImpl {
	@PersistenceContext
	private EntityManager entityManager;

	public void getDiscripancies() {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("status_json");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.OUT);

		// Pass the parameter values
		query.setParameter(1, 123);
		query.setParameter(2, 456);

		// Execute query
		query.execute();

		// Get output parameters
		String outMessage = (String) query.getOutputParameterValue(3);
		System.out.println(outMessage);

	}

}
