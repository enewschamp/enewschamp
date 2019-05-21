package com.enewschamp.publication.page.handler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.app.service.PublicationGroupHelper;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.publication.page.data.PublicationGroupPageData;
import com.enewschamp.user.domain.service.UserService;

@Component(value="PublicationListPageHandler")
public class PublicationListPageHandler implements IPageHandler  {

	
	@Autowired
	private PublicationGroupHelper publicationGroupHelper;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	GenreService genreService;

	@Autowired
	UserService userService;
	
	@Autowired
	EditionService editionService;
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		
		PageDTO pageDTO = new PageDTO();

		Long publicationGroupId = pageRequest.getData().get("publicationGroupId").asLong();
		
		PublicationGroupPageData data = new PublicationGroupPageData();
		
		data.setPublisherLOV(userService.getPublisherLOV());
		data.setEditorLOV(userService.getEditorLOV());
		
		data.setPublicationGroup(publicationGroupHelper.getPublicationGroup(publicationGroupId));		
		pageDTO.setData(data);
		return pageDTO;
	}
	
	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		return pageNavigationContext.getPreviousPageResponse();
	}
}
