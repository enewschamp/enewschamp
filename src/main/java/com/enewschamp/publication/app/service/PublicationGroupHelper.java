package com.enewschamp.publication.app.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.app.dto.PublicationGroupDTO;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.entity.PublicationGroup;
import com.enewschamp.publication.domain.service.PublicationArticleLinkageRepository;
import com.enewschamp.publication.domain.service.PublicationGroupService;

@Component
public class PublicationGroupHelper {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private PublicationGroupService publicationGroupService;
	
	@Autowired
	PublicationArticleLinkageRepository publicationArticleLinkageRepository;
	
	@Autowired
	private PublicationHelper publicationHelper;
	
	public PublicationGroupDTO createPublicationGroup(PublicationGroupDTO publicationGroupDTO) {
		
		List<PublicationDTO> publicationDTOs = publicationGroupDTO.getPublications();
		
		PublicationGroup publicationGroup = modelMapper.map(publicationGroupDTO, PublicationGroup.class);
		
		List<Publication> publications = new ArrayList<Publication>();
		for(PublicationDTO publicationDTO: publicationDTOs) {
			publications.add(modelMapper.map(publicationDTO, Publication.class));
		}
		publicationGroup.setPublications(publications);
		
		publicationGroup = publicationGroupService.create(publicationGroup);
		
		publicationGroupDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);
		
		List<PublicationDTO> publicationList = new ArrayList<PublicationDTO>(); 
		for(PublicationDTO publicationDTO: publicationDTOs) {
			publicationDTO.setPublicationGroupId(publicationGroup.getPublicationGroupId());
			publicationDTO.setRecordInUse(publicationGroup.getRecordInUse());
			publicationDTO.setOperatorId(publicationGroup.getOperatorId());
			publicationDTO.setEditionId(publicationGroup.getEditionId());
			publicationDTO.setPublishDate(publicationGroup.getPublicationDate());
			publicationDTO = publicationHelper.createPublication(publicationDTO);
			publicationList.add(publicationDTO);
		}
		publicationGroupDTO.setPublications(publicationList);
		
		return publicationGroupDTO;
	}

	public PublicationGroupDTO getPublicationGroup(Long publicationGroupId) {
		PublicationGroup publicationGroup = publicationGroupService.get(publicationGroupId);
		PublicationGroupDTO publicationDTO = modelMapper.map(publicationGroup, PublicationGroupDTO.class);
		
		List<PublicationDTO> publications = publicationHelper.getByPublicationGroupId(publicationGroupId);
		publicationDTO.setPublications(publications);
		return publicationDTO;
	}
	
}