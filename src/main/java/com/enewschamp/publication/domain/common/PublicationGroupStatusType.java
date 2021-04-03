package com.enewschamp.publication.domain.common;

public enum PublicationGroupStatusType {

	Assigned(1), WIP(2), ReadyToPublish(3), Published(4), Closed(5);

	private int order;

	private PublicationGroupStatusType() {

	}

	private PublicationGroupStatusType(int order) {
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}

	public static PublicationGroupStatusType fromPublicationStatus(PublicationStatusType publicationStatus) {
		PublicationGroupStatusType status = PublicationGroupStatusType.Assigned;
		switch (publicationStatus) {
		case Assigned:
			status = PublicationGroupStatusType.Assigned;
			break;
		case Rework:
			status = PublicationGroupStatusType.WIP;
			break;
		case ReadyToPublish:
			status = PublicationGroupStatusType.ReadyToPublish;
			break;
		case Published:
			status = PublicationGroupStatusType.Published;
			break;
		case Closed:
			status = PublicationGroupStatusType.Closed;
			break;
		}
		return status;
	}
}
