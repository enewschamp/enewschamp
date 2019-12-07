package com.enewschamp.publication.domain.common;

public enum PublicationStatusType {

	Initial,
	Assigned,
	Rework,
	ReadyToPublish,
	Published,
	Closed;
	
	public static PublicationStatusType fromValue(String status) {
		for(PublicationStatusType statusType : PublicationStatusType.values()) {
			if(statusType.toString().equals(status)) {
				return statusType;
			}
		}
		return null;
	}
}
