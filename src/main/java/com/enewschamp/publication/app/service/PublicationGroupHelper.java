package com.enewschamp.publication.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.app.dto.PublicationGroupDTO;
import com.enewschamp.publication.domain.common.PublicationGroupStatusType;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.entity.PublicationGroup;
import com.enewschamp.publication.domain.service.PublicationGroupRepository;
import com.enewschamp.publication.domain.service.PublicationGroupService;
import com.enewschamp.publication.domain.service.PublicationRepository;

@Component
public class PublicationGroupHelper {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private PublicationGroupService publicationGroupService;

	@Autowired
	private PublicationRepository publicationRepository;

	@Autowired
	private PublicationGroupRepository publicationGroupRepository;

	@Autowired
	private PublicationHelper publicationHelper;

	public PublicationGroupDTO createPublicationGroup(PublicationGroupDTO publicationGroupDTO) {
		List<PublicationDTO> publicationDTOs = null;
		PublicationGroup existingPublicationGroup = publicationGroupService
				.get(publicationGroupDTO.getPublicationGroupId());
		if (existingPublicationGroup != null) {
			PublicationGroup publicationGroup = publicationGroupService
					.getPublicationGroupByPublicationDate(publicationGroupDTO.getPublicationDate());
			if (publicationGroup != null
					&& (publicationGroup.getPublicationGroupId() != publicationGroupDTO.getPublicationGroupId())) {
				throw new BusinessException(ErrorCodeConstants.PUBLICATION_ALREADY_EXIST_FOR_PUB_DATE,
						String.valueOf(publicationGroupDTO.getPublicationDate()));
			}
			publicationDTOs = publicationGroupDTO.getPublications();
		} else {
			if (checkDedupePublicationDate(publicationGroupDTO.getPublicationDate())) {
				throw new BusinessException(ErrorCodeConstants.PUBLICATION_ALREADY_EXIST_FOR_PUB_DATE,
						String.valueOf(publicationGroupDTO.getPublicationDate()));
			} else {
				if (publicationGroupDTO.getPublications() != null
						&& (publicationGroupDTO.getPublications().size() > 0)) {
					publicationDTOs = publicationGroupDTO.getPublications();
				} else {
					publicationDTOs = createDefaultPublications(publicationGroupDTO);
				}
			}
		}
		PublicationGroup publicationGroup = modelMapper.map(publicationGroupDTO, PublicationGroup.class);
		if (publicationGroup.getRecordInUse() == null) {
			publicationGroup.setRecordInUse(RecordInUseType.Y);
		}

		List<Publication> publications = new ArrayList<Publication>();
		for (PublicationDTO publicationDTO : publicationDTOs) {
			publications.add(modelMapper.map(publicationDTO, Publication.class));
		}
		publicationGroup.setPublications(publications);
		publicationGroup = publicationGroupService.create(publicationGroup);
		publicationGroupDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);

		List<PublicationDTO> publicationList = new ArrayList<PublicationDTO>();
		for (PublicationDTO publicationDTO : publicationDTOs) {
			PublicationDTO existingPublicationDTO = null;
			if (publicationDTO.getPublicationId() != null) {
				existingPublicationDTO = publicationHelper.get(publicationDTO.getPublicationId());
			}
			if (existingPublicationDTO == null || (publicationDTO.getCurrentAction() != null)) {
				if (existingPublicationDTO != null) {
					publicationDTO.setStatus(existingPublicationDTO.getStatus());
				}
				publicationDTO.setPublicationGroupId(publicationGroup.getPublicationGroupId());
				publicationDTO.setOperatorId(publicationGroup.getOperatorId());
				publicationDTO.setRecordInUse(publicationGroup.getRecordInUse());
				publicationDTO = copyFromPublicationGroup(publicationGroup, publicationDTO);
				publicationDTO = publicationHelper.createPublication(publicationDTO);
			}
			publicationList.add(publicationDTO);
		}
		publicationGroup = publicationGroupService.get(publicationGroupDTO.getPublicationGroupId());
		publications = publicationRepository.findByPublicationGroupId(publicationGroup.getPublicationGroupId());
		PublicationGroupStatusType newStatus = publicationGroupService.deriveNewStatus(
				publicationRepository.findByPublicationGroupId(publicationGroup.getPublicationGroupId()));
		if (PublicationGroupStatusType.Closed.equals(newStatus)) {
			publicationGroup.setRecordInUse(RecordInUseType.N);
		}
		publicationGroup.setStatus(newStatus);
		publicationGroup.setPublications(publications);
		publicationGroup = publicationGroupRepository.save(publicationGroup);
		publicationGroupDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);
		publicationGroupDTO.setPublications(publicationList);
		return publicationGroupDTO;
	}

	private PublicationDTO copyFromPublicationGroup(PublicationGroup publicationGroup, PublicationDTO publicationDTO) {
		publicationDTO.setPublicationGroupId(publicationGroup.getPublicationGroupId());
		publicationDTO.setRecordInUse(publicationGroup.getRecordInUse());
		publicationDTO.setOperatorId(publicationGroup.getOperatorId());
		publicationDTO.setPublicationDate(publicationGroup.getPublicationDate());
		return publicationDTO;
	}

	public PublicationGroupDTO getPublicationGroup(Long publicationGroupId) {
		PublicationGroup publicationGroup = publicationGroupService.get(publicationGroupId);
		PublicationGroupDTO publicationDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);
		List<PublicationDTO> publications = publicationHelper.getByPublicationGroupId(publicationGroupId);
		publicationDTO.setPublications(publications);
		return publicationDTO;
	}

	private boolean checkDedupePublicationDate(LocalDate publicationDate) {
		PublicationGroup publicationGroup = publicationGroupService
				.getPublicationGroupByPublicationDate(publicationDate);
		return (publicationGroup != null ? true : false);
	}

	private List<PublicationDTO> createDefaultPublications(PublicationGroupDTO publicationGroupDTO) {
		List<PublicationDTO> publications = new ArrayList<PublicationDTO>();
		publications.add(createDefaultPublication(publicationGroupDTO, 1));
		publications.add(createDefaultPublication(publicationGroupDTO, 2));
		publications.add(createDefaultPublication(publicationGroupDTO, 3));
		publications.add(createDefaultPublication(publicationGroupDTO, 4));
		return publications;
	}

	private PublicationDTO createDefaultPublication(PublicationGroupDTO publicationGroupDTO, int readingLevel) {
		PublicationDTO publication = new PublicationDTO();
		publication.setRecordInUse(RecordInUseType.Y);
		publication.setOperationDateTime(publicationGroupDTO.getOperationDateTime());
		publication.setReadingLevel(readingLevel);
		return publication;
	}
}
