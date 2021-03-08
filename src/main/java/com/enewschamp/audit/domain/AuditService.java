package com.enewschamp.audit.domain;

import java.util.List;

import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.core.metamodel.object.InstanceId;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuditService {

	private final Javers javers;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	public AuditService(Javers javers) {
		this.javers = javers;
	}

	public String getEntityAudit(Object entityInstance) {
		QueryBuilder jqlQuery = QueryBuilder.byInstance(entityInstance).withChildValueObjects(true);

		return queryChanges(jqlQuery);
	}

	public String getEntityAuditByCriteria(Object entityInstance, AuditQueryCriteria queryCriteria) {
		QueryBuilder jqlQuery = QueryBuilder.byInstance(entityInstance).withChildValueObjects(true);

		if (queryCriteria.getCommitId() != null) {
			jqlQuery.withCommitId(queryCriteria.getCommitId());
		}

		if (queryCriteria.getPropertyName() != null) {
			jqlQuery.withChangedProperty(queryCriteria.getPropertyName());
		}
		jqlQuery.withNewObjectChanges(queryCriteria.isWithNewObjectChanges());

		return queryChanges(jqlQuery);
	}

	private String queryChanges(QueryBuilder jqlQuery) {
		Changes changes = javers.findChanges(jqlQuery.build());
		return javers.getJsonConverter().toJson(changes.groupByCommit());
	}

	public Changes getEntityChanges(Object entityInstance) {
		QueryBuilder jqlQuery = QueryBuilder.byInstance(entityInstance).withChildValueObjects(true);
		return javers.findChanges(jqlQuery.build());
	}

	public Changes getEntityChangesByCriteria(Object entityInstance, AuditQueryCriteria queryCriteria) {
		QueryBuilder jqlQuery = null;
		if (entityInstance != null) {
			jqlQuery = QueryBuilder.byInstance(entityInstance).withChildValueObjects(true);
		} else {
			Class clazz = null;
			try {
				clazz = Class.forName(queryCriteria.getObjectName());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			jqlQuery = QueryBuilder.byClass(clazz).withChildValueObjects(true);
		}
		if (queryCriteria != null) {
			if (queryCriteria.getCommitId() != null) {
				jqlQuery.withCommitId(queryCriteria.getCommitId());
			}
			if (queryCriteria.getSnapshotType() != null) {
				jqlQuery.withSnapshotType(queryCriteria.getSnapshotType());
			}
			jqlQuery.withNewObjectChanges(queryCriteria.isWithNewObjectChanges());
			if (queryCriteria.getVersion() != null) {
				jqlQuery.withVersion(queryCriteria.getVersion());
			}
			if (queryCriteria.getPropertyName() != null) {
				jqlQuery.withChangedProperty(queryCriteria.getPropertyName());
			}
		}

		return javers.findChanges(jqlQuery.build());
	}

	public List<CdoSnapshot> getEntitySnapshots(Object entityInstance) {
		QueryBuilder jqlQuery = QueryBuilder.byInstance(entityInstance).withChildValueObjects(true);
		return javers.findSnapshots(jqlQuery.build());
	}

	public CdoSnapshot getEntitySnapshot(InstanceId instanceId, AuditQueryCriteria snapshotQuery) {

		QueryBuilder jqlQuery = null;
		jqlQuery = QueryBuilder.byInstanceId(instanceId.getCdoId(), instanceId.getTypeName());

		if (snapshotQuery.getCommitId() != null) {
			jqlQuery = jqlQuery.withCommitId(snapshotQuery.getCommitId());
		}
		if (snapshotQuery.getSnapshotType() != null) {
			jqlQuery = jqlQuery.withSnapshotType(snapshotQuery.getSnapshotType());
		}
		if (snapshotQuery.getVersion() != null) {
			jqlQuery = jqlQuery.withVersion(snapshotQuery.getVersion());
		}

		List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
		if (snapshots.size() > 0) {
			return snapshots.get(0);
		}
		return null;
	}

	public List<CdoSnapshot> getEntitySnapshots(InstanceId instanceId, AuditQueryCriteria snapshotQuery) {

		QueryBuilder jqlQuery = null;
		jqlQuery = QueryBuilder.byInstanceId(instanceId.getCdoId(), instanceId.getTypeName());

		if (snapshotQuery.getSnapshotType() != null) {
			jqlQuery = jqlQuery.withSnapshotType(snapshotQuery.getSnapshotType());
		}
		if (snapshotQuery.getPropertyName() != null) {
			jqlQuery.withChangedProperty(snapshotQuery.getPropertyName());
		}
		jqlQuery.withNewObjectChanges(true);
		return javers.findSnapshots(jqlQuery.build());
	}

	public Shadow<Object> getLatestShadowForEntity(InstanceId instanceId) {

		QueryBuilder jqlQuery = null;
		jqlQuery = QueryBuilder.byInstanceId(instanceId.getCdoId(), instanceId.getTypeName()).limit(5);

		List<Shadow<Object>> shadows = javers.findShadows(jqlQuery.build());

		if (shadows != null && shadows.size() > 0) {
			return shadows.get(0);
		}
		return null;
	}

	public Diff findDifference(Object instance1, Object instance2) {
		Diff diff = javers.compare(instance1, instance2);
		// System.out.println(diff.changesSummary());
		// System.out.println(diff.toString());
		return diff;
	}

}
