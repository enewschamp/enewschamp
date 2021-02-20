package com.enewschamp.app.admin.publication.monthly.total.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.article.monthly.total.ArticlePublicationMonthlyTotal;
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

@Component("PublicationMonthlyTotalPageHandler")
public class PublicationMonthlyTotalPageHandler implements IPageHandler {
	@Autowired
	private PublicationMonthlySummaryService monthlySummaryService;
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
		Page<ArticlePublicationMonthlyTotal> monthlyTotalList = monthlySummaryService.listPublicationMonthlyTotal(
				searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<PublicationMonthlyTotalPageData> list = mapPublicationMonthlySummaryData(monthlyTotalList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((monthlyTotalList.getNumber() + 1) == monthlyTotalList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<PublicationMonthlyTotalPageData> mapPublicationMonthlySummaryData(
			Page<ArticlePublicationMonthlyTotal> page) {
		List<PublicationMonthlyTotalPageData> monthlySummaryPageDataList = new ArrayList<PublicationMonthlyTotalPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<ArticlePublicationMonthlyTotal> pageDataList = page.getContent();
			for (ArticlePublicationMonthlyTotal monthlyTotal : pageDataList) {
				PublicationMonthlyTotalPageData monthlyTotalPageData = modelMapper.map(monthlyTotal,
						PublicationMonthlyTotalPageData.class);
				monthlySummaryPageDataList.add(monthlyTotalPageData);
			}
		}
		return monthlySummaryPageDataList;
	}
}