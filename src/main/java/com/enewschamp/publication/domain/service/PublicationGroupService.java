package com.enewschamp.publication.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditBuilder;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.app.dto.PublicationGroupDTO;
import com.enewschamp.publication.domain.common.PublicationGroupStatusType;
import com.enewschamp.publication.domain.common.PublicationStatusType;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.entity.PublicationGroup;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PublicationGroupService {

	@Autowired
	PublicationGroupRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	private PublicationService publicationService;

	@Autowired
	private PublicationRepository publicationRepository;

	@Autowired
	private EnewschampApplicationProperties appConfig;

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

	public PublicationGroup getPublicationGroupByPublicationDate(LocalDate publicationDate) {
		Optional<PublicationGroup> existingEntity = repository.getPublicationGroupByPublicationDate(publicationDate);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public PublicationGroup load(Long publicationGroupId) {
		Optional<PublicationGroup> existingEntity = repository.findById(publicationGroupId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.PUBLICATION_GRP_NOT_FOUND,
					String.valueOf(publicationGroupId));
		}
	}

	public PublicationGroup get(Long publicationGroupId) {
		if (publicationGroupId == null) {
			return null;
		}
		Optional<PublicationGroup> existingEntity = repository.findById(publicationGroupId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.PUBLICATION_GRP_NOT_FOUND,
					String.valueOf(publicationGroupId));
		}
	}

	public String getAudit(Long publicationGroupId) {
		PublicationGroup publicationGroup = new PublicationGroup();
		publicationGroup.setPublicationGroupId(publicationGroupId);
		AuditBuilder auditBuilder = AuditBuilder.getInstance(auditService, objectMapper, appConfig)
				.forParentObject(publicationGroup);
		return auditBuilder.build();
	}

	public PublicationGroupStatusType deriveStatus(PublicationGroup publicationGroup) {
		PublicationGroupStatusType newStatus = deriveStatus(publicationGroup, publicationGroup.getPublications());
		publicationGroup.setStatus(newStatus);
		return newStatus;
	}

	private PublicationGroupStatusType deriveStatus(PublicationGroup publicationGroup, List<Publication> publications) {
		PublicationGroupStatusType newStatus = null;
		if (publications != null) {
			for (Publication publication : publications) {
				PublicationStatusType articleStatus = publicationService.derivePublicationStatus(publicationGroup,
						publication, false);
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

	public PublicationGroupStatusType deriveNewStatus(List<Publication> publications) {
		PublicationGroupStatusType newStatus = null;
		if (isAllClosed(publications)) {
			newStatus = PublicationGroupStatusType.Closed;
		} else if (isAllPublished(publications)) {
			newStatus = PublicationGroupStatusType.Published;
		} else if (isAllReadyToPublish(publications)) {
			newStatus = PublicationGroupStatusType.ReadyToPublish;
		} else if (isAllAssigned(publications)) {
			newStatus = PublicationGroupStatusType.Assigned;
		} else {
			newStatus = PublicationGroupStatusType.WIP;
		}

		return newStatus;
	}

	private boolean isAllClosed(List<Publication> publications) {
		boolean flag = true;
		for (Publication publication : publications) {
			if (!PublicationStatusType.Closed.equals(publication.getStatus())) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	private boolean isAllReadyToPublish(List<Publication> publications) {
		boolean flag = true;
		for (Publication publication : publications) {
			if (!PublicationStatusType.ReadyToPublish.equals(publication.getStatus())
					&& !PublicationStatusType.Published.equals(publication.getStatus())
					&& !PublicationStatusType.Closed.equals(publication.getStatus())) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	private boolean isAllPublished(List<Publication> publications) {
		boolean flag = true;
		for (Publication publication : publications) {
			if (!PublicationStatusType.Published.equals(publication.getStatus())
					&& !PublicationStatusType.Closed.equals(publication.getStatus())) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	private boolean isAllAssigned(List<Publication> publications) {
		boolean flag = true;
		for (Publication publication : publications) {
			if (!PublicationStatusType.Assigned.equals(publication.getStatus())
					&& !PublicationStatusType.Closed.equals(publication.getStatus())) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	public PublicationGroupDTO assignPublisher(Long publicationGroupId, String publisherId, String operatorId) {
		PublicationGroup publicationGroup = load(publicationGroupId);
		String currentPublisherId = publicationGroup.getPublisherId();
		if (PublicationGroupStatusType.Published.equals((publicationGroup.getStatus()))) {
			throw new BusinessException(ErrorCodeConstants.NO_CHANGES_ALLOWED);
		} else if (publisherId.equalsIgnoreCase(currentPublisherId)) {
			throw new BusinessException(ErrorCodeConstants.NOT_CHANGES_FOUND);
		}
		publicationGroup.setOperatorId(operatorId);
		PublicationGroupDTO publicationGroupDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);
		PublicationGroupStatusType newStatus = deriveNewStatus(
				publicationRepository.findByPublicationGroupId(publicationGroup.getPublicationGroupId()));
		publicationGroup.setPublisherId(publisherId);
		publicationGroup.setStatus(newStatus);
		repository.save(publicationGroup);
		return publicationGroupDTO;
	}

	public PublicationGroupDTO assignEditor(Long pubicationGroupId, String editorId, String operatorId) {
		PublicationGroup publicationGroup = load(pubicationGroupId);
		String currentEditorId = publicationGroup.getEditorId();
		if (PublicationGroupStatusType.Published.equals((publicationGroup.getStatus()))) {
			throw new BusinessException(ErrorCodeConstants.NO_CHANGES_ALLOWED);
		} else if (editorId.equalsIgnoreCase(currentEditorId)) {
			throw new BusinessException(ErrorCodeConstants.NOT_CHANGES_FOUND);
		}
		publicationGroup.setOperatorId(operatorId);
		PublicationGroupDTO publicationGroupDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);
		PublicationGroupStatusType newStatus = deriveNewStatus(
				publicationRepository.findByPublicationGroupId(publicationGroup.getPublicationGroupId()));
		publicationGroup.setEditorId(editorId);
		publicationGroup.setStatus(newStatus);
		repository.save(publicationGroup);
		return publicationGroupDTO;
	}

	public PublicationGroupDTO closePublicationGroup(Long pubicationGroupId, String userId) {
		PublicationGroup publicationGroup = load(pubicationGroupId);
		if (PublicationGroupStatusType.Published.equals((publicationGroup.getStatus()))) {
			throw new BusinessException(ErrorCodeConstants.NO_CHANGES_ALLOWED);
		}
		List<PublicationDTO> publicationList = publicationService.closePublications(pubicationGroupId, userId);
		PublicationGroupStatusType newStatus = deriveNewStatus(
				publicationRepository.findByPublicationGroupId(publicationGroup.getPublicationGroupId()));
		publicationGroup.setOperatorId(userId);
		publicationGroup.setStatus(newStatus);
		publicationGroup = repository.save(publicationGroup);
		PublicationGroupDTO publicationGroupDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);
		publicationGroupDTO.setPublications(publicationList);
		return publicationGroupDTO;
	}

	public PublicationGroupDTO reinstatePublicationGroup(Long pubicationGroupId, String userId) {
		PublicationGroup publicationGroup = load(pubicationGroupId);
		if (!PublicationGroupStatusType.Closed.equals((publicationGroup.getStatus()))) {
			throw new BusinessException(ErrorCodeConstants.NO_CHANGES_ALLOWED);
		}
		List<PublicationDTO> publicationList = publicationService.reinstatePublications(pubicationGroupId, userId);
		PublicationGroupStatusType newStatus = deriveNewStatus(
				publicationRepository.findByPublicationGroupId(publicationGroup.getPublicationGroupId()));
		publicationGroup.setOperatorId(userId);
		publicationGroup.setStatus(newStatus);
		publicationGroup = repository.save(publicationGroup);
		PublicationGroupDTO publicationGroupDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);
		publicationGroupDTO.setPublications(publicationList);
		return publicationGroupDTO;
	}

}
