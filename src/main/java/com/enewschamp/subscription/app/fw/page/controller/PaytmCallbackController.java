package com.enewschamp.subscription.app.fw.page.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.app.common.KeyProperty;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.common.domain.service.ErrorCodesService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.security.service.AppSecurityService;
import com.enewschamp.subscription.domain.business.StudentPaymentBusiness;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paytm.pg.merchant.PaytmChecksum;

import lombok.extern.java.Log;

@Log
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/paytm/")
public class PaytmCallbackController {

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
	StudentPaymentBusiness studentPaymentBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@RequestMapping(value = "/callback", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String responseData = null;
		String checksumHash = null;
		String mid = null;
		String currency = null;
		String gatewayName = null;
		String respMsg = null;
		String bankName = null;
		String paymentMode = null;
		String respCode = null;
		String txnId = null;
		String txnAmount = null;
		String orderId = null;
		String status = null;
		String bankTxnId = null;
		String txnDate = null;

		TreeMap<String, String> paytmParams = new TreeMap<String, String>();
		for (Entry<String, String[]> requestParamsEntry : request.getParameterMap().entrySet()) {
			if ("CHECKSUMHASH".equalsIgnoreCase(requestParamsEntry.getKey())) {
				checksumHash = requestParamsEntry.getValue()[0];
			} else {
				if ("MID".equalsIgnoreCase(requestParamsEntry.getKey())) {
					mid = requestParamsEntry.getValue()[0];
				} else if ("RESPCODE".equalsIgnoreCase(requestParamsEntry.getKey())) {
					respCode = requestParamsEntry.getValue()[0];
				} else if ("CURRENCY".equalsIgnoreCase(requestParamsEntry.getKey())) {
					currency = requestParamsEntry.getValue()[0];
				} else if ("GATEWAYNAME".equalsIgnoreCase(requestParamsEntry.getKey())) {
					gatewayName = requestParamsEntry.getValue()[0];
				} else if ("RESPMSG".equalsIgnoreCase(requestParamsEntry.getKey())) {
					respMsg = requestParamsEntry.getValue()[0];
				} else if ("BANKNAME".equalsIgnoreCase(requestParamsEntry.getKey())) {
					bankName = requestParamsEntry.getValue()[0];
				} else if ("PAYMENTMODE".equalsIgnoreCase(requestParamsEntry.getKey())) {
					paymentMode = requestParamsEntry.getValue()[0];
				} else if ("TXNID".equalsIgnoreCase(requestParamsEntry.getKey())) {
					txnId = requestParamsEntry.getValue()[0];
				} else if ("TXNAMOUNT".equalsIgnoreCase(requestParamsEntry.getKey())) {
					txnAmount = requestParamsEntry.getValue()[0];
				} else if ("ORDERID".equalsIgnoreCase(requestParamsEntry.getKey())) {
					orderId = requestParamsEntry.getValue()[0];
				} else if ("STATUS".equalsIgnoreCase(requestParamsEntry.getKey())) {
					status = requestParamsEntry.getValue()[0];
				} else if ("BANKTXNID".equalsIgnoreCase(requestParamsEntry.getKey())) {
					bankTxnId = requestParamsEntry.getValue()[0];
				} else if ("TXNDATE".equalsIgnoreCase(requestParamsEntry.getKey())) {
					txnDate = requestParamsEntry.getValue()[0];
				} else if ("TXNDATE".equalsIgnoreCase(requestParamsEntry.getKey())) {
					txnDate = requestParamsEntry.getValue()[0];
				}
				paytmParams.put(requestParamsEntry.getKey(), requestParamsEntry.getValue()[0]);
			}
		}

		boolean isValidChecksum = false;
		try {
			isValidChecksum = PaytmChecksum.verifySignature(paytmParams, KeyProperty.MERCHANT_KEY, checksumHash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String orderStatus = "";
		if ("01".equals(respCode)) {
			if (isValidChecksum) {
				HashMap<String, String> transactionStatus = getTransactionStatus("StudentApp", orderId);
				if ("TXN_SUCCESS".equals(transactionStatus.get("TRAN_STATUS"))) {
					orderStatus = propertiesService.getValue("StudentApp",
							PropertyConstants.PAYTM_CALLBACK_TRAN_STATUS_TXN_SUCCESS);
				} else {
					if ("TXN_FAILURE".equals(transactionStatus.get("TRAN_STATUS"))
							|| "PENDING".equals(transactionStatus.get("TRAN_STATUS"))) {
						orderStatus = propertiesService.getValue("StudentApp",
								PropertyConstants.PAYTM_CALLBACK_TRAN_STATUS_TXN_FAILURE_OR_PENDING);
					} else if ("CHECKSUM_MISMATCH".equals(transactionStatus.get("TRAN_STATUS"))) {
						orderStatus = propertiesService.getValue("StudentApp",
								PropertyConstants.PAYTM_CALLBACK_CHECKSUM_MISMATCH);
					} else if (txnAmount != null && transactionStatus.get("TRAN_AMT") != null
							&& (Double.compare(Double.valueOf(txnAmount),
									Double.valueOf(transactionStatus.get("TRAN_AMT"))) != 0)) {
						orderStatus = propertiesService.getValue("StudentApp",
								PropertyConstants.PAYTM_CALLBACK_AMOUNT_MISMATCH);
					}
				}
			} else {
				orderStatus = propertiesService.getValue("StudentApp",
						PropertyConstants.PAYTM_CALLBACK_CHECKSUM_MISMATCH);
			}
		} else {
			orderStatus = "FAILED";
		}
		StudentPaymentWork studentPaymentWork = studentPaymentWorkService.getByOrderId(orderId);
		if (studentPaymentWork != null) {
			studentPaymentWork.setPaytmCallbackRespTime(LocalDateTime.now());
			studentPaymentWork.setPaytmBankName(bankName);
			studentPaymentWork.setPaytmBankTxnId(bankTxnId);
			studentPaymentWork.setPaytmChecksumHash(checksumHash);
			studentPaymentWork.setPaytmGatewayName(gatewayName);
			studentPaymentWork.setPaytmPaymentMode(paymentMode);
			studentPaymentWork.setPaytmRespCode(respCode);
			studentPaymentWork.setPaytmRespMsg(respMsg);
			studentPaymentWork.setPaytmStatus(status);
			studentPaymentWork.setPaytmTxnAmount(txnAmount);
			studentPaymentWork.setPaytmTxnId(txnId);
			studentPaymentWork.setPaytmTxnDate(txnDate);
			studentPaymentWork.setFinalOrderStatus(orderStatus);
			studentPaymentWorkService.update(studentPaymentWork);
			if (!"01".equals(respCode)) {
				if ("141".equals(respCode)) {
					studentPaymentWork.setFinalOrderStatus("CANCELLED");
				}
				studentPaymentBusiness.workToFailed(orderId);
			}
			responseData = "RESPCODE###" + respCode + "|$|RESPMSG###" + respMsg + "|$|GATEWAYNAME###" + gatewayName
					+ "|$|BANKNAME###" + bankName + "|$|PAYMENTMODE###" + paymentMode + "|$|ORDERID###" + orderId
					+ "|$|TXNID###" + txnId + "|$|TXNAMOUNT###" + txnAmount + "|$|CURRENCY###" + currency
					+ "|$|STATUS###" + status + "|$|BANKTXNID###" + bankTxnId + "|$|TXNDATE###" + txnDate
					+ "|$|CALLBACK_STATUS###" + orderStatus;
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(
					"<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"><title>"
							+ responseData + "</title></head>");
			out.println(
					"<body><table align='center'><tr><td><STRONG>Transaction is being processed,</STRONG></td></tr>");
			out.println("<tr><td><font color='blue'>Please wait ...</font></td></tr>");
			out.println("<tr><td>(Please do not press 'Refresh' or 'Back' button</td></tr>");
			out.println("</table></body></html>");
		}
	}

	private HashMap<String, String> getTransactionStatus(String module, String orderId) {
		HashMap<String, String> tranData = new HashMap<String, String>();
		JSONObject paytmParams = new JSONObject();
		JSONObject body = new JSONObject();
		body.put("mid", KeyProperty.MID);
		body.put("orderId", orderId);
		String signature = "";
		StudentPaymentWork studentPaymentWork = studentPaymentWorkService.getByOrderId(orderId);
		try {
			signature = PaytmChecksum.generateSignature(body.toString(), KeyProperty.MERCHANT_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject head = new JSONObject();
		head.put("signature", signature);
		paytmParams.put("body", body);
		paytmParams.put("head", head);
		String post_data = paytmParams.toString();
		String responseData = "";
		try {
			URL url = new URL(propertiesService.getValue(module, PropertyConstants.PAYTM_TRANSACTION_STATUS_URL));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(post_data);
			post_data = post_data.replace(KeyProperty.MID, "XXXXXXXXXXXXXX");
			Blob tranStatusReqPayload = new SerialBlob(post_data.getBytes());
			studentPaymentWork.setTranStatusApiRequest(tranStatusReqPayload);
			requestWriter.close();
			InputStream is = connection.getInputStream();
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
			if ((responseData = responseReader.readLine()) != null) {
				JSONParser parser = new JSONParser();
				System.out.println(">>>>>>>>>>responseData tran status>>>>>>>>>>" + responseData);
				String response_data = responseData.replace(KeyProperty.MID, "XXXXXXXXXXXXXX");
				JSONObject tempJSON = (JSONObject) parser.parse(response_data);
				if (tempJSON.get("body") != null) {
					JSONObject responseBody = (JSONObject) tempJSON.get("body");
					if (responseBody.get("refundAmt") != null) {
						responseBody.put("refundAmt", "XX.XX");
					}
					if (responseBody.get("txnAmount") != null) {
						responseBody.put("txnAmount", "XX.XX");
					}
					tempJSON.put("body", responseBody);
				}
				Blob tranStatusResPayload = new SerialBlob(tempJSON.toString().getBytes());
				studentPaymentWork.setTranStatusApiResponse(tranStatusResPayload);
				String tranAmt = null;
				String tranStatus = null;
				try {
					boolean isValidChecksum = false;
					String checksumHash = "";
					JSONObject json = (JSONObject) parser.parse(responseData);
					if (json.get("head") != null) {
						JSONObject jsonBody = (JSONObject) parser.parse(json.get("head").toString());
						checksumHash = jsonBody.get("signature").toString();
					}
					JSONObject jsonBody = null;
					if (json.get("body") != null) {
						jsonBody = (JSONObject) parser.parse(json.get("body").toString());
						try {
							Gson gson = new Gson();
							JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);
							isValidChecksum = PaytmChecksum.verifySignature(jsonObject.get("body").toString(),
									KeyProperty.MERCHANT_KEY, checksumHash);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (!isValidChecksum) {
						tranStatus = "CHECKSUM_MISMATCH";
					} else {
						JSONObject tranDetails = (JSONObject) jsonBody.get("resultInfo");
						tranStatus = tranDetails.get("resultStatus").toString();
						tranAmt = (jsonBody.get("txnAmount") != null ? jsonBody.get("txnAmount").toString() : "");
					}
				} catch (ParseException e) {
					e.printStackTrace();
					tranStatus = "Unable to check transaction details at the moment.";
				}
				tranData.put("TRAN_STATUS", tranStatus);
				tranData.put("TRAN_AMT", tranAmt);
			}
			responseReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		if (studentPaymentWork != null) {
			studentPaymentWorkService.update(studentPaymentWork);
		}
		return tranData;
	}
}
