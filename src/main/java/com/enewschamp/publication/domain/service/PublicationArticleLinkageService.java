package com.enewschamp.publication.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.publication.domain.entity.PublicationArticleLinkage;
import com.enewschamp.publication.domain.entity.PublicationArticleLinkageKey;

@Service
public class PublicationArticleLinkageService {

	@Autowired
	PublicationArticleLinkageRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	public PublicationArticleLinkage create(PublicationArticleLinkage publicationArticleLinkage) {
		return repository.save(publicationArticleLinkage);
	}
	
	public PublicationArticleLinkage update(PublicationArticleLinkage publicationArticleLinkage) {
		PublicationArticleLinkageKey publicationArticleLinkageId = publicationArticleLinkage.getPublicationArticleLinkageKey();
		PublicationArticleLinkage existingPublicationArticleLinkage = get(publicationArticleLinkageId);
		modelMapper.map(publicationArticleLinkage, existingPublicationArticleLinkage);
		return repository.save(existingPublicationArticleLinkage);
	}
	
	public PublicationArticleLinkage patch(PublicationArticleLinkage publicationArticleLinkage) {
		PublicationArticleLinkageKey publicationArticleLinkageKey = publicationArticleLinkage.getPublicationArticleLinkageKey();
		PublicationArticleLinkage existingEntity = get(publicationArticleLinkageKey);
		modelMapperForPatch.map(publicationArticleLinkage, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(PublicationArticleLinkageKey publicationArticleLinkageKey) {
		repository.deleteById(publicationArticleLinkageKey);
	}
	
	public PublicationArticleLinkage get(PublicationArticleLinkageKey publicationArticleLinkageKey) {
		Optional<PublicationArticleLinkage> existingEntity = repository.findById(publicationArticleLinkageKey);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.PUBLICATION_ART_LINK_NOT_FOUND, "Publication Article Linkage not found!");
		}
	}
	
	public String getAudit(PublicationArticleLinkageKey publicationArticleLinkageKey) {
		PublicationArticleLinkage publicationArticleLinkage = new PublicationArticleLinkage();
		publicationArticleLinkage.setPublicationArticleLinkageKey(publicationArticleLinkageKey);
		return auditService.getEntityAudit(publicationArticleLinkage);
	}
	
}
