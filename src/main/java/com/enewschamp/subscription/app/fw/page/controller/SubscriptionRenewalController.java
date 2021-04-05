package com.enewschamp.subscription.app.fw.page.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.common.domain.service.ErrorCodesService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.security.service.AppSecurityService;
import com.enewschamp.subscription.domain.business.StudentPaymentBusiness;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.service.StudentPaymentService;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.enewschamp.user.app.dto.StudentRefundDTO;
import com.enewschamp.user.domain.service.StudentRefundService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.pg.merchant.PaytmChecksum;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class SubscriptionRenewalController {

	@Autowired
	AppSecurityService appSecService;

	@Autowired
	ErrorCodesService errorCodeService;

	@Autowired
	private PropertiesBackendService propertiesService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StudentPaymentWorkService studentPaymentWorkService;

	@Autowired
	StudentSubscriptionService studentSubscriptionService;

	@Autowired
	StudentPaymentService studentPaymentService;

	@Autowired
	StudentRefundService studentRefundService;

	@Autowired
	StudentPaymentBusiness studentPaymentBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@PostMapping(value = "/admin/payment/subscription/renewal")
	public ResponseEntity<StudentRefundDTO> subscriptionRenewal(@RequestBody @Valid StudentRefundDTO refundDTO)
			throws Exception {
		List<StudentSubscription> subscriptionRenewalList = studentSubscriptionService.getSubscriptionRenewalList();
		if (subscriptionRenewalList != null && subscriptionRenewalList.size() > 0) {
			for (int i = 0; i < subscriptionRenewalList.size(); i++) {
				StudentSubscription studentSubscription = subscriptionRenewalList.get(i);
				JSONObject paytmParams = new JSONObject();
				JSONObject body = new JSONObject();
				body.put("mid", propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MID));
				body.put("subsId", studentSubscription.getSubscriptionId());
				String checksum = PaytmChecksum.generateSignature(body.toString(),
						propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MERCHANT_KEY));
				JSONObject head = new JSONObject();
				head.put("signature", checksum);
				head.put("tokenType", "AES");

				paytmParams.put("body", body);
				paytmParams.put("head", head);

				String post_data = paytmParams.toString();
				URL url = new URL(
						propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_SUBSCRIPTION_STATUS_URL));
				try {
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
						JSONParser parser = new JSONParser();
						JSONObject json = (JSONObject) parser.parse(responseData);
						if (json.get("body") != null) {
							JSONObject jsonBody = (JSONObject) parser.parse(json.get("body").toString());
							JSONObject resultInfo = (JSONObject) parser.parse(jsonBody.get("resultInfo").toString());
							String response = resultInfo.get("status").toString();
							if ("SUCCESS".equals(response)) {
								if ("ACTIVE".equals(jsonBody.get("status"))) {
									renewSubscription(jsonBody.get("upfrontTxnAmount").toString(), studentSubscription);
								}
							}
						}
					}
					responseReader.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
			refundDTO.setMessage("Subscription renewal processed successfully.");
		} else {
			refundDTO.setMessage("No pending renewals available.");
		}
		return new ResponseEntity<StudentRefundDTO>(refundDTO, HttpStatus.OK);
	}

	private void renewSubscription(String subscriptionAmount, StudentSubscription studentSubscription) {
		try {
			StudentPaymentWork studentPaymentWork = new StudentPaymentWork();
			studentPaymentWork.setOperatorId("SYSTEM");
			studentPaymentWork.setRecordInUse(RecordInUseType.Y);
			studentPaymentWork.setEditionId(studentSubscription.getEditionId());
			studentPaymentWork.setPaymentAmount(Double.valueOf(subscriptionAmount));
			studentPaymentWork.setPaymentCurrency("INR");
			studentPaymentWork.setStudentId(studentSubscription.getStudentId());
			studentPaymentWork.setSubscriptionId(studentSubscription.getSubscriptionId());
			JSONObject paytmParams = new JSONObject();
			JSONObject body = new JSONObject();
			body.put("mid", propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MID));
			body.put("orderId",
					generateOrderId(studentSubscription.getStudentId(), studentSubscription.getSubscriptionSelected()));
			body.put("subscriptionId", studentSubscription.getSubscriptionId());
			String checksum = PaytmChecksum.generateSignature(body.toString(),
					propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MERCHANT_KEY));
			JSONObject txnAmount = new JSONObject();
			txnAmount.put("value", subscriptionAmount);
			txnAmount.put("currency", "INR");

			body.put("txnAmount", txnAmount);

			JSONObject head = new JSONObject();
			head.put("signature", checksum);
			head.put("tokenType", "AES");

			paytmParams.put("body", body);
			paytmParams.put("head", head);

			String post_data = paytmParams.toString();
			URL url = new URL(propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_RENEW_SUBSCRIPTION_URL));

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
				JSONObject json = (JSONObject) parser.parse(responseData);
				if (json.get("body") != null) {
					JSONObject jsonBody = (JSONObject) parser.parse(json.get("body").toString());
					JSONObject resultInfo = (JSONObject) parser.parse(jsonBody.get("resultInfo").toString());
					String response = resultInfo.get("resultStatus").toString();
					if ("S".equals(response)) {
						if ("ACTIVE".equals(jsonBody.get("status"))) {
							String paytmTxnId = jsonBody.get("txnId").toString();
							studentPaymentWork.setPaytmTxnId(paytmTxnId);
							studentPaymentWork = studentPaymentWorkService.create(studentPaymentWork);
							String subscriptionPeriod = studentSubscription.getSubscriptionPeriod();
							studentSubscription.setEndDate(
									getNextRenewalDate(subscriptionPeriod, studentSubscription.getEndDate()));
							studentSubscriptionService.update(studentSubscription);
							studentPaymentBusiness.workToMaster(studentSubscription.getStudentId(),
									studentSubscription.getEditionId());
							studentPaymentWorkService.delete(studentPaymentWork.getPaymentId());
						}
					}
				}
			}
			responseReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	private LocalDate getNextRenewalDate(String subscriptionPeriod, LocalDate endDate) {
		if (subscriptionPeriod.endsWith("D")) {
			endDate = endDate
					.plusDays(Integer.valueOf(subscriptionPeriod.substring(0, (subscriptionPeriod.length() - 1))));
		} else if (subscriptionPeriod.endsWith("M")) {
			endDate = endDate
					.plusMonths(Integer.valueOf(subscriptionPeriod.substring(0, (subscriptionPeriod.length() - 1))));
		} else if (subscriptionPeriod.endsWith("Y")) {
			endDate = endDate
					.plusYears(Integer.valueOf(subscriptionPeriod.substring(0, (subscriptionPeriod.length() - 1))));
		}
		return endDate;
	}

	private String generateOrderId(Long studentId, String subscriptionType) {
		String orderId = subscriptionType + "" + new Date().getTime() + "_" + studentId;
		return orderId;

	}
}