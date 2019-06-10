package com.enewschamp.audit.domain;

import org.javers.core.commit.CommitId;
import org.javers.core.metamodel.object.SnapshotType;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AuditQueryCriteria extends AbstractDTO {
	
	private static final long serialVersionUID = -7724430529197618591L;	

	private CommitId commitId;
	private SnapshotType snapshotType;
	private Long version;
	private boolean withNewObjectChanges;
	private String objectName;
	private String propertyName;

	
}
