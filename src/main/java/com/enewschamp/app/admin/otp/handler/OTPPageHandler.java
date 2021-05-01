package com.enewschamp.app.admin.otp.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.otp.entity.OTP;
import com.enewschamp.app.otp.service.OTPService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("OTPPageHandler")
public class OTPPageHandler implements IPageHandler {
	@Autowired
	private OTPService otpService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;

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
		validate(pageData, this.getClass().getName());
		OTP otp = mapOTPData(pageRequest, pageData);
		if (StringUtils.isEmpty(otp.getOtpGenTime())) {
			otp.setOtpGenTime(LocalDateTime.now());
			otp.setOperationDateTime(LocalDateTime.now());
		}

		otp = otpService.create(otp);
		mapOTP(pageRequest, pageDto, otp);
		return pageDto;
	}

	private OTP mapOTPData(PageRequestDTO pageRequest, OTPPageData pageData) {
		OTP otp = modelMapper.map(pageData, OTP.class);
		otp.setRecordInUse(RecordInUseType.Y);
		otp.setOperationDateTime(LocalDateTime.now());
		return otp;
	}

	@SneakyThrows
	private PageDTO updateOTP(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		OTPPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), OTPPageData.class);
		validate(pageData, this.getClass().getName());
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
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<OTP> otpList = otpService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<OTPPageData> list = mapOTPData(otpList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
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

}
