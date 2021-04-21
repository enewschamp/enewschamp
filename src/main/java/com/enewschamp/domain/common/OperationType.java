package com.enewschamp.domain.common;

import org.javers.core.metamodel.object.SnapshotType;

public enum OperationType {
	Add, Remove, Modify;

	public static OperationType fromSnapShotType(SnapshotType snapshotTytpe) {
		OperationType type = null;
		if (snapshotTytpe.equals(SnapshotType.INITIAL)) {
			type = OperationType.Add;
		} else if (snapshotTytpe.equals(SnapshotType.UPDATE)) {
			type = OperationType.Modify;
		} else if (snapshotTytpe.equals(SnapshotType.TERMINAL)) {
			type = OperationType.Remove;
		}
		return type;
	}
}
