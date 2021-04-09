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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import com.enewschamp.app.common.uicontrols.rules.UIControlsRuleExecutionService;
import com.enewschamp.app.common.uicontrols.service.UIControlsGlobalService;
import com.enewschamp.app.common.uicontrols.service.UIControlsService;
import com.enewschamp.app.fw.page.navigation.rules.PageNavRuleExecutionService;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.app.holiday.service.HolidayService;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.common.domain.service.ErrorCodesService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.common.domain.service.PropertiesFrontendService;
import com.enewschamp.domain.common.PageHandlerFactory;
import com.enewschamp.master.badge.service.BadgeService;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.security.service.AppSecurityService;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentPaymentBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.service.StudentControlWorkService;
import com.enewschamp.subscription.domain.service.StudentDetailsWorkService;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.subscription.domain.service.StudentPreferencesWorkService;
import com.enewschamp.subscription.domain.service.StudentSchoolWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
							isValidChecksum = PaytmChecksum.verifySignature(json.get("body").toString(),
									KeyProperty.MERCHANT_KEY, checksumHash);
							System.out.println(">>>>>>>>>checksumHash>>>>>>>>>" + checksumHash);
							System.out.println(">>>>>>>>>isValidChecksum1>>>>>>>>>" + PaytmChecksum.verifySignature(
									json.get("body").toString(), KeyProperty.MERCHANT_KEY, checksumHash));
							System.out.println(">>>>>>>>>isValidChecksum2>>>>>>>>>" + PaytmChecksum
									.verifySignature(json.toString(), KeyProperty.MERCHANT_KEY, checksumHash));
							System.out.println(">>>>>>>>>isValidChecksum3>>>>>>>>>" + PaytmChecksum
									.verifySignature(responseData, KeyProperty.MERCHANT_KEY, checksumHash));
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

	public static void main(String[] args) {
		JSONObject json = new JSONObject();
		json.put("bankName", "WALLET");
		json.put("bankTxnId", "64376087");
		json.put("gatewayName", "WALLET");
		json.put("mid", "ErzAtT88266326392189");
		json.put("orderId", "ENC_20215908225903_57");
		json.put("paymentMode", "PPI");
		json.put("refundAmt", "0.00");
		JSONObject resultInfo = new JSONObject();
		resultInfo.put("resultCode", "01");
		resultInfo.put("resultMsg", "Txn Success");
		resultInfo.put("resultStatus", "TXN_SUCCESS");
		json.put("resultInfo", resultInfo);
		json.put("subsId", "167473");
		json.put("txnAmount", "100.00");
		json.put("txnDate", "2021-04-08 22:59:04.0");
		json.put("txnId", "20210408111212800110168508002512108");
		json.put("txnType", "SALE");
		try {
			System.out.println(">>>>>>>>>>" + PaytmChecksum.verifySignature("{\"bankName\" : \"WALLET\","
					+ "		\"bankTxnId\" : \"64376087\"," + "		\"gatewayName\" : \"WALLET\","
					+ "		\"mid\" : \"ErzAtT88266326392189\"," + "		\"orderId\" : \"ENC_20215908225903_57\","
					+ "		\"paymentMode\" : \"PPI\"," + "		\"refundAmt\" : \"0.00\"," + "		\"resultInfo\" : {"
					+ "			\"resultCode\" : \"01\"," + "			\"resultMsg\" : \"Txn Success\","
					+ "			\"resultStatus\" : \"TXN_SUCCESS\"" + "		}," + "		\"subsId\" : \"167473\","
					+ "		\"txnAmount\" : \"100.00\"," + "		\"txnDate\" : \"2021-04-08 22:59:04.0\","
					+ "		\"txnId\" : \"20210408111212800110168508002512108\"," + "		\"txnType\" : \"SALE\"}",
					"kk3pYLy_DD4uk9NR",
					"eL0IMK5fd1dqmq02wEvoVDqCR/mdG+WSuGsYFQmtanP58lLWTKJFlROXeDTr6wphy2HORvs3nB8HZZmsdkbYgereywwLKXh6UFJTZVi4yv8="));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
