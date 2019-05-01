package com.enewschamp.audit.domain;

import org.javers.core.Changes;
import org.javers.core.Javers;
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
		Changes changes = javers.findChanges(jqlQuery.build());
		changes.groupByCommit();
		return javers.getJsonConverter().toJson(changes.groupByCommit());
	}

}
