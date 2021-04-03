//package com.enewschamp.subscription.page.handler;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.TreeMap;
//
//import org.json.simple.JSONObject;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.enewschamp.app.common.ErrorCodeConstants;
//import com.enewschamp.app.common.PageDTO;
//import com.enewschamp.app.common.PageRequestDTO;
//import com.enewschamp.app.common.PropertyConstants;
//import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
//import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
//import com.enewschamp.app.school.service.SchoolPricingService;
//import com.enewschamp.common.domain.service.PropertiesBackendService;
//import com.enewschamp.domain.common.IPageHandler;
//import com.enewschamp.domain.common.PageNavigationContext;
//import com.enewschamp.problem.BusinessException;
//import com.enewschamp.subscription.app.dto.PaymentPageData;
//import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
//import com.enewschamp.subscription.domain.business.SchoolDetailsBusiness;
//import com.enewschamp.subscription.domain.business.StudentControlBusiness;
//import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
//import com.enewschamp.subscription.domain.business.StudentPaymentBusiness;
//import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
//import com.enewschamp.subscription.domain.business.SubscriptionPeriodBusiness;
//import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
//import com.enewschamp.subscription.domain.service.StudentDetailsWorkService;
//import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
//import com.enewschamp.subscription.domain.service.StudentSchoolWorkService;
//import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;
//import com.enewschamp.subscription.pricing.service.IndividualPricingService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.paytm.pg.merchant.CheckSumServiceHelper;
//
//@Component(value = "FeePaymentPageHandler")
//public class FeePaymentPageHandler implements IPageHandler {
//
//	@Autowired
//	ObjectMapper objectMapper;
//
//	@Autowired
//	StudentControlBusiness studentControlBusiness;
//
//	@Autowired
//	SubscriptionBusiness subscriptionBusiness;
//
//	@Autowired
//	ModelMapper modelMapper;
//
//	@Autowired
//	SubscriptionPeriodBusiness subscriptionPeriodBusiness;
//
//	@Autowired
//	SchoolPricingService schoolPricingService;
//
//	@Autowired
//	StudentPaymentWorkService studentPaymentWorkService;
//
//	@Autowired
//	StudentPaymentBusiness studentPaymentBusiness;
//
//	@Autowired
//	IndividualPricingService individualPricingService;
//
//	@Autowired
//	private PropertiesBackendService propertiesService;
//
//	@Autowired
//	PageNavigationService pageNavigationService;
//
//	@Autowired
//	SchoolDetailsBusiness schoolDetailsBusiness;
//
//	@Autowired
//	StudentDetailsBusiness studentDetailsBusiness;
//
//	@Autowired
//	StudentSubscriptionWorkService studentSubscriptionWorkService;
//
//	@Autowired
//	StudentDetailsWorkService studentDetailsWorkService;
//
//	@Autowired
//	StudentSchoolWorkService studentSchoolWorkService;
//
//	@Override
//	public PageDTO handleAction(PageRequestDTO pageRequest) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
//		PageDTO pageDto = new PageDTO();
//		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
//		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
//		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
//		PaymentPageData pageData = new PaymentPageData();
//		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
//		StudentPaymentWork studentPaymentWork = studentPaymentWorkService
//				.getByStudentIdAndEdition(studentControlWorkDTO.getStudentId(), editionId);
//		TreeMap<String, String> paramMap = new TreeMap<String, String>();
//		paramMap.put("MID", propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
//				PropertyConstants.PAYTM_DETAILS_MID));
//		paramMap.put("ORDER_ID", studentPaymentWork.getOrderId());
//		paramMap.put("CUST_ID", "" + studentPaymentWork.getStudentId());
//		paramMap.put("INDUSTRY_TYPE_ID",
//				propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
//						PropertyConstants.PAYTM_DETAILS_INDUSTRY_TYPE_ID));
//		paramMap.put("CHANNEL_ID",
//				propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
//						PropertyConstants.PAYTM_DETAILS_CHANNEL_ID));
//		paramMap.put("TXN_AMOUNT", "" + studentPaymentWork.getPaymentAmount());
//		paramMap.put("WEBSITE",
//				propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
//						PropertyConstants.PAYTM_DETAILS_WEBSITE));
//		paramMap.put("EMAIL", "");
//		paramMap.put("MOBILE_NO", "");
//		paramMap.put("CALLBACK_URL",
//				propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
//						PropertyConstants.PAYTM_DETAILS_CALLBACK_URL) + paramMap.get("ORDER_ID"));
//		try {
//			String checkSum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(
//					propertiesService.getValue(pageNavigationContext.getPageRequest().getHeader().getModule(),
//							PropertyConstants.PAYTM_DETAILS_MERCHANT_KEY),
//					paramMap);
//			paramMap.put("CHECKSUMHASH", checkSum);
//			pageData.setParamMap(paramMap);
//			pageDto.setData(pageData);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return pageDto;
//	}
//
//	private boolean checkTransactionStatus(PageRequestDTO pageRequest) {
//		/* initialize an object */
//		JSONObject paytmParams = new JSONObject();
//
//		/* body parameters */
//		JSONObject body = new JSONObject();
//
//		/*
//		 * Find your MID in your Paytm Dashboard at
//		 * https://dashboard.paytm.com/next/apikeys
//		 */
//		body.put("mid", "YOUR_MID_HERE");
//
//		/* Enter your order id which needs to be check status for */
//		body.put("orderId", "YOUR_ORDER_ID");
//
//		/**
//		 * Generate checksum by parameters we have in body You can get Checksum JAR from
//		 * https://developer.paytm.com/docs/checksum/ Find your Merchant Key in your
//		 * Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
//		 */
//		String checksum = "";
//		try {
//			checksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum("YOUR_KEY_HERE",
//					body.toString());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		/* head parameters */
//		JSONObject head = new JSONObject();
//		/* put generated checksum value here */
//		head.put("signature", checksum);
//		/* prepare JSON string for request */
//		paytmParams.put("body", body);
//		paytmParams.put("head", head);
//		String post_data = paytmParams.toString();
//		/* for Staging */
//		try {
//			URL url = new URL("https://securegw-stage.paytm.in/merchant-status/api/v1/getPaymentStatus");
//
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setRequestMethod("POST");
//			connection.setRequestProperty("Content-Type", "application/json");
//			connection.setDoOutput(true);
//
//			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
//			requestWriter.writeBytes(post_data);
//			requestWriter.close();
//			String responseData = "";
//			InputStream is = connection.getInputStream();
//			BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
//			if ((responseData = responseReader.readLine()) != null) {
//				System.out.append("Response: " + responseData);
//			}
//			// System.out.append("Request: " + post_data);
//			responseReader.close();
//		} catch (Exception exception) {
//			exception.printStackTrace();
//		}
//		return true;
//
//	}
//
//	@Override
//	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
//		String methodName = pageNavigatorDTO.getSubmissionMethod();
//		if (methodName != null && !"".equals(methodName)) {
//			Class[] params = new Class[2];
//			params[0] = PageRequestDTO.class;
//			params[1] = PageNavigatorDTO.class;
//			Method m = null;
//			try {
//				m = this.getClass().getDeclaredMethod(methodName, params);
//			} catch (NoSuchMethodException e1) {
//				e1.printStackTrace();
//			} catch (SecurityException e1) {
//				e1.printStackTrace();
//			}
//			try {
//				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
//			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				if (e.getCause() instanceof BusinessException) {
//					throw ((BusinessException) e.getCause());
//				} else {
//					e.printStackTrace();
//				}
//			} catch (SecurityException e) {
//				e.printStackTrace();
//			}
//		}
//		PageDTO pageDTO = new PageDTO();
//		return pageDTO;
//	}
//
//	public PageDTO handleActionOnTransactionResponse(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
//		PageDTO pageDTO = new PageDTO();
//		Long studentId = 0L;
//		String emailId = pageRequest.getHeader().getEmailId();
//		String editionId = pageRequest.getHeader().getEditionId();
//		String operation = pageRequest.getHeader().getOperation();
//		String module = pageRequest.getHeader().getModule();
//		PaymentPageData paymentPagedata = mapPagedata(pageRequest);
//		TreeMap<String, String> inResponse = paymentPagedata.getInResponse();
//		String checkSumHash = inResponse.get("CHECKSUMHASH");
//		boolean isValidChecksum = false;
//		try {
//			isValidChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(
//					propertiesService.getValue(module, PropertyConstants.PAYTM_DETAILS_MERCHANT_KEY), inResponse,
//					checkSumHash);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (isValidChecksum) {
//			System.out.append("Checksum Matched");
//		} else {
//			System.out.append("Checksum Mismatched");
//		}
//		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
//		studentId = studentControlWorkDTO.getStudentId();
//		StudentPaymentWork studentPaymentWork = studentPaymentWorkService.getByStudentIdAndEdition(studentId,
//				editionId);
//		studentControlBusiness.updateAsWork(studentControlWorkDTO);
//		subscriptionBusiness.workToMaster(module, studentId, editionId);
//		studentPaymentBusiness.workToMaster(studentId, editionId);
//		studentControlBusiness.workToMaster(studentId);
//		if ("SchoolSubs".equalsIgnoreCase(operation)) {
//			studentDetailsBusiness.workToMaster(studentId);
//			studentDetailsWorkService.delete(studentId);
//			schoolDetailsBusiness.workToMaster(studentId);
//			studentSchoolWorkService.delete(studentId);
//		}
//		studentPaymentWorkService.delete(studentPaymentWork.getPaymentId());
//		studentSubscriptionWorkService.delete(studentId);
//		pageDTO.setHeader(pageRequest.getHeader());
//		return pageDTO;
//	}
//
//	@Override
//	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
//		PageDTO pageDTO = new PageDTO();
//		String emailId = pageRequest.getHeader().getEmailId();
//		String editionId = pageRequest.getHeader().getEditionId();
//		Long studentId = studentControlBusiness.getStudentId(emailId);
//		studentPaymentBusiness.workToMaster(studentId, editionId);
//		StudentPaymentWork studentPaymentWork = studentPaymentWorkService.getByStudentIdAndEdition(studentId,
//				editionId);
//		studentPaymentWorkService.delete(studentPaymentWork.getPaymentId());
//		pageDTO.setHeader(pageRequest.getHeader());
//		return pageDTO;
//	}
//
//	public PageDTO handleActionSomeUIErrorOccurred(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
//		PageDTO pageDTO = new PageDTO();
//		PaymentPageData paymentPagedata = mapPagedata(pageRequest);
//		pageDTO.setErrorMessage("UI Error " + paymentPagedata.getInErrorMessage());
//		pageDTO.setHeader(pageRequest.getHeader());
//		return pageDTO;
//	}
//
//	public PageDTO handleActionNetworkNotAvailable(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
//		PageDTO pageDTO = new PageDTO();
//		pageDTO.setErrorMessage("Network connection error: Check your internet connectivity");
//		pageDTO.setHeader(pageRequest.getHeader());
//		return pageDTO;
//	}
//
//	public PageDTO handleActionClientAuthenticationFailed(PageRequestDTO pageRequest,
//			PageNavigatorDTO pageNavigatorDTO) {
//		PageDTO pageDTO = new PageDTO();
//		PaymentPageData paymentPagedata = mapPagedata(pageRequest);
//		pageDTO.setErrorMessage("Authentication failed: Server error " + paymentPagedata.getInErrorMessage());
//		pageDTO.setHeader(pageRequest.getHeader());
//		return pageDTO;
//	}
//
//	public PageDTO handleActionOnErrorLoadingWebPage(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
//		PageDTO pageDTO = new PageDTO();
//		PaymentPageData paymentPagedata = mapPagedata(pageRequest);
//		pageDTO.setErrorMessage("Unable to load webpage " + paymentPagedata.getInErrorMessage());
//		pageDTO.setHeader(pageRequest.getHeader());
//		return pageDTO;
//	}
//
//	public PageDTO handleActionOnBackPressedCancelTransaction(PageRequestDTO pageRequest,
//			PageNavigatorDTO pageNavigatorDTO) {
//		PageDTO pageDTO = new PageDTO();
//		pageDTO.setErrorMessage("Transaction cancelled");
//		pageDTO.setHeader(pageRequest.getHeader());
//		return pageDTO;
//	}
//
//	public PageDTO handleActionOnTransactionCancel(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
//		PageDTO pageDTO = new PageDTO();
//		PaymentPageData paymentPagedata = mapPagedata(pageRequest);
//		pageDTO.setErrorMessage("Transaction cancelled" + paymentPagedata.getInErrorMessage());
//		pageDTO.setHeader(pageRequest.getHeader());
//		return pageDTO;
//	}
//
//	public PaymentPageData mapPagedata(PageRequestDTO pageRequest) {
//		PaymentPageData paymentPageData = null;
//		try {
//			paymentPageData = objectMapper.readValue(pageRequest.getData().toString(), PaymentPageData.class);
//		} catch (IOException e) {
//			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
//		}
//		return paymentPageData;
//	}
//
//}
