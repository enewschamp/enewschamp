package com.enewschamp.app.admin.student.quiz.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.quiz.entity.QuizScore;
import com.enewschamp.app.student.quiz.service.QuizScoreService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component("QuizScorePageHandler")
@RequiredArgsConstructor
public class QuizScorePageHandler implements IPageHandler {
	private final QuizScoreService quizScoreService;
	private final ModelMapper modelMapper;
	private final ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createQuizScore(pageRequest);
			break;
		case "Update":
			pageDto = updateQuizScore(pageRequest);
			break;
		case "Read":
			pageDto = readQuizScore(pageRequest);
			break;
		case "Close":
			pageDto = closeQuizScore(pageRequest);
			break;
		case "Reinstate":
			pageDto = reInQuizScore(pageRequest);
			break;
		case "List":
			pageDto = listQuizScore(pageRequest);
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
	private PageDTO createQuizScore(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		QuizScorePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), QuizScorePageData.class);
		validate(pageData, this.getClass().getName());
		QuizScore quizScore = mapQuizScoreData(pageRequest, pageData);
		quizScore = quizScoreService.create(quizScore);
		mapQuizScore(pageRequest, pageDto, quizScore);
		return pageDto;
	}

	private QuizScore mapQuizScoreData(PageRequestDTO pageRequest, QuizScorePageData pageData) {
		QuizScore quizScore = modelMapper.map(pageData, QuizScore.class);
		quizScore.setRecordInUse(RecordInUseType.Y);
		return quizScore;
	}

	@SneakyThrows
	private PageDTO updateQuizScore(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		QuizScorePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), QuizScorePageData.class);
		validate(pageData, this.getClass().getName());
		QuizScore quizScore = mapQuizScoreData(pageRequest, pageData);
		quizScore = quizScoreService.update(quizScore);
		mapQuizScore(pageRequest, pageDto, quizScore);
		return pageDto;
	}

	private void mapQuizScore(PageRequestDTO pageRequest, PageDTO pageDto, QuizScore quizScore) {
		QuizScorePageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(quizScore);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readQuizScore(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		QuizScorePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), QuizScorePageData.class);
		QuizScore quizScore = modelMapper.map(pageData, QuizScore.class);
		quizScore = quizScoreService.read(quizScore);
		mapQuizScore(pageRequest, pageDto, quizScore);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reInQuizScore(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		QuizScorePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), QuizScorePageData.class);
		QuizScore quizScore = modelMapper.map(pageData, QuizScore.class);
		quizScore = quizScoreService.reInstateQuizScore(quizScore);
		mapQuizScore(pageRequest, pageDto, quizScore);
		return pageDto;
	}

	private QuizScorePageData mapPageData(QuizScore quizScore) {
		QuizScorePageData pageData = modelMapper.map(quizScore, QuizScorePageData.class);
		pageData.setLastUpdate(quizScore.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeQuizScore(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		QuizScorePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), QuizScorePageData.class);
		QuizScore quizScore = modelMapper.map(pageData, QuizScore.class);
		quizScore = quizScoreService.close(quizScore);
		mapQuizScore(pageRequest, pageDto, quizScore);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listQuizScore(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<QuizScore> quizScoreList = quizScoreService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<QuizScorePageData> list = mapQuizScoreData(quizScoreList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((quizScoreList.getNumber() + 1) == quizScoreList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<QuizScorePageData> mapQuizScoreData(Page<QuizScore> page) {
		List<QuizScorePageData> quizScorePageDataList = new ArrayList<QuizScorePageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<QuizScore> pageDataList = page.getContent();
			for (QuizScore quizScore : pageDataList) {
				QuizScorePageData QuizScorePageData = mapPageData(quizScore);
				QuizScorePageData.setLastUpdate(quizScore.getOperationDateTime());
				quizScorePageDataList.add(QuizScorePageData);
			}
		}
		return quizScorePageDataList;
	}

}