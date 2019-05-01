package com.enewschamp.audit.domain;

import java.util.List;

import org.javers.core.Javers;
import org.javers.core.diff.Change;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

	private final Javers javers;

	@Autowired
    public AuditService(Javers javers) {
        this.javers = javers;
    }

	public String getEntityAudit(Object entityInstance) {
		QueryBuilder jqlQuery = QueryBuilder.byInstance(entityInstance).withChildValueObjects();
		return queryChanges(jqlQuery);
	}

	private String queryChanges(QueryBuilder jqlQuery) {
		List<Change> changes = javers.findChanges(jqlQuery.build());
		return javers.getJsonConverter().toJson(changes);
	}

}
