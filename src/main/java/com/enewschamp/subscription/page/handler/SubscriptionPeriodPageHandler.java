package com.enewschamp.subscription.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageSaveTable;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.app.school.entity.SchoolPricing;
import com.enewschamp.app.school.service.SchoolPricingService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolWorkDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionWorkDTO;
import com.enewschamp.subscription.app.dto.SubscriptionPeriodPageData;
import com.enewschamp.subscription.common.SubscriptionType;
import com.enewschamp.subscription.domain.business.SchoolDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionPeriodBusiness;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;
import com.enewschamp.subscription.pricing.entity.IndividualPricing;
import com.enewschamp.subscription.pricing.service.IndividualPricingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "SubscriptionPeriodPageHandler")
public class SubscriptionPeriodPageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SubscriptionPeriodBusiness subscriptionPeriodBusiness;

	@Autowired
	SchoolPricingService schoolPricingService;

	@Autowired
	StudentPaymentWorkService studentPaymentWorkService;

	@Autowired
	IndividualPricingService individualPricingService;

	@Autowired
	PageNavigationService pageNavigationService;

	@Autowired
	SchoolDetailsBusiness schoolDetailsBusiness;

	@Autowired
	StudentSubscriptionWorkService studentSubscriptionWorkService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		String methodName = pageNavigationContext.getLoadMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[1];
			params[0] = PageNavigationContext.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
				return (PageDTO) m.invoke(this, pageNavigationContext);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
				}
			} catch (NoSuchMethodException nsmEx) {
				nsmEx.printStackTrace();
			} catch (SecurityException seEx) {
				seEx.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadNextPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		Long studentId = pageNavigationContext.getPageRequest().getHeader().getStudentId();
		String subscriptionType = "";
		SubscriptionPeriodPageData subscriptionPeriodPageData = new SubscriptionPeriodPageData();
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(studentId);
		if (studentControlWorkDTO != null) {
			subscriptionType = studentControlWorkDTO.getSubscriptionTypeW();
		}
		subscriptionPeriodPageData = mapPricingDetails(subscriptionPeriodPageData, subscriptionType, studentId,
				editionId);
		pageDto.setData(subscriptionPeriodPageData);
		return pageDto;
	}

	public PageDTO loadWorkDataPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		Long studentId = pageNavigationContext.getPageRequest().getHeader().getStudentId();
		String subscriptionType = "";
		SubscriptionPeriodPageData subscriptionPeriodPageData = new SubscriptionPeriodPageData();
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(studentId);
		if (studentControlWorkDTO != null) {
			studentId = studentControlWorkDTO.getStudentId();
			subscriptionType = studentControlWorkDTO.getSubscriptionTypeW();
		}
		StudentSubscriptionWorkDTO studentSubscriptionWorkDTO = subscriptionBusiness
				.getStudentSubscriptionFromWork(studentId, editionId);
		if (studentSubscriptionWorkDTO != null) {
			subscriptionPeriodPageData = modelMapper.map(studentSubscriptionWorkDTO, SubscriptionPeriodPageData.class);
			subscriptionPeriodPageData.setAutoRenew(studentSubscriptionWorkDTO.getAutoRenewal());
			subscriptionPeriodPageData
					.setSubscriptionPeriodSelected(studentSubscriptionWorkDTO.getSubscriptionPeriod());
		}
		subscriptionPeriodPageData = mapPricingDetails(subscriptionPeriodPageData, subscriptionType, studentId,
				editionId);
		pageDto.setData(subscriptionPeriodPageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		Long studentId = pageRequest.getHeader().getStudentId();
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(studentId);
		if (studentControlWorkDTO != null) {
			studentId = studentControlWorkDTO.getStudentId();
		}
		String editionId = pageRequest.getHeader().getEditionId();
		StudentSubscriptionDTO studentSubscriotionDTO = subscriptionBusiness.getStudentSubscriptionFromMaster(studentId,
				editionId);
		SubscriptionPeriodPageData subscriptionPeriodPageData = null;
		subscriptionPeriodPageData = mapPagedata(pageRequest);
		String subscriptionPeriod = subscriptionPeriodPageData.getSubscriptionPeriodSelected();
		String autoRenew = subscriptionPeriodPageData.getAutoRenew();
		int days = 0;
		if (subscriptionPeriod.endsWith("D")) {
			days = Integer.valueOf(subscriptionPeriod.substring(0, (subscriptionPeriod.length() - 1)));
		} else if (subscriptionPeriod.endsWith("M")) {
			days = Integer.valueOf(subscriptionPeriod.substring(0, (subscriptionPeriod.length() - 1))) * 30;
		} else if (subscriptionPeriod.endsWith("Y")) {
			days = Integer.valueOf(subscriptionPeriod.substring(0, (subscriptionPeriod.length() - 1))) * 365;
		}
		// calculate the start and end date..
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = startDate.plusDays(days);
		// set the calculated dates in the subscription object..
		studentSubscriotionDTO.setStartDate(startDate);
		studentSubscriotionDTO.setEndDate(endDate);
		studentSubscriotionDTO.setAutoRenewal(autoRenew);
		subscriptionBusiness.saveAsMaster(studentSubscriotionDTO);
		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		String methodName = pageNavigatorDTO.getSubmissionMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[2];
			params[0] = PageRequestDTO.class;
			params[1] = PageNavigatorDTO.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
				}
			} catch (NoSuchMethodException nsmEx) {
				nsmEx.printStackTrace();
			} catch (SecurityException seEx) {
				seEx.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		return pageDTO;
	}

	public PageDTO handleNextAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		Long studentId = pageRequest.getHeader().getStudentId();
		String editionId = pageRequest.getHeader().getEditionId();
		String saveIn = pageNavigatorDTO.getUpdationTable();
		String operation = pageRequest.getHeader().getOperation();
		if (PageSaveTable.W.toString().equals(saveIn)) {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(studentId);
			studentId = studentControlWorkDTO.getStudentId();
			StudentSubscriptionWorkDTO studentSubscpritionWorkDTO = subscriptionBusiness
					.getStudentSubscriptionFromWork(studentId, editionId);
			SubscriptionPeriodPageData subscriptionPeriodPageData = mapPagedata(pageRequest);
			String subscriptionPeriod = subscriptionPeriodPageData.getSubscriptionPeriodSelected();
			String autoRenew = subscriptionPeriodPageData.getAutoRenew();
			Double feeAmount = subscriptionPeriodPageData.getSubscriptionFee();
			String feeCurrency = subscriptionPeriodPageData.getSubscriptionFeeCurrency();
			studentSubscpritionWorkDTO.setAutoRenewal(autoRenew);
			studentSubscpritionWorkDTO.setSubscriptionPeriod(subscriptionPeriod);
			studentSubscpritionWorkDTO.setOperatorId("" + studentId);
			studentSubscpritionWorkDTO.setRecordInUse(RecordInUseType.Y);
			StudentPaymentWork studentPaymentWork = new StudentPaymentWork();
			studentPaymentWork.setStudentId(studentId);
			studentPaymentWork.setEditionId(editionId);
			studentPaymentWork.setPaymentAmount(feeAmount);
			studentPaymentWork.setPaymentCurrency(feeCurrency);
			studentPaymentWork.setSubscriptionPeriod(subscriptionPeriod);
			studentPaymentWork.setOperatorId("" + studentId);
			studentPaymentWork.setRecordInUse(RecordInUseType.Y);
			studentPaymentWork.setSubscriptionType(studentSubscpritionWorkDTO.getSubscriptionSelected());
			String orderId = generateOrderId(studentId, studentSubscpritionWorkDTO.getSubscriptionSelected());
			studentPaymentWork.setOrderId(orderId);
			studentSubscpritionWorkDTO.setOrderId(orderId);
			subscriptionBusiness.updateSubscriptionPeriodInWork(studentSubscpritionWorkDTO);
			studentPaymentWork = studentPaymentWorkService.create(studentPaymentWork);
			studentPaymentWork.getPaymentId();
			studentControlWorkDTO.setNextPageOperation(operation);
			studentControlBusiness.saveAsWork(studentControlWorkDTO);
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handlePreviousWorkDataPage(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		Long studentId = pageRequest.getHeader().getStudentId();
		String editionId = pageRequest.getHeader().getEditionId();
		String saveIn = pageNavigatorDTO.getUpdationTable();
		String operation = pageRequest.getHeader().getOperation();
		if (PageSaveTable.W.toString().equals(saveIn)) {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(studentId);
			StudentSubscriptionWorkDTO studentSubscpritionWorkDTO = subscriptionBusiness
					.getStudentSubscriptionFromWork(studentId, editionId);
			SubscriptionPeriodPageData subscriptionPeriodPageData = mapPagedata(pageRequest);
			String subscriptionPeriod = subscriptionPeriodPageData.getSubscriptionPeriodSelected();
			String autoRenew = subscriptionPeriodPageData.getAutoRenew();
			Double feeAmount = subscriptionPeriodPageData.getSubscriptionFee();
			String feeCurrency = subscriptionPeriodPageData.getSubscriptionFeeCurrency();
			studentSubscpritionWorkDTO.setAutoRenewal(autoRenew);
			studentSubscpritionWorkDTO.setSubscriptionPeriod(subscriptionPeriod);
			studentSubscpritionWorkDTO.setOperatorId("" + studentId);
			studentSubscpritionWorkDTO.setRecordInUse(RecordInUseType.Y);
			StudentPaymentWork studentPaymentWork = new StudentPaymentWork();
			studentPaymentWork.setStudentId(studentId);
			studentPaymentWork.setEditionId(editionId);
			studentPaymentWork.setPaymentAmount(feeAmount);
			studentPaymentWork.setSubscriptionPeriod(subscriptionPeriod);
			studentPaymentWork.setPaymentCurrency(feeCurrency);
			studentPaymentWork.setOperatorId("" + studentId);
			studentPaymentWork.setRecordInUse(RecordInUseType.Y);
			studentPaymentWork.setSubscriptionType(studentSubscpritionWorkDTO.getSubscriptionSelected());
			studentPaymentWork
					.setOrderId(generateOrderId(studentId, studentSubscpritionWorkDTO.getSubscriptionSelected()));
			subscriptionBusiness.updateSubscriptionPeriodInWork(studentSubscpritionWorkDTO);
			studentPaymentWork = studentPaymentWorkService.create(studentPaymentWork);
			studentPaymentWork.getPaymentId();
			studentControlWorkDTO.setNextPageOperation(operation);
			studentControlBusiness.saveAsWork(studentControlWorkDTO);
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	private String generateOrderId(Long studentId, String subscriptionType) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHmmss");
		String orderId = "ENC" + "_" + sdf.format(new Date()) + "_" + studentId;
		return orderId;
	}

	public SubscriptionPeriodPageData mapPagedata(PageRequestDTO pageRequest) {
		SubscriptionPeriodPageData subscriptionPeriodPageData = null;
		try {
			subscriptionPeriodPageData = objectMapper.readValue(pageRequest.getData().toString(),
					SubscriptionPeriodPageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
		}
		return subscriptionPeriodPageData;
	}

	private SubscriptionPeriodPageData mapPricingDetails(SubscriptionPeriodPageData subscriptionPeriodPageData,
			String subscriptionType, Long studentId, String editionId) {
		if (SubscriptionType.P.toString().equalsIgnoreCase(subscriptionType)) {
			// fetch individual pricing
			IndividualPricing individualPricing = individualPricingService.getPricingForIndividual(editionId);
			subscriptionPeriodPageData = modelMapper.map(individualPricing, SubscriptionPeriodPageData.class);
		} else if (SubscriptionType.S.toString().equalsIgnoreCase(subscriptionType)) {
			// fetch the student school
			Long schoolId = 0L;
			StudentSchoolWorkDTO studentSchoolWorkDTO = schoolDetailsBusiness.getStudentFromWork(studentId);
			if (studentSchoolWorkDTO != null) {
				if ("N".equalsIgnoreCase(studentSchoolWorkDTO.getSchoolNotInTheList())) {
					schoolId = Long.valueOf(studentSchoolWorkDTO.getSchool());
				}
			}
			SchoolPricing schoolPricing = schoolPricingService.getPricingForInstitution(schoolId, editionId);
			subscriptionPeriodPageData = modelMapper.map(schoolPricing, SubscriptionPeriodPageData.class);
		}
		return subscriptionPeriodPageData;
	}

}