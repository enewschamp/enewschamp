package com.enewschamp.publication.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditBuilder;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
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
	
	public Publication create(Publication publication) {
		return repository.save(publication);
	}
	
	public Publication update(Publication publication) {
		Long publicationId = publication.getPublicationId();
		Publication existingEntity = load(publicationId);
		modelMapper.map(publication, existingEntity);
		return repository.save(existingEntity);
	}
	
	public Publication patch(Publication publication) {
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
	
}
