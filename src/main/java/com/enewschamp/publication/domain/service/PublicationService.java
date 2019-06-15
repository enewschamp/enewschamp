package com.enewschamp.publication.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.page.data.PropertyAuditData;
import com.enewschamp.audit.domain.AuditBuilder;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.StatusTransitionDTO;
import com.enewschamp.domain.common.StatusTransitionHandler;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.domain.common.PublicationStatusType;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.entity.PublicationArticleLinkage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PublicationService {

	@Autowired
	PublicationRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private EnewschampApplicationProperties appConfig;
	
	@Autowired
	private StatusTransitionHandler statusTransitionHandler;
	
	public Publication create(Publication publication) {
		derivePublicationStatus(publication);
		return repository.save(publication);
	}
	
	public Publication update(Publication publication) {
		derivePublicationStatus(publication);
		Long publicationId = publication.getPublicationId();
		Publication existingEntity = load(publicationId);
		modelMapper.map(publication, existingEntity);
		return repository.save(existingEntity);
	}
	
	public Publication patch(Publication publication) {
		derivePublicationStatus(publication);
		Long publicationId = publication.getPublicationId();
		Publication existingEntity = load(publicationId);
		modelMapperForPatch.map(publication, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long publicationId) {
		repository.deleteById(publicationId);
	}
	
	public Publication load(Long publicationId) {
		Optional<Publication> existingEntity = repository.findById(publicationId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.PUBLICATION_NOT_FOUND);
		}
	}
	
	public Publication get(Long publicationId) {
		Optional<Publication> existingEntity = repository.findById(publicationId);
		if(existingEntity.isPresent()) {
			return existingEntity.get();
		}
		return null;
	}
	
	public String getAudit(Long publicationId) {
		Publication publication = new Publication();
		publication.setPublicationId(publicationId);
		
		AuditBuilder auditBuilder = AuditBuilder.getInstance(auditService, objectMapper, appConfig).forParentObject(publication);
		
		// Fetch article linkage changes
		publication = load(publicationId);
		for(PublicationArticleLinkage articleLinkage: publication.getArticleLinkages()) {
			auditBuilder.forChildObject(articleLinkage);
		}
		return auditBuilder.build();
	}
	
	public PublicationStatusType derivePublicationStatus(Publication publication) {
		PublicationStatusType status = PublicationStatusType.Unassigned;
		if(publication.getCurrentAction() != null) {
			PublicationStatusType existingStatus = repository.getCurrentStatus(publication.getPublicationId());
			existingStatus = existingStatus == null ? PublicationStatusType.Unassigned : existingStatus;
			StatusTransitionDTO transition = new StatusTransitionDTO(Publication.class.getSimpleName(), 
																	 String.valueOf(publication.getPublicationId()),
																	 existingStatus.toString(),
																     publication.getCurrentAction().toString(), 
																     null);
			
			String nextStatus = statusTransitionHandler.findNextStatus(transition);
			if(nextStatus.equals(StatusTransitionDTO.REVERSE_STATE)) {
				PublicationStatusType previousStatus = repository.getPreviousStatus(publication.getPublicationId());
				nextStatus = previousStatus.toString();
			}
			
			status = PublicationStatusType.fromValue(nextStatus);
		}
		publication.setStatus(status);
		return status;
	}
	
	public Publication assignEditor(Long publicationId, String editorId) {
		
		Publication publication = get(publicationId);
		if(publication != null) {
			publication.setEditorId(editorId);
			repository.save(publication);
		}
		return publication;
	}
	
	public Publication assignPublisher(Long publicationId, String publisherId) {
		
		Publication publication = get(publicationId);
		if(publication != null) {
			publication.setPublisherId(publisherId);
			repository.save(publication);
		}
		return publication;
	}
	
	public List<PropertyAuditData> getPreviousComments(Long publicationId) {
		Publication publication = new Publication();
		publication.setPublicationId(publicationId);
		
		AuditBuilder auditBuilder = AuditBuilder.getInstance(auditService, objectMapper, appConfig).forParentObject(publication);
		auditBuilder.forProperty("comments");
		
		return auditBuilder.buildPropertyAudit();
	}
	
}
