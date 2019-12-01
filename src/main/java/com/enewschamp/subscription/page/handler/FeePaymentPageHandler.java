package com.enewschamp.subscription.page.handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.app.school.service.SchoolPricingService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.PaymentPageData;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.domain.business.SchoolDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentPaymentBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionPeriodBusiness;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;
import com.enewschamp.subscription.pricing.service.IndividualPricingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.pg.merchant.CheckSumServiceHelper;

@Component(value = "FeePaymentPageHandler")
public class FeePaymentPageHandler implements IPageHandler {

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
	private EnewschampApplicationProperties appConfig;

	@Autowired
	PageNavigationService pageNavigationService;

	@Autowired
	SchoolDetailsBusiness schoolDetailsBusiness;

	@Autowired
	StudentSubscriptionWorkService studentSubscriptionWorkService;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionID();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		PaymentPageData pageData = new PaymentPageData();
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		StudentPaymentWork studentPaymentWork = studentPaymentWorkService
				.getByStudentIdAndEdition(studentControlWorkDTO.getStudentID(), editionId);
		TreeMap<String, String> paramMap = new TreeMap<String, String>();
		paramMap.put("MID", appConfig.getPaytmDetails().get("mid"));
		paramMap.put("ORDER_ID", studentPaymentWork.getOrderId());
		paramMap.put("CUST_ID", "" + studentPaymentWork.getStudentID());
		paramMap.put("INDUSTRY_TYPE_ID", appConfig.getPaytmDetails().get("industryTypeId"));
		paramMap.put("CHANNEL_ID", appConfig.getPaytmDetails().get("channelId"));
		paramMap.put("TXN_AMOUNT", "42.39");
		paramMap.put("WEBSITE", appConfig.getPaytmDetails().get("website"));
		paramMap.put("EMAIL", "");
		paramMap.put("MOBILE_NO", "");
		paramMap.put("CALLBACK_URL", appConfig.getPaytmDetails().get("callbackURL") + paramMap.get("ORDER_ID"));
		try {
			String checkSum = CheckSumServiceHelper.getCheckSumServiceHelper()
					.genrateCheckSum(appConfig.getPaytmDetails().get("merchantkey"), paramMap);
			paramMap.put("CHECKSUMHASH", checkSum);
			pageData.setParamMap(paramMap);
			pageDto.setData(pageData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageDto;
	}

	private boolean checkTransactionStatus(PageRequestDTO pageRequest) {
		/* initialize an object */
		JSONObject paytmParams = new JSONObject();

		/* body parameters */
		JSONObject body = new JSONObject();

		/*
		 * Find your MID in your Paytm Dashboard at
		 * https://dashboard.paytm.com/next/apikeys
		 */
		body.put("mid", "YOUR_MID_HERE");

		/* Enter your order id which needs to be check status for */
		body.put("orderId", "YOUR_ORDER_ID");

		/**
		 * Generate checksum by parameters we have in body You can get Checksum JAR from
		 * https://developer.paytm.com/docs/checksum/ Find your Merchant Key in your
		 * Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
		 */
		String checksum = "";
		try {
			checksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum("YOUR_KEY_HERE",
					body.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* head parameters */
		JSONObject head = new JSONObject();

		/* put generated checksum value here */
		head.put("signature", checksum);

		/* prepare JSON string for request */
		paytmParams.put("body", body);
		paytmParams.put("head", head);
		String post_data = paytmParams.toString();

		/* for Staging */
		try {
			URL url = new URL("https://securegw-stage.paytm.in/merchant-status/api/v1/getPaymentStatus");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(post_data);
			requestWriter.close();
			String responseData = "";
			InputStream is = connection.getInputStream();
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
			if ((responseData = responseReader.readLine()) != null) {
				System.out.append("Response: " + responseData);
			}
			// System.out.append("Request: " + post_data);
			responseReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return true;

	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {

		PageDTO pageDTO = new PageDTO();
		Long studentId = 0L;
		String emailId = pageRequest.getHeader().getEmailID();
		String editionId = pageRequest.getHeader().getEditionID();
		PaymentPageData paymentPagedata = mapPagedata(pageRequest);
		if (PageAction.OnTransactionResponse.toString().equals(actionName)) {
			TreeMap<String, String> inResponse = paymentPagedata.getInResponse();
			String checkSumHash = inResponse.get("CHECKSUMHASH");
			boolean isValidChecksum = false;
			try {
				isValidChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum("YOUR_KEY_HERE",
						inResponse, checkSumHash);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (isValidChecksum) {
				System.out.append("Checksum Matched");
			} else {
				System.out.append("Checksum Mismatched");
			}
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			studentId = studentControlWorkDTO.getStudentID();
			StudentPaymentWork studentPaymentWork = studentPaymentWorkService.getByStudentIdAndEdition(studentId,
					editionId);
			studentControlBusiness.updateAsWork(studentControlWorkDTO);
			subscriptionBusiness.saveWorkToMaster(studentId, editionId);
			studentPaymentBusiness.workToMaster(studentId, editionId);
			studentControlBusiness.workToMaster(studentId);
			studentPaymentWorkService.delete(studentPaymentWork.getPaymentID());
			studentSubscriptionWorkService.delete(studentId);
		} else if (PageAction.SomeUIErrorOccurred.toString().equals(actionName)
				|| PageAction.NetworkNotAvailable.toString().equals(actionName)
				|| PageAction.ClientAuthenticationFailed.toString().equals(actionName)
				|| PageAction.OnErrorLoadingWebPage.toString().equals(actionName)
				|| PageAction.OnBackPressedCancelTransaction.toString().equals(actionName)
				|| PageAction.OnTransactionCancel.toString().equals(actionName)) {
			if (PageAction.SomeUIErrorOccurred.toString().equals(actionName)) {
				pageDTO.setErrorMessage("UI Error " + paymentPagedata.getInErrorMessage());
			} else if (PageAction.NetworkNotAvailable.toString().equals(actionName)) {
				pageDTO.setErrorMessage("Network connection error: Check your internet connectivity");
			} else if (PageAction.ClientAuthenticationFailed.toString().equals(actionName)) {
				pageDTO.setErrorMessage("Authentication failed: Server error " + paymentPagedata.getInErrorMessage());
			} else if (PageAction.OnErrorLoadingWebPage.toString().equals(actionName)) {
				pageDTO.setErrorMessage("Unable to load webpage " + paymentPagedata.getInErrorMessage());
			} else if (PageAction.OnBackPressedCancelTransaction.toString().equals(actionName)) {
				pageDTO.setErrorMessage("Transaction cancelled");
			} else if (PageAction.OnTransactionCancel.toString().equals(actionName)) {
				pageDTO.setErrorMessage("Transaction cancelled" + paymentPagedata.getInErrorMessage());
			}
			// payment work table entry delete logic here..
		}

		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PaymentPageData mapPagedata(PageRequestDTO pageRequest) {
		PaymentPageData paymentPageData = null;
		try {
			paymentPageData = objectMapper.readValue(pageRequest.getData().toString(), PaymentPageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodes.SREVER_ERROR);
		}
		return paymentPageData;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
