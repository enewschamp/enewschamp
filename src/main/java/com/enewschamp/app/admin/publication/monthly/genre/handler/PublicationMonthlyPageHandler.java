package com.enewschamp.app.admin.publication.monthly.genre.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.article.monthly.ArticlePublicationMonthlyGenre;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.publication.domain.service.PublicationMonthlySummaryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("PublicationMonthlyPageHandler")
public class PublicationMonthlyPageHandler implements IPageHandler {
	@Autowired
	private PublicationMonthlySummaryService dailySummaryService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "List":
			pageDto = listPublicationMonthlySummary(pageRequest);
			break;
		default:
			break;
		}
		return pageDto;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@SneakyThrows
	private PageDTO listPublicationMonthlySummary(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<ArticlePublicationMonthlyGenre> monthlySummaryList = dailySummaryService.listPublicationMonthlySummary(
				searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<PublicationMonthlySummaryPageData> list = mapPublicationMonthlySummaryData(monthlySummaryList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((monthlySummaryList.getNumber() + 1) == monthlySummaryList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<PublicationMonthlySummaryPageData> mapPublicationMonthlySummaryData(
			Page<ArticlePublicationMonthlyGenre> page) {
		List<PublicationMonthlySummaryPageData> monthlySummaryPageDataList = new ArrayList<PublicationMonthlySummaryPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<ArticlePublicationMonthlyGenre> pageDataList = page.getContent();
			for (ArticlePublicationMonthlyGenre userLogin : pageDataList) {
				PublicationMonthlySummaryPageData userLoginPageData = modelMapper.map(userLogin,
						PublicationMonthlySummaryPageData.class);
				monthlySummaryPageDataList.add(userLoginPageData);
			}
		}
		return monthlySummaryPageDataList;
	}
}