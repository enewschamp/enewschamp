package com.enewschamp.article.page.handler;

import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.registration.entity.RegistrationStatus;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.page.data.HashTagPageData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.app.dto.HashTagDTO;
import com.enewschamp.publication.domain.entity.HashTag;
import com.enewschamp.publication.domain.service.HashTagService;
import com.enewschamp.publication.domain.service.PublicationGroupService;
import com.enewschamp.publication.page.handler.PublicationGroupPageHandler;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "HashTagPageHandler")
public class HashTagPageHandler implements IPageHandler {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private HashTagService hashTagService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDTO = new PageDTO();
		return pageDTO;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO page = new PageDTO();
		HashTagPageData hashTagPageData = new HashTagPageData();
		String action = pageNavigationContext.getPageRequest().getHeader().getAction();
		HashTagDTO hashTagDTO = null;
		try {
			objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
			hashTagDTO = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
					HashTagDTO.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		HashTag hashTag = modelMapper.map(hashTagDTO, HashTag.class);
		if (hashTag == null || hashTag.getHashTag() == null || "".equals(hashTag.getHashTag().trim())) {
			throw new BusinessException(ErrorCodeConstants.INVALID_HASHTAG);
		}
		if ("AddHashTag".equalsIgnoreCase(action)) {
			if (hashTagService.get(hashTag.getHashTag()) == null) {
				hashTag = hashTagService.create(hashTag);
				hashTagPageData.setMessage("HashTag added successfully");
			} else {
				throw new BusinessException(ErrorCodeConstants.HASHTAG_ALREADY_EXISTS);
			}
		} else if ("ModifyHashTag".equalsIgnoreCase(action)) {
			hashTag = hashTagService.update(hashTag);
			hashTagPageData.setMessage("HashTag modified successfully");
		} else if ("DeleteHashTag".equalsIgnoreCase(action)) {
			hashTagService.delete(hashTag.getHashTag());
			hashTagPageData.setMessage("HashTag deleted successfully");
		} else if ("SearchHashTag".equalsIgnoreCase(action)) {
			List<HashTagDTO> hashTagList = hashTagService.getHashTagByName(hashTag.getHashTag());
			hashTagPageData.setHashTagList(hashTagList);
		}
		page.setData(hashTagPageData);
		page.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return page;
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