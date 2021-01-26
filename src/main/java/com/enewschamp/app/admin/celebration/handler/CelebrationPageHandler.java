package com.enewschamp.app.admin.celebration.handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.celebration.entity.Celebration;
import com.enewschamp.app.admin.celebration.service.CelebrationService;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("CelebrationPageHandler")
@Slf4j
public class CelebrationPageHandler implements IPageHandler {
	@Autowired
	private CelebrationService celebrationService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
	private Validator validator;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createCelebration(pageRequest);
			break;
		case "Update":
			pageDto = updateCelebration(pageRequest);
			break;
		case "Read":
			pageDto = readCelebration(pageRequest);
			break;
		case "Close":
			pageDto = closeCelebration(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateCelebration(pageRequest);
			break;
		case "List":
			pageDto = listCelebration(pageRequest);
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
	private PageDTO createCelebration(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CelebrationPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CelebrationPageData.class);
		validate(pageData);
		Celebration celebration = mapCelebrationData(pageRequest, pageData);
		celebration = celebrationService.create(celebration);
		String newImageName = celebration.getCelebrationId() + "_" + System.currentTimeMillis();
		String imageType = pageData.getImageTypeExt();
		boolean saveFlag = commonService.saveImages("Admin", "celebration", imageType, pageData.getBase64Image(),
				newImageName, celebration.getImageName());
		if (saveFlag) {
			celebration.setImageName(newImageName + "." + imageType);
			celebration = celebrationService.update(celebration);
		}
		mapCelebration(pageRequest, pageDto, celebration);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
		pageDto.getHeader().setUserId(null);
		pageDto.getHeader().setDeviceId(null);
	}

	private Celebration mapCelebrationData(PageRequestDTO pageRequest, CelebrationPageData pageData) {
		Celebration celebration = modelMapper.map(pageData, Celebration.class);
		celebration.setRecordInUse(RecordInUseType.Y);
		return celebration;
	}

	@SneakyThrows
	private PageDTO updateCelebration(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CelebrationPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CelebrationPageData.class);
		validate(pageData);
		Celebration celebration = mapCelebrationData(pageRequest, pageData);
		celebration = celebrationService.update(celebration);
		mapCelebration(pageRequest, pageDto, celebration);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readCelebration(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CelebrationPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CelebrationPageData.class);
		Celebration celebration = modelMapper.map(pageData, Celebration.class);
		celebration = celebrationService.read(celebration);
		mapCelebration(pageRequest, pageDto, celebration);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeCelebration(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CelebrationPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CelebrationPageData.class);
		Celebration celebration = modelMapper.map(pageData, Celebration.class);
		celebration = celebrationService.close(celebration);
		mapCelebration(pageRequest, pageDto, celebration);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateCelebration(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CelebrationPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CelebrationPageData.class);
		Celebration celebration = modelMapper.map(pageData, Celebration.class);
		celebration = celebrationService.reinstate(celebration);
		mapCelebration(pageRequest, pageDto, celebration);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listCelebration(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<Celebration> celebrationList = celebrationService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<CelebrationPageData> list = mapCelebrationData(celebrationList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((celebrationList.getNumber() + 1) == celebrationList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private CelebrationPageData mapPageData(Celebration celebration) {
		CelebrationPageData pageData = modelMapper.map(celebration, CelebrationPageData.class);
		pageData.setLastUpdate(celebration.getOperationDateTime());
		return pageData;
	}

	private void mapCelebration(PageRequestDTO pageRequest, PageDTO pageDto, Celebration celebration) {
		CelebrationPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(celebration);
		pageDto.setData(pageData);
	}

	public List<CelebrationPageData> mapCelebrationData(Page<Celebration> page) {
		List<CelebrationPageData> celebrationPageDataList = new ArrayList<CelebrationPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<Celebration> pageDataList = page.getContent();
			for (Celebration celebration : pageDataList) {
				CelebrationPageData celebrationPageData = modelMapper.map(celebration, CelebrationPageData.class);
				celebrationPageData.setLastUpdate(celebration.getOperationDateTime());
				celebrationPageDataList.add(celebrationPageData);
			}
		}
		return celebrationPageDataList;
	}

	private void validate(CelebrationPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<CelebrationPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}