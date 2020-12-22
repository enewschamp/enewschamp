package com.enewschamp.article.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.AuditPageData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.domain.service.PublicationGroupService;
import com.enewschamp.publication.domain.service.PublicationService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "AuditPageHandler")
public class AuditPageHandler implements IPageHandler {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private NewsArticleGroupService articleGroupService;

	@Autowired
	private NewsArticleService articleService;

	@Autowired
	private PublicationGroupService publicationGroupService;

	@Autowired
	private PublicationService publicationService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO page = new PageDTO();
		AuditPageData pageData = new AuditPageData();

		String auditData = null;

		// Check for article group audit
		Long articleGroupId = null;
		JsonNode node = pageNavigationContext.getPageRequest().getData().get("newsArticleGroupId");
		if (node != null) {
			articleGroupId = node.asLong();
		}
		if (articleGroupId != null) {
			auditData = articleGroupService.getAudit(articleGroupId);
			pageData.setNewsArticleGroupId(articleGroupId);
		}
		// Check for article audit
		Long newsArticleId = null;
		node = pageNavigationContext.getPageRequest().getData().get("newsArticleId");
		if (node != null) {
			newsArticleId = node.asLong();
		}
		if (newsArticleId != null) {
			auditData = articleService.getAudit(newsArticleId);
			pageData.setNewsArticleId(newsArticleId);
		}

		Long publicationGroupId = null;
		node = pageNavigationContext.getPageRequest().getData().get("publicationGroupId");
		if (node != null) {
			publicationGroupId = node.asLong();
		}
		if (publicationGroupId != null) {
			auditData = publicationGroupService.getAudit(publicationGroupId);
			pageData.setPublicationGroupId(publicationGroupId);
		}
		// Check for publication audit
		Long publicationId = null;
		node = pageNavigationContext.getPageRequest().getData().get("publicationId");
		if (node != null) {
			publicationId = node.asLong();
		}
		if (publicationId != null) {
			auditData = publicationService.getAudit(publicationId);
			pageData.setPublicationId(publicationId);
		}
		try {
			pageData.setAudit(objectMapper.readValue(auditData, JsonNode.class));
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		page.setData(pageData);
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