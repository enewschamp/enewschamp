package com.enewschamp.app.admin.student.achievement.handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.subscription.domain.entity.StudentShareAchievements;
import com.enewschamp.subscription.domain.service.StudentShareAchievementsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("StudentShareAchievementsPageHandler")
public class StudentShareAchievementsPageHandler implements IPageHandler {

	@Autowired
	private StudentShareAchievementsService studentShareAchievementsService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createStudentAchievement(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentAchievement(pageRequest);
			break;
		case "Read":
			pageDto = readStudentAchievement(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentAchievement(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateStudentAchievement(pageRequest);
			break;
		case "List":
			pageDto = listStudentAchievement(pageRequest);
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
	private PageDTO createStudentAchievement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentShareAchievementsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentShareAchievementsPageData.class);
		validate(pageData,  this.getClass().getName());
		StudentShareAchievements studentAchievements = mapStudentShareAchievementsData(pageRequest, pageData);
		studentAchievements = studentShareAchievementsService.create(studentAchievements);
		mapStudentShareAchievements(pageRequest, pageDto, studentAchievements);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateStudentAchievement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentShareAchievementsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentShareAchievementsPageData.class);
		validate(pageData,  this.getClass().getName());
		StudentShareAchievements studentAchievements = mapStudentShareAchievementsData(pageRequest, pageData);
		studentAchievements = studentShareAchievementsService.update(studentAchievements);
		mapStudentShareAchievements(pageRequest, pageDto, studentAchievements);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readStudentAchievement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentShareAchievementsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentShareAchievementsPageData.class);
		StudentShareAchievements studentAchievements = modelMapper.map(pageData, StudentShareAchievements.class);
		studentAchievements = studentShareAchievementsService.read(studentAchievements);
		mapStudentShareAchievements(pageRequest, pageDto, studentAchievements);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeStudentAchievement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentShareAchievementsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentShareAchievementsPageData.class);
		StudentShareAchievements studentAchievements = modelMapper.map(pageData, StudentShareAchievements.class);
		studentAchievements = studentShareAchievementsService.close(studentAchievements);
		mapStudentShareAchievements(pageRequest, pageDto, studentAchievements);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateStudentAchievement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentShareAchievementsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentShareAchievementsPageData.class);
		StudentShareAchievements studentAchievements = modelMapper.map(pageData, StudentShareAchievements.class);
		studentAchievements = studentShareAchievementsService.reInstate(studentAchievements);
		mapStudentShareAchievements(pageRequest, pageDto, studentAchievements);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentAchievement(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);

		Page<StudentShareAchievements> studentAchievementsList = studentShareAchievementsService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentShareAchievementsPageData> list = mapStudentShareAchievementsData(studentAchievementsList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((studentAchievementsList.getNumber() + 1) == studentAchievementsList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private void mapStudentShareAchievements(PageRequestDTO pageRequest, PageDTO pageDto,
			StudentShareAchievements studentAchievements) {
		StudentShareAchievementsPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(studentAchievements);
		pageDto.setData(pageData);
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
		pageDto.getHeader().setUserId(null);
		pageDto.getHeader().setDeviceId(null);
	}

	private StudentShareAchievements mapStudentShareAchievementsData(PageRequestDTO pageRequest,
			StudentShareAchievementsPageData pageData) {
		StudentShareAchievements studentAchievements = modelMapper.map(pageData, StudentShareAchievements.class);
		studentAchievements.setRecordInUse(RecordInUseType.Y);
		return studentAchievements;
	}

	private StudentShareAchievementsPageData mapPageData(StudentShareAchievements studentAchievements) {
		StudentShareAchievementsPageData pageData = modelMapper.map(studentAchievements,
				StudentShareAchievementsPageData.class);
		pageData.setLastUpdate(studentAchievements.getOperationDateTime());
		return pageData;
	}

	public List<StudentShareAchievementsPageData> mapStudentShareAchievementsData(Page<StudentShareAchievements> page) {
		List<StudentShareAchievementsPageData> studentAchievementsPageDataList = new ArrayList<StudentShareAchievementsPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentShareAchievements> pageDataList = page.getContent();
			for (StudentShareAchievements studentAchievements : pageDataList) {
				StudentShareAchievementsPageData pageData = modelMapper.map(studentAchievements,
						StudentShareAchievementsPageData.class);
				pageData.setLastUpdate(studentAchievements.getOperationDateTime());
				studentAchievementsPageDataList.add(pageData);
			}
		}
		return studentAchievementsPageDataList;
	}

}
