package com.enewschamp.publication.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.common.PublicationGroupStatusType;
import com.enewschamp.publication.domain.common.PublicationStatusType;
import com.enewschamp.publication.domain.entity.Publication;
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
	
	@Autowired
	private PublicationService publicationService;
	
	public PublicationGroup create(PublicationGroup publicationGroup) {
		//deriveStatus(publicationGroup);
		return repository.save(publicationGroup);
	}
	
	public PublicationGroup update(PublicationGroup publicationGroup) {
		Long publicationGroupId = publicationGroup.getPublicationGroupId();
		PublicationGroup existingEntity = load(publicationGroupId);
		modelMapper.map(publicationGroup, existingEntity);
		return repository.save(existingEntity);
	}
	
	public PublicationGroup patch(PublicationGroup publicationGroup) {
		Long publicationGroupId = publicationGroup.getPublicationGroupId();
		PublicationGroup existingEntity = load(publicationGroupId);
		modelMapperForPatch.map(publicationGroup, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long publicationGroupId) {
		repository.deleteById(publicationGroupId);
	}
	
	public PublicationGroup load(Long publicationGroupId) {
		Optional<PublicationGroup> existingEntity = repository.findById(publicationGroupId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.PUBLICATION_GRP_NOT_FOUND);
		}
	}
	
	public PublicationGroup get(Long publicationGroupId) {
		Optional<PublicationGroup> existingEntity = repository.findById(publicationGroupId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}
	
	public String getAudit(Long publicationGroupId) {
		PublicationGroup publicationGroup = new PublicationGroup();
		publicationGroup.setPublicationGroupId(publicationGroupId);
		return auditService.getEntityAudit(publicationGroup);
	}
	
	public PublicationGroupStatusType deriveStatus(PublicationGroup articleGroup) {
		PublicationGroupStatusType newStatus = deriveStatus(articleGroup.getPublications());
		//articleGroup.setStatus(newStatus);
		return newStatus;
	}
	
	private PublicationGroupStatusType deriveStatus(List<Publication> publications) {
		PublicationGroupStatusType newStatus = null;
		if(publications != null) {
			for(Publication publication: publications) {
				PublicationStatusType articleStatus = publicationService.derivePublicationStatus(publication);
				if(articleStatus != null) {
					PublicationGroupStatusType status = PublicationGroupStatusType.fromPublicationStatus(articleStatus);
					if(status.equals(PublicationGroupStatusType.WIP)) {
						newStatus = status;
						break;
					}
					if(newStatus == null) {
						newStatus = status;
						continue;
					}
					if(status.getOrder() < newStatus.getOrder()) {
						newStatus = status;
					}
				}
			}
		}
		return newStatus;
	}
	
}
