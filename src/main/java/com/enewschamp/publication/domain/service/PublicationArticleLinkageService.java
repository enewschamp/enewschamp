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
		PublicationArticleLinkage existingPublicationArticleLinkage = get(publicationArticleLinkage.getId());
		modelMapper.map(publicationArticleLinkage, existingPublicationArticleLinkage);
		return repository.save(existingPublicationArticleLinkage);
	}
	
	public PublicationArticleLinkage patch(PublicationArticleLinkage publicationArticleLinkage) {
		PublicationArticleLinkage existingEntity = get(publicationArticleLinkage.getId());
		modelMapperForPatch.map(publicationArticleLinkage, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long linkageId) {
		repository.deleteById(linkageId);
	}
	
	public PublicationArticleLinkage get(Long linkageId) {
		Optional<PublicationArticleLinkage> existingEntity = repository.findById(linkageId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.PUBLICATION_ART_LINK_NOT_FOUND, "Publication Article Linkage not found!");
		}
	}
	
	public String getAudit(Long linkageId) {
		PublicationArticleLinkage publicationArticleLinkage = new PublicationArticleLinkage();
		publicationArticleLinkage.setId(linkageId);
		return auditService.getEntityAudit(publicationArticleLinkage);
	}
	
}
