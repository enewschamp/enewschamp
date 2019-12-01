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
import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.app.dto.PublicationGroupDTO;
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
		deriveStatus(publicationGroup);
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
			throw new BusinessException(ErrorCodes.PUBLICATION_GRP_NOT_FOUND, String.valueOf(publicationGroupId));
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
		articleGroup.setStatus(newStatus);
		return newStatus;
	}

	private PublicationGroupStatusType deriveStatus(List<Publication> publications) {
		PublicationGroupStatusType newStatus = null;
		if (publications != null) {
			for (Publication publication : publications) {
				PublicationStatusType articleStatus = publicationService.derivePublicationStatus(publication, false);
				if (articleStatus != null) {
					PublicationGroupStatusType status = PublicationGroupStatusType.fromPublicationStatus(articleStatus);
					if (status.equals(PublicationGroupStatusType.WIP)) {
						newStatus = status;
						break;
					}
					if (newStatus == null) {
						newStatus = status;
						continue;
					}
					if (status.getOrder() < newStatus.getOrder()) {
						newStatus = status;
					}
				}
			}
		}
		return newStatus;
	}

	public PublicationGroupDTO assignPublisher(Long publicationGroupId, String publisherId) {
		PublicationGroup publicationGroup = load(publicationGroupId);
		publicationGroup.setPublisherId(publisherId);
		PublicationGroupDTO publicationGroupDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);
		List<PublicationDTO> publicationList = publicationService.assignPublisher(publicationGroupId, publisherId);
		publicationGroup.setStatus(PublicationGroupStatusType.Assigned);
		repository.save(publicationGroup);
		publicationGroupDTO.setPublications(publicationList);
		return publicationGroupDTO;
	}

	public PublicationGroupDTO assignEditor(Long pubicationGroupId, String editorId) {
		PublicationGroup publicationGroup = load(pubicationGroupId);
		publicationGroup.setEditorId(editorId);
		PublicationGroupDTO publicationGroupDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);
		List<PublicationDTO> publicationList = publicationService.assignEditor(pubicationGroupId, editorId);
		repository.save(publicationGroup);
		publicationGroupDTO.setPublications(publicationList);
		return publicationGroupDTO;
	}

	public PublicationGroupDTO closePublicationGroup(Long pubicationGroupId, String userId) {
		PublicationGroup publicationGroup = load(pubicationGroupId);
		List<PublicationDTO> publicationList = publicationService.closeArticles(pubicationGroupId, userId);
		publicationGroup.setStatus(PublicationGroupStatusType.Closed);
		publicationGroup = repository.save(publicationGroup);
		PublicationGroupDTO publicationGroupDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);
		publicationGroupDTO.setPublications(publicationList);
		return publicationGroupDTO;
	}

	public PublicationGroupDTO reinstatePublicationGroup(Long pubicationGroupId, String userId) {
		PublicationGroup publicationGroup = load(pubicationGroupId);
		List<PublicationDTO> publicationList = publicationService.reinstateArticles(pubicationGroupId, userId);
		publicationGroup.setStatus(PublicationGroupStatusType.Unassigned);
		publicationGroup = repository.save(publicationGroup);
		PublicationGroupDTO publicationGroupDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);
		publicationGroupDTO.setPublications(publicationList);
		return publicationGroupDTO;
	}

}
