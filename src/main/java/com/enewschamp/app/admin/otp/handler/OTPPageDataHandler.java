package com.enewschamp.app.admin.otp.handler;

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
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.otp.entity.OTP;
import com.enewschamp.app.otp.service.OTPService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("OTPPageDataHandler")
@Slf4j
public class OTPPageDataHandler implements IPageHandler {
	@Autowired
	private OTPService otpService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;
	private Validator validator;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createOTP(pageRequest);
			break;
		case "Update":
			pageDto = updateOTP(pageRequest);
			break;
		case "Read":
			pageDto = readOTP(pageRequest);
			break;
		case "Close":
			pageDto = closeOTP(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateOTP(pageRequest);
			break;
		case "List":
			pageDto = listOTP(pageRequest);
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
	private PageDTO createOTP(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		OTPPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), OTPPageData.class);
		validateData(pageData);
		OTP otp = mapOTPData(pageRequest, pageData);
		otp = otpService.create(otp);
		mapOTP(pageRequest, pageDto, otp);
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

	private OTP mapOTPData(PageRequestDTO pageRequest, OTPPageData pageData) {
		OTP otp = modelMapper.map(pageData, OTP.class);
		otp.setRecordInUse(RecordInUseType.Y);
		return otp;
	}

	@SneakyThrows
	private PageDTO updateOTP(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		OTPPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), OTPPageData.class);
		validateData(pageData);
		OTP otp = mapOTPData(pageRequest, pageData);
		otp = otpService.update(otp);
		mapOTP(pageRequest, pageDto, otp);
		return pageDto;
	}

	private void mapOTP(PageRequestDTO pageRequest, PageDTO pageDto, OTP otp) {
		OTPPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(otp);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readOTP(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		OTPPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), OTPPageData.class);
		OTP otp = modelMapper.map(pageData, OTP.class);
		otp = otpService.read(otp);
		mapOTP(pageRequest, pageDto, otp);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateOTP(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		OTPPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), OTPPageData.class);
		OTP otp = modelMapper.map(pageData, OTP.class);
		otp = otpService.reInstate(otp);
		mapOTP(pageRequest, pageDto, otp);
		return pageDto;
	}

	private OTPPageData mapPageData(OTP otp) {
		OTPPageData pageData = modelMapper.map(otp, OTPPageData.class);
		pageData.setLastUpdate(otp.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeOTP(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		OTPPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), OTPPageData.class);
		OTP otp = modelMapper.map(pageData, OTP.class);
		otp = otpService.close(otp);
		mapOTP(pageRequest, pageDto, otp);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listOTP(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = new AdminSearchRequest();
		searchRequest.setCountryId(
				pageRequest.getData().get(CommonConstants.FILTER).get(CommonConstants.COUNTRY_ID).asText());
		Page<OTP> otpList = otpService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<OTPPageData> list = mapOTPData(otpList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((otpList.getNumber() + 1) == otpList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<OTPPageData> mapOTPData(Page<OTP> page) {
		List<OTPPageData> otpPageDataList = new ArrayList<OTPPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<OTP> pageDataList = page.getContent();
			for (OTP otp : pageDataList) {
				OTPPageData otpPageData = modelMapper.map(otp, OTPPageData.class);
				otpPageData.setLastUpdate(otp.getOperationDateTime());
				otpPageDataList.add(otpPageData);
			}
		}
		return otpPageDataList;
	}

	private void validateData(OTPPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<OTPPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}
}
