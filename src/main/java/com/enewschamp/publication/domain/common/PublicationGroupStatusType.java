package com.enewschamp.publication.domain.common;

public enum PublicationGroupStatusType {

	Unassigned(1),
	Assigned(2),
	WIP(3),
	ReadyToPublish(4),
	Published(5),
	Closed(6);
	
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
		PublicationGroupStatusType status = PublicationGroupStatusType.Unassigned;
		switch(publicationStatus) {
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
