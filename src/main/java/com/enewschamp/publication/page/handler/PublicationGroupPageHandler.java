package com.enewschamp.publication.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.app.dto.PublicationGroupDTO;
import com.enewschamp.publication.app.service.PublicationGroupHelper;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.PublicationGroupService;
import com.enewschamp.publication.page.data.PublicationGroupPageData;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "PublicationGroupPageHandler")
public class PublicationGroupPageHandler implements IPageHandler {

	@Autowired
	private PublicationGroupHelper publicationGroupHelper;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	UserService userService;

	@Autowired
	EditionService editionService;

	@Autowired
	PublicationGroupService publicationGroupService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {

		PageDTO pageDTO = new PageDTO();
		PublicationGroupPageData data = new PublicationGroupPageData();

		String action = pageRequest.getHeader().getAction();
		String userId = pageRequest.getHeader().getUserId();
		Long publicationGroupId = 0L;
		if (pageRequest.getData().get("publicationGroupId") != null) {
			publicationGroupId = pageRequest.getData().get("publicationGroupId").asLong();
		}
		PublicationGroupDTO publicationGroupDTO = null;
		switch (action) {
		case "SavePublication":
			try {
				objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
				publicationGroupDTO = objectMapper.readValue(pageRequest.getData().toString(),
						PublicationGroupDTO.class);
			} catch (JsonParseException e) {
				throw new RuntimeException(e);
			} catch (JsonMappingException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			publicationGroupDTO = publicationGroupHelper.createPublicationGroup(publicationGroupDTO);
			break;
		case "GetPreviousComments":
			// do nothing;
			break;
		case "ClosePublication":
			publicationGroupDTO = modelMapper.map(
					publicationGroupService.closePublicationGroup(publicationGroupId, userId),
					PublicationGroupDTO.class);
			break;
		case "ReInstatePublication":
			publicationGroupDTO = modelMapper.map(
					publicationGroupService.reinstatePublicationGroup(publicationGroupId, userId),
					PublicationGroupDTO.class);
			break;
		}
		data.setPublicationGroup(publicationGroupDTO);
		data.setPublisherLOV(userService.getPublisherLOV());
		data.setEditorLOV(userService.getEditorLOV());
		data.setAuthorLOV(userService.getAuthorLOV());
		data.setEditionsLOV(editionService.getLOV());
		pageDTO.setHeader(pageRequest.getHeader());
		pageDTO.setData(data);
		return pageDTO;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		Long publicationGroupId = null;
		if (!"CreatePublication".equalsIgnoreCase(pageNavigationContext.getActionName())
				&& pageNavigationContext.getPageRequest().getData().get("publicationGroupId") == null) {
			throw new BusinessException(ErrorCodeConstants.PUBLICATION_GROUP_ID_MISSING);
		}
		if (!"CreatePublication".equalsIgnoreCase(pageNavigationContext.getActionName())) {
			publicationGroupId = pageNavigationContext.getPageRequest().getData().get("publicationGroupId").asLong();
		}

		PublicationGroupPageData data = new PublicationGroupPageData();
		data.setPublisherLOV(userService.getPublisherLOV());
		data.setEditorLOV(userService.getEditorLOV());
		data.setAuthorLOV(userService.getAuthorLOV());
		data.setEditionsLOV(editionService.getLOV());
		if (publicationGroupId != null) {
			data.setPublicationGroup(publicationGroupHelper.getPublicationGroup(publicationGroupId));
		}
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		pageDTO.setData(data);
		return pageDTO;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}
}
