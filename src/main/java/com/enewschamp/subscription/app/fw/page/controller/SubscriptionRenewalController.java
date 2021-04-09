package com.enewschamp.subscription.app.fw.page.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.text.SimpleDateFormat;
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

import com.enewschamp.app.common.KeyProperty;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.common.domain.service.ErrorCodesService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.security.service.AppSecurityService;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.domain.business.StudentPaymentBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
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
	SubscriptionBusiness subscriptionBusiness;

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
		LocalDate endDate = LocalDate.now();
		int days = Integer.valueOf(propertiesService
				.getValue("StudentApp", PropertyConstants.PAYTM_SUBSCRIPTION_RENEWAL_ADVANCE_DAYS).toString());
		endDate = endDate.plusDays(days);
		List<StudentSubscription> subscriptionRenewalList = studentSubscriptionService
				.getSubscriptionRenewalList(endDate);

		if (subscriptionRenewalList != null && subscriptionRenewalList.size() > 0) {
			for (int i = 0; i < subscriptionRenewalList.size(); i++) {
				StudentSubscriptionDTO studentSubscription = modelMapper.map(subscriptionRenewalList.get(i),
						StudentSubscriptionDTO.class);
				JSONObject paytmParams = new JSONObject();
				JSONObject body = new JSONObject();
				body.put("mid", KeyProperty.MID);
				body.put("subsId", studentSubscription.getSubscriptionId());
				SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
				Date debitDate = new SimpleDateFormat("yyyy-MM-dd").parse(studentSubscription.getEndDate().toString());
				body.put("debitDate", sdf.format(debitDate));
				String checksum = PaytmChecksum.generateSignature(body.toString(), KeyProperty.MERCHANT_KEY);
				JSONObject head = new JSONObject();
				head.put("signature", checksum);
				head.put("tokenType", "AES");

				paytmParams.put("body", body);
				paytmParams.put("head", head);

				String post_data = paytmParams.toString();
				System.out.println(">>>>>>>>>>>>body>>>>>>>>>>>" + post_data);
				System.out.println(">>>>>>>>>>>URL>>>>>>>>>>>>"
						+ propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_SUBSCRIPTION_STATUS_URL));
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
						System.out.println(">>>>>>>>>responseData>>>>>>" + responseData);
						JSONObject json = (JSONObject) parser.parse(responseData);
						if (json.get("body") != null) {
							JSONObject jsonBody = (JSONObject) parser.parse(json.get("body").toString());
							JSONObject resultInfo = (JSONObject) parser.parse(jsonBody.get("resultInfo").toString());
							String response = resultInfo.get("status").toString();
							if ("SUCCESS".equals(response)) {
								if (jsonBody.get("status") != null
										&& "ACTIVE".equals(jsonBody.get("status").toString())) {
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

	private void renewSubscription(String subscriptionAmount, StudentSubscriptionDTO studentSubscriptionDTO) {
		try {
			StudentPaymentWork studentPaymentWork = new StudentPaymentWork();
			studentPaymentWork.setOperatorId(PropertyConstants.DEFAULT_OPERATOR_ID);
			studentPaymentWork.setRecordInUse(RecordInUseType.Y);
			studentPaymentWork.setEditionId(studentSubscriptionDTO.getEditionId());
			studentPaymentWork.setPaymentAmount(Double.valueOf(subscriptionAmount));
			studentPaymentWork.setPaymentCurrency("INR");
			studentPaymentWork.setStudentId(studentSubscriptionDTO.getStudentId());
			studentPaymentWork.setSubscriptionId(studentSubscriptionDTO.getSubscriptionId());
			studentPaymentWork.setSubscriptionPeriod(studentSubscriptionDTO.getSubscriptionPeriod());
			studentPaymentWork.setSubscriptionType(studentSubscriptionDTO.getSubscriptionSelected());
			String renewalOrderId = generateOrderId(studentSubscriptionDTO.getStudentId(),
					studentSubscriptionDTO.getSubscriptionSelected());
			studentPaymentWork.setOrderId(renewalOrderId);
			JSONObject paytmParams = new JSONObject();
			JSONObject body = new JSONObject();
			body.put("mid", KeyProperty.MID);
			body.put("orderId", renewalOrderId);
			body.put("subscriptionId", studentSubscriptionDTO.getSubscriptionId());

			JSONObject txnAmount = new JSONObject();
			txnAmount.put("value", subscriptionAmount);
			txnAmount.put("currency", "INR");

			body.put("txnAmount", txnAmount);

			String checksum = PaytmChecksum.generateSignature(body.toString(), KeyProperty.MERCHANT_KEY);

			JSONObject head = new JSONObject();
			head.put("signature", checksum);
			// head.put("tokenType", "AES");

			paytmParams.put("body", body);
			paytmParams.put("head", head);

			String post_data = paytmParams.toString();
			System.out.println(">>>>>>>renewal json>>>>>>>>>>>>>" + post_data);
			System.out.println(">>>>>>>URL>>>>>>>>>>>>>"
					+ propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_RENEW_SUBSCRIPTION_URL));
			URL url = new URL(propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_RENEW_SUBSCRIPTION_URL)
					+ "?mid=" + KeyProperty.MID + "&orderId=" + renewalOrderId);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(post_data);
			Blob initTransReqPayload = null;
			post_data = post_data.replace(KeyProperty.MID, "XXXXXXXXXXXXXX");
			post_data = post_data.replace(subscriptionAmount, "XX.XX");

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
				JSONObject json = (JSONObject) parser.parse(responseData);
				System.out.println(">>>>>>>>>>>>>renewal api response>>>>>>>>>>>>" + responseData);
				if (json.get("body") != null) {
					JSONObject jsonBody = (JSONObject) parser.parse(json.get("body").toString());
					JSONObject resultInfo = (JSONObject) parser.parse(jsonBody.get("resultInfo").toString());
					String response = resultInfo.get("resultStatus").toString();
					if ("S".equals(response)) {
						String paytmTxnId = jsonBody.get("txnId").toString();
						String subscriptionPeriod = studentSubscriptionDTO.getSubscriptionPeriod();
						LocalDate subscriptionEndDate = getNextRenewalDate(subscriptionPeriod,
								studentSubscriptionDTO.getEndDate());
						studentPaymentWork.setPaytmTxnId(paytmTxnId);
						studentPaymentWork.setPaytmStatus("TXN_SUCCESS");
						studentPaymentWork.setFinalOrderStatus("SUCCESS");
						studentPaymentWork.setSubscriptionEndDate(subscriptionEndDate);
						studentPaymentWork = studentPaymentWorkService.create(studentPaymentWork);
						studentSubscriptionDTO.setEndDate(subscriptionEndDate);
						studentSubscriptionDTO.setOperatorId(PropertyConstants.DEFAULT_OPERATOR_ID);
						StudentSubscription studentSubscription = modelMapper.map(studentSubscriptionDTO,
								StudentSubscription.class);
						studentSubscriptionService.update(studentSubscription);
						studentPaymentBusiness.workToMaster(studentSubscription.getStudentId(),
								studentSubscription.getEditionId());
						studentPaymentWorkService.delete(studentPaymentWork.getPaymentId());
					} else {
						studentPaymentWork.setPaytmStatus("TXN_FAILURE");
						studentPaymentWork.setFinalOrderStatus("FAILED");
						studentPaymentWork = studentPaymentWorkService.create(studentPaymentWork);
						studentPaymentBusiness.workToFailed(renewalOrderId);
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHmmss");
		String orderId = "ENC" + "_" + sdf.format(new Date()) + "_" + studentId;
		return orderId;
	}
}
