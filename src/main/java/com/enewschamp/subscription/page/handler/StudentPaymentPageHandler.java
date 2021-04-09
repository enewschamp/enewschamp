package com.enewschamp.subscription.page.handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.TreeMap;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.KeyProperty;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.app.school.service.SchoolPricingService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.PaymentPageData;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.domain.business.SchoolDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentPaymentBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionPeriodBusiness;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;
import com.enewschamp.subscription.domain.service.StudentDetailsWorkService;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.subscription.domain.service.StudentSchoolWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;
import com.enewschamp.subscription.pricing.service.IndividualPricingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.pg.merchant.PaytmChecksum;

@Component(value = "StudentPaymentPageHandler")
public class StudentPaymentPageHandler implements IPageHandler {

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
	StudentPaymentBusiness studentPaymentBusiness;

	@Autowired
	IndividualPricingService individualPricingService;

	@Autowired
	private PropertiesBackendService propertiesService;

	@Autowired
	PageNavigationService pageNavigationService;

	@Autowired
	SchoolDetailsBusiness schoolDetailsBusiness;

	@Autowired
	StudentDetailsBusiness studentDetailsBusiness;

	@Autowired
	StudentSubscriptionWorkService studentSubscriptionWorkService;

	@Autowired
	StudentDetailsWorkService studentDetailsWorkService;

	@Autowired
	StudentSchoolWorkService studentSchoolWorkService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		TreeMap<String, String> paramMap = new TreeMap<String, String>();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		PaymentPageData pageData = new PaymentPageData();
		Long studentId = 0L;
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		if (studentControlWorkDTO != null) {
			studentId = studentControlWorkDTO.getStudentId();
		}
		StudentPaymentWork studentPaymentWork = studentPaymentWorkService.getByStudentIdAndEdition(studentId,
				editionId);
		StudentSubscriptionWork studentSubscriptionWork = studentSubscriptionWorkService.get(studentId, editionId);
		if ("Y".equals(studentSubscriptionWork.getAutoRenewal())) {
			paramMap = initiateSubscription(pageNavigationContext, studentPaymentWork,
					studentSubscriptionWork.getSubscriptionPeriod());
			studentSubscriptionWork.setSubscriptionId(paramMap.get("subscriptionId"));
			studentSubscriptionWorkService.update(studentSubscriptionWork);
		} else {
			paramMap = initiateTransaction(pageNavigationContext, studentPaymentWork);

		}
		pageData.setParamMap(paramMap);
		pageDto.setData(pageData);
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
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		studentPaymentBusiness.workToMaster(studentId, editionId);
		StudentPaymentWork studentPaymentWork = studentPaymentWorkService
				.getSuccessTransactionByStudentIdAndEdition(studentId, editionId);
		studentPaymentWorkService.delete(studentPaymentWork.getPaymentId());
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handlePreviousWorkDataPage(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageRequest.getHeader());
		String emailId = pageRequest.getHeader().getEmailId();
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		studentPaymentBusiness.workToMaster(studentId, editionId);
		StudentPaymentWork studentPaymentWork = studentPaymentWorkService.getByStudentIdAndEdition(studentId,
				editionId);
		if (studentPaymentWork != null) {
			studentPaymentWorkService.delete(studentPaymentWork.getPaymentId());
		}
		return pageDTO;
	}

	private TreeMap<String, String> initiateSubscription(PageNavigationContext pageNavigationContext,
			StudentPaymentWork studentPaymentWork, String subscriptionPeriod) {
		TreeMap<String, String> paramMap = new TreeMap<String, String>();
		StudentSubscriptionWork studentSubscriptionWork = null;
		try {
			studentSubscriptionWork = studentSubscriptionWorkService.get(studentPaymentWork.getStudentId(),
					studentPaymentWork.getEditionId());
			String module = pageNavigationContext.getPageRequest().getHeader().getModule();
			String subscriptionFrequency = subscriptionPeriod.substring(0, subscriptionPeriod.length() - 1);
			String subscriptionFrequencyUnit = subscriptionPeriod.substring(subscriptionPeriod.length() - 1,
					subscriptionPeriod.length());
			if ("M".equals(subscriptionFrequencyUnit)) {
				subscriptionFrequencyUnit = "MONTH";
			} else if ("Y".equals(subscriptionFrequencyUnit)) {
				subscriptionFrequencyUnit = "YEAR";
			}
			LocalDate subscriptionExpiryDate = LocalDate.now();
			subscriptionExpiryDate = subscriptionExpiryDate.plusYears(Long
					.valueOf(propertiesService.getValue(module, PropertyConstants.PAYTM_SUBSCRIPTION_EXPIRY_YEARS)));
			subscriptionExpiryDate = subscriptionExpiryDate.plusDays(3);
			String subscriptionExpiryDateStr = subscriptionExpiryDate.getYear() + "-"
					+ ((subscriptionExpiryDate.getMonthValue() > 9) ? subscriptionExpiryDate.getMonthValue()
							: "0" + subscriptionExpiryDate.getMonthValue())
					+ "-" + ((subscriptionExpiryDate.getDayOfMonth() > 9) ? subscriptionExpiryDate.getDayOfMonth()
							: "0" + subscriptionExpiryDate.getDayOfMonth());
			LocalDate subscriptionStartDate = LocalDate.now();
			String subscriptionStartDateStr = subscriptionStartDate.getYear() + "-"
					+ ((subscriptionStartDate.getMonthValue() > 9) ? subscriptionStartDate.getMonthValue()
							: "0" + subscriptionStartDate.getMonthValue())
					+ "-" + ((subscriptionStartDate.getDayOfMonth() > 9) ? subscriptionStartDate.getDayOfMonth()
							: "0" + subscriptionStartDate.getDayOfMonth());
			String orderId = studentPaymentWork.getOrderId();
			String amountType = propertiesService.getValue(module, PropertyConstants.PAYTM_SUBSCRIPTION_AMOUNT_TYPE);
			JSONObject paytmParams = new JSONObject();
			JSONObject body = new JSONObject();
			body.put("requestType",
					propertiesService.getValue(module, PropertyConstants.PAYTM_REQUEST_TYPE_SUBSCRIPTION));
			body.put("mid", KeyProperty.MID);
			body.put("websiteName", propertiesService.getValue(module, PropertyConstants.PAYTM_WEBSITE));
			body.put("orderId", orderId);
			body.put("callbackUrl", propertiesService.getValue(module, PropertyConstants.PAYTM_CALLBACK_URL));
			body.put("subscriptionAmountType", amountType);
			studentSubscriptionWork.setSubscriptionAmountType(amountType);
			body.put("subscriptionFrequency", subscriptionFrequency);
			studentSubscriptionWork.setSubscriptionFrequency(subscriptionFrequency);
			body.put("subscriptionFrequencyUnit", subscriptionFrequencyUnit);
			studentSubscriptionWork.setSubscriptionFrequencyUnit(subscriptionFrequencyUnit);
			body.put("subscriptionExpiryDate", subscriptionExpiryDateStr);
			studentSubscriptionWork.setSubscriptionExpiryDate(subscriptionExpiryDateStr);
			body.put("subscriptionEnableRetry",
					propertiesService.getValue(module, PropertyConstants.PAYTM_SUBSCRIPTION_ENABLE_RETRY));
			body.put("subscriptionStartDate", subscriptionStartDateStr);
			body.put("subscriptionGraceDays",
					propertiesService.getValue(module, PropertyConstants.PAYTM_SUBSCRIPTION_GRACE_DAYS));
			String txAmtStr = "" + studentPaymentWork.getPaymentAmount() + "0";
			JSONObject txnAmount = new JSONObject();
			txnAmount.put("value", txAmtStr);
			txnAmount.put("currency", studentPaymentWork.getPaymentCurrency());
			JSONObject userInfo = new JSONObject();
			userInfo.put("custId", "" + studentPaymentWork.getStudentId());
			body.put("txnAmount", txnAmount);
			body.put("userInfo", userInfo);
			String signature = PaytmChecksum.generateSignature(body.toString(), KeyProperty.MERCHANT_KEY);
			JSONObject head = new JSONObject();
			head.put("signature", signature);
			paytmParams.put("body", body);
			paytmParams.put("head", head);
			String post_data = paytmParams.toString();
			System.out.println(">>>>>>>post_data>>>>>>>>" + post_data);
			URL url = new URL(propertiesService.getValue(module, PropertyConstants.PAYTM_INITIATE_SUBSCRIPTION_URL)
					+ "?mid=" + KeyProperty.MID + "&orderId=" + orderId);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(post_data);
			Blob initTransReqPayload = null;
			post_data = post_data.replace(KeyProperty.MID, "XXXXXXXXXXXXXX");
			post_data = post_data.replace(txAmtStr, "XX.XX");
			initTransReqPayload = new SerialBlob(post_data.getBytes());
			studentPaymentWork.setInitTranApiRequest(initTransReqPayload);
			requestWriter.close();
			String responseData = "";
			InputStream is = connection.getInputStream();
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
			if ((responseData = responseReader.readLine()) != null) {
				Blob initTranResPayload = null;
				String response_data = responseData.replace(KeyProperty.MID, "XXXXXXXXXXXXXX");
				response_data = response_data.replace(txAmtStr, "XX.XX");
				initTranResPayload = new SerialBlob(response_data.getBytes());
				studentPaymentWork.setInitTranApiResponse(initTranResPayload);
				JSONParser parser = new JSONParser();
				try {
					JSONObject json = (JSONObject) parser.parse(responseData);
					System.out.println(">>>>>>>>>>>responseData>>>>>>>>>>>" + responseData);
					if (json.get("body") != null) {
						JSONObject jsonBody = (JSONObject) parser.parse(json.get("body").toString());
						String txnToken = jsonBody.get("txnToken").toString();
						String subscriptionId = jsonBody.get("subscriptionId").toString();
						studentPaymentWork.setSubscriptionId(subscriptionId);
						paramMap.put("url",
								propertiesService.getValue(module, PropertyConstants.PAYTM_SHOW_PAYMENTS_PAGE_URL)
										+ "?mid=" + KeyProperty.MID + "&orderId=" + orderId);
						paramMap.put("mid", KeyProperty.MID);
						paramMap.put("orderId", orderId);
						paramMap.put("txnToken", txnToken);
						paramMap.put("subscriptionId", subscriptionId);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			responseReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		if (studentPaymentWork != null) {
			studentPaymentWorkService.update(studentPaymentWork);
		}
		if (studentSubscriptionWork != null) {
			studentSubscriptionWorkService.update(studentSubscriptionWork);
		}
		return paramMap;
	}

	private TreeMap<String, String> initiateTransaction(PageNavigationContext pageNavigationContext,
			StudentPaymentWork studentPaymentWork) {
		TreeMap<String, String> paramMap = new TreeMap<String, String>();
		try {
			String orderId = studentPaymentWork.getOrderId();
			String module = pageNavigationContext.getPageRequest().getHeader().getModule();
			JSONObject paytmParams = new JSONObject();
			JSONObject body = new JSONObject();
			body.put("requestType", propertiesService.getValue(module, PropertyConstants.PAYTM_REQUEST_TYPE_PAYMENT));
			body.put("mid", KeyProperty.MID);
			body.put("websiteName", propertiesService.getValue(module, PropertyConstants.PAYTM_WEBSITE));
			body.put("orderId", orderId);
			body.put("callbackUrl", propertiesService.getValue(module, PropertyConstants.PAYTM_CALLBACK_URL));
			JSONObject txnAmount = new JSONObject();
			txnAmount.put("value", "" + studentPaymentWork.getPaymentAmount());
			txnAmount.put("currency", studentPaymentWork.getPaymentCurrency());
			JSONObject userInfo = new JSONObject();
			userInfo.put("custId", studentPaymentWork.getStudentId());
			body.put("txnAmount", txnAmount);
			body.put("userInfo", userInfo);
			String signature = PaytmChecksum.generateSignature(body.toString(), KeyProperty.MERCHANT_KEY);
			JSONObject head = new JSONObject();
			head.put("signature", signature);
			paytmParams.put("body", body);
			paytmParams.put("head", head);
			String post_data = paytmParams.toString();
			URL url = new URL(propertiesService.getValue(module, PropertyConstants.PAYTM_INITIATE_TRANSACTION_URL)
					+ "?mid=" + KeyProperty.MID + "&orderId=" + orderId);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(post_data);
			Blob initTransReqPayload = null;
			post_data = post_data.replace(KeyProperty.MID, "XXXXXXXXXXXXXX");
			post_data = post_data.replace("" + txnAmount, "XX.XX");
			initTransReqPayload = new SerialBlob(post_data.getBytes());
			studentPaymentWork.setInitTranApiRequest(initTransReqPayload);
			requestWriter.close();
			String responseData = "";
			InputStream is = connection.getInputStream();
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
			if ((responseData = responseReader.readLine()) != null) {
				Blob initTranResPayload = null;
				String response_data = responseData.replace(KeyProperty.MID, "XXXXXXXXXXXXXX");
				response_data = response_data.replace("" + txnAmount, "XX.XX");
				initTranResPayload = new SerialBlob(response_data.getBytes());
				studentPaymentWork.setInitTranApiResponse(initTranResPayload);
				JSONParser parser = new JSONParser();
				try {
					JSONObject json = (JSONObject) parser.parse(responseData);
					if (json.get("body") != null) {
						JSONObject jsonBody = (JSONObject) parser.parse(json.get("body").toString());
						String txnToken = jsonBody.get("txnToken").toString();
						paramMap.put("url",
								propertiesService.getValue(module, PropertyConstants.PAYTM_SHOW_PAYMENTS_PAGE_URL)
										+ "?mid=" + KeyProperty.MID + "&orderId=" + orderId);
						paramMap.put("mid", KeyProperty.MID);
						paramMap.put("orderId", orderId);
						paramMap.put("txnToken", txnToken);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			responseReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		if (studentPaymentWork != null) {
			studentPaymentWorkService.update(studentPaymentWork);
		}
		return paramMap;
	}
}
