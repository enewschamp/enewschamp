package com.enewschamp.subscription.page.handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		PaymentPageData pageData = new PaymentPageData();
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		StudentPaymentWork studentPaymentWork = studentPaymentWorkService
				.getByStudentIdAndEdition(studentControlWorkDTO.getStudentId(), editionId);
		TreeMap<String, String> paramMap = new TreeMap<String, String>();
		try {
			String mid = propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
					PropertyConstants.PAYTM_MID);
			String orderId = studentPaymentWork.getOrderId();
			JSONObject paytmParams = new JSONObject();
			JSONObject body = new JSONObject();
			body.put("requestType", "Payment");
			body.put("mid", mid);
			body.put("websiteName", "WEBSTAGING");
			body.put("orderId", orderId);
			body.put("callbackUrl",
					propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
							PropertyConstants.PAYTM_CALLBACK_URL));
			JSONObject txnAmount = new JSONObject();
			txnAmount.put("value", studentPaymentWork.getPaymentAmount());
			txnAmount.put("currency", studentPaymentWork.getPaymentCurrency());
			JSONObject userInfo = new JSONObject();
			userInfo.put("custId", studentPaymentWork.getStudentId());
			body.put("txnAmount", txnAmount);
			body.put("userInfo", userInfo);
			String signature = PaytmChecksum.generateSignature(body.toString(),
					propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
							PropertyConstants.PAYTM_MERCHANT_KEY));
			JSONObject head = new JSONObject();
			head.put("signature", signature);
			paytmParams.put("body", body);
			paytmParams.put("head", head);
			String post_data = paytmParams.toString();
			URL url = new URL(propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
					PropertyConstants.PAYTM_INITIATE_TRANSACTION_URL) + "?mid=" + mid + "&orderId=" + orderId);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(post_data);
			Blob initTransReqPayload = null;
			initTransReqPayload = new SerialBlob(post_data.getBytes());
			studentPaymentWork.setInitTranApiRequest(initTransReqPayload);
			requestWriter.close();
			String responseData = "";
			InputStream is = connection.getInputStream();
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
			if ((responseData = responseReader.readLine()) != null) {
				Blob initTranResPayload = null;
				initTranResPayload = new SerialBlob(responseData.getBytes());
				studentPaymentWork.setInitTranApiResponse(initTranResPayload);
				JSONParser parser = new JSONParser();
				try {
					JSONObject json = (JSONObject) parser.parse(responseData);
					if (json.get("body") != null) {
						JSONObject jsonBody = (JSONObject) parser.parse(json.get("body").toString());
						String txnToken = jsonBody.get("txnToken").toString();
						paramMap.put("url",
								propertiesService.getValue(
										pageNavigationContext.getPageRequest().getHeader().getModule(),
										PropertyConstants.PAYTM_SHOW_PAYMENTS_PAGE_URL) + "?mid=" + mid + "&orderId="
										+ orderId);
						paramMap.put("mid", mid);
						paramMap.put("orderId", orderId);
						paramMap.put("txnToken", txnToken);
					}
					pageData.setParamMap(paramMap);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
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
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			try {
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
					// e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
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

	public PaymentPageData mapPagedata(PageRequestDTO pageRequest) {
		PaymentPageData paymentPageData = null;
		try {
			paymentPageData = objectMapper.readValue(pageRequest.getData().toString(), PaymentPageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
		}
		return paymentPageData;
	}

}
