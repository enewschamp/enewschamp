package com.enewschamp.publication.domain.service;

import java.time.LocalDate;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.publication.domain.entity.Publication;

public class PublicationBusinessPolicy extends AbstractBusinessPolicy {
	

	private Publication publication;
	
	public PublicationBusinessPolicy(Publication publication) {
		this.publication = publication;
	}
	
	@Override
	protected void validatePolicy() {
		validatePublicationDate(publication);
	}
	
	private void validatePublicationDate(Publication publication) {
		if(publication.getPublishDate().isBefore(LocalDate.now())) {
			addValidationError(new ValidationError(ErrorCodes.PUBLICATION_DATE_SHOULD_BE_FUTURE, "Publication.PublishDate")); 
		}
	}
		
}
