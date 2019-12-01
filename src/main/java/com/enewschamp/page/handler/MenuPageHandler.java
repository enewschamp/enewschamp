package com.enewschamp.page.handler;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.menu.page.data.MenuPageData;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.article.page.data.NewsArticleSearchResultData;
import com.enewschamp.domain.common.AbstractPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.entity.StudentDetailsWork;
import com.enewschamp.subscription.domain.service.StudentDetailsService;
import com.enewschamp.subscription.domain.service.StudentDetailsWorkService;

@Component(value = "MenuPageHandler")
public class MenuPageHandler extends AbstractPageHandler {

	@Autowired
	private NewsArticleService newsArticleService;
	@Autowired
	StudentControlBusiness studentControlBusiness;
	@Autowired
	private EnewschampApplicationProperties appConfig;
	@Autowired
	StudentDetailsBusiness studentDetailsBusiness;

	@Autowired
	StudentDetailsWorkService studentDetailsWorkService;

	@Autowired
	StudentDetailsService studentDetailsService;

	@Override
	public PageDTO handlePageAction(String actionName, PageRequestDTO pageRequest) {

		PageDTO page = new PageDTO();
		return page;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String eMailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();
		String action = pageNavigationContext.getActionName();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionID();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationdate();
		if (PageAction.home.toString().equalsIgnoreCase(action)) {
			NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
			searchRequestData.setEditionId(editionId);
			searchRequestData.setPublicationDate(publicationDate);
			NewsArticleSearchResultData searchResult = new NewsArticleSearchResultData();
			pageDto.setData(searchResult);
			Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
					pageNavigationContext.getPageRequest().getHeader());
			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(pageResult.isLast());
			header.setPageCount(pageResult.getTotalPages());
			header.setRecordCount(pageResult.getNumberOfElements());
			header.setPageNo(pageResult.getNumber() + 1);
			pageDto.setHeader(header);
		} else {
			Long studentId = studentControlBusiness.getStudentId(eMailId);
			MenuPageData pageData = new MenuPageData();
			StudentDetails student = studentDetailsService.get(studentId);
			StudentDetailsWork studentWork = null;
			if (student == null) {
				studentWork = studentDetailsWorkService.get(studentId);
				if (studentWork != null) {
					pageData.setName(studentWork.getName());
					pageData.setPicImage(studentWork.getPhoto());
					pageData.setSurname(studentWork.getSurname());
				}
			} else {
				pageData.setName(student.getName());
				pageData.setPicImage(student.getPhoto());
				pageData.setSurname(student.getSurname());
			}
			pageData.setPremiumSubsMsg(appConfig.getPremiumSubsMsg());
			pageDto.setData(pageData);
			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			pageDto.setHeader(header);
		}
		return pageDto;

	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}
}
