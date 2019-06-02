package com.enewschamp.publication.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.PublicationGroup;

@Service
public class PublicationGroupService {

	@Autowired
	PublicationGroupRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	public PublicationGroup create(PublicationGroup publicationGroup) {
		return repository.save(publicationGroup);
	}
	
	public PublicationGroup update(PublicationGroup publicationGroup) {
		Long publicationGroupId = publicationGroup.getPublicationGroupId();
		PublicationGroup existingEntity = get(publicationGroupId);
		modelMapper.map(publicationGroup, existingEntity);
		return repository.save(existingEntity);
	}
	
	public PublicationGroup patch(PublicationGroup publicationGroup) {
		Long publicationGroupId = publicationGroup.getPublicationGroupId();
		PublicationGroup existingEntity = get(publicationGroupId);
		modelMapperForPatch.map(publicationGroup, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long publicationGroupId) {
		repository.deleteById(publicationGroupId);
	}
	
	public PublicationGroup get(Long publicationGroupId) {
		Optional<PublicationGroup> existingEntity = repository.findById(publicationGroupId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.PUBLICATION_GRP_NOT_FOUND);
		}
	}
	
	public String getAudit(Long publicationGroupId) {
		PublicationGroup publicationGroup = new PublicationGroup();
		publicationGroup.setPublicationGroupId(publicationGroupId);
		return auditService.getEntityAudit(publicationGroup);
	}
	
}
