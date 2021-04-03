package com.enewschamp.article.page.handler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.CommentsAuditPageData;
import com.enewschamp.article.page.data.PropertyAuditData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.domain.service.PublicationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "CommentsAuditPageHandler")
public class CommentsAuditPageHandler implements IPageHandler {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private NewsArticleGroupService articleGroupService;

	@Autowired
	private NewsArticleService articleService;

	@Autowired
	private PublicationService publicationService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {

		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO page = new PageDTO();
		CommentsAuditPageData pageData = new CommentsAuditPageData();

		List<PropertyAuditData> commentsAuditData = null;
		Long articleGroupId = null;
		Long articleId = null;

		JsonNode node = pageNavigationContext.getPageRequest().getData().get("newsArticleGroupId");
		if (node != null) {
			articleGroupId = node.asLong();
		} else {
			node = pageNavigationContext.getPageRequest().getData().get("newsArticleId");
			if (node != null) {
				articleId = node.asLong();
			}
		}
		if (articleGroupId != null || articleId != null) {
			if (articleGroupId != null) {
				commentsAuditData = articleGroupService.getPreviousComments(articleGroupId);
				pageData.setNewsArticleGroupId(articleGroupId);
			} else {
				commentsAuditData = articleService.getPreviousComments(articleId);
				pageData.setNewsArticleId(articleId);
			}
		} else {
			Long publicationId = null;
			node = pageNavigationContext.getPageRequest().getData().get("publicationId");
			if (node != null) {
				publicationId = node.asLong();
			}
			if (publicationId != null) {
				commentsAuditData = publicationService.getPreviousComments(publicationId);
			}
			pageData.setPublicationId(publicationId);
		}
		pageData.setPreviousComments(commentsAuditData);
		page.setHeader(pageNavigationContext.getPageRequest().getHeader());
		page.setData(pageData);
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