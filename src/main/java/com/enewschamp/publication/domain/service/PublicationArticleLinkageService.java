package com.enewschamp.publication.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
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
		PublicationArticleLinkage existingPublicationArticleLinkage = get(publicationArticleLinkage.getLinkageId());
		modelMapper.map(publicationArticleLinkage, existingPublicationArticleLinkage);
		return repository.save(existingPublicationArticleLinkage);
	}
	
	public PublicationArticleLinkage patch(PublicationArticleLinkage publicationArticleLinkage) {
		PublicationArticleLinkage existingEntity = get(publicationArticleLinkage.getLinkageId());
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
			throw new BusinessException(ErrorCodes.PUBLICATION_ART_LINK_NOT_FOUND);
		}
	}
	
	public String getAudit(Long linkageId) {
		PublicationArticleLinkage publicationArticleLinkage = new PublicationArticleLinkage();
		publicationArticleLinkage.setLinkageId(linkageId);
		return auditService.getEntityAudit(publicationArticleLinkage);
	}
	
}
