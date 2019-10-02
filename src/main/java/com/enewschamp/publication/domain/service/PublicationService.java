package com.enewschamp.publication.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.article.domain.service.NewsArticleService;
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
import com.enewschamp.publication.page.data.PublicationSearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PublicationService {

	@Autowired
	PublicationRepository repository;
	
	@Autowired
	private PublicationRepositoryCustom customRepository;
	
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
	
	@Autowired
	private NewsArticleService articleService;
	
	@Autowired
	private PublicationDailySummaryService dailySummaryService;
	
	public Publication create(Publication publication) {
		derivePublicationStatus(publication);
		publication = repository.save(publication);
		checkAndPerformPublishActions(publication);
		return publication;
	}
	
	public Publication update(Publication publication) {
		derivePublicationStatus(publication);
		Long publicationId = publication.getPublicationId();
		Publication existingEntity = load(publicationId);
		modelMapper.map(publication, existingEntity);
		checkAndPerformPublishActions(publication);
		return repository.save(existingEntity);
	}
	
	public Publication patch(Publication publication) {
		derivePublicationStatus(publication);
		Long publicationId = publication.getPublicationId();
		Publication existingEntity = load(publicationId);
		modelMapperForPatch.map(publication, existingEntity);
		checkAndPerformPublishActions(publication);
		return repository.save(existingEntity);
	}
	
	private void checkAndPerformPublishActions(Publication publication) {
		if(PublicationStatusType.Published.equals(publication.getStatus())) {
			articleService.markArticlesAsPublished(publication);
		}
		dailySummaryService.saveSummary(publication);
	}
	
	public void delete(Long publicationId) {
		repository.deleteById(publicationId);
	}
	
	public Publication load(Long publicationId) {
		Optional<Publication> existingEntity = repository.findById(publicationId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.PUBLICATION_NOT_FOUND, String.valueOf(publicationId));
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
		if(publication.getPublicationId() == null 
				|| publication.getPublicationId() == 0
				|| publication.getEditorId() != null) {
			status = PublicationStatusType.Assigned;
		} else {
			if(publication.getCurrentAction() != null && publication.getPublicationId() != null) {
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
	
	public Page<PublicationDTO> findPublications(PublicationSearchRequest searchRequest, HeaderDTO header) {
		int pageNumber = header.getPageNo();
		pageNumber = pageNumber > 0 ? (pageNumber - 1) : 0;
		Pageable pageable = PageRequest.of(pageNumber, header.getPageSize());
		return customRepository.findPublications(searchRequest, pageable);
	}
	
	public LocalDate getNextAvailablePublicationDate(LocalDate givenDate, String editionId, int readingLevel) {
		return repository.getNextAvailablePublicationDate(givenDate, editionId, readingLevel);
	}
	public LocalDate getLatestPublication(String editionId, int readingLevel) {
		return repository.getLatestPublicationDate(editionId, readingLevel);
	}
	
	public LocalDate getPreviousAvailablePublicationDate(LocalDate givenDate, String editionId, int readingLevel) {
		return repository.getPreviousAvailablePublicationDate(givenDate, editionId, readingLevel);
	}
}
