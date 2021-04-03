package com.enewschamp.subscription.app.fw.page.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;
import javax.validation.Valid;

import org.json.simple.JSONArray;
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

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.common.domain.service.ErrorCodesService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.security.service.AppSecurityService;
import com.enewschamp.subscription.domain.business.StudentPaymentBusiness;
import com.enewschamp.subscription.domain.entity.StudentPayment;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.service.StudentPaymentService;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.user.app.dto.RefundListDTO;
import com.enewschamp.user.app.dto.RefundSearchData;
import com.enewschamp.user.app.dto.StudentRefundDTO;
import com.enewschamp.user.domain.entity.StudentRefund;
import com.enewschamp.user.domain.service.StudentRefundService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.pg.merchant.PaytmChecksum;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class StudentRefundController {

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
	StudentPaymentService studentPaymentService;

	@Autowired
	StudentRefundService studentRefundService;

	@Autowired
	StudentPaymentBusiness studentPaymentBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@PostMapping(value = "/admin/payment/refund/initiate")
	public ResponseEntity<StudentRefundDTO> initiateRefund(@RequestBody @Valid StudentRefundDTO refundDTO)
			throws Exception {
		Long studentId = 0L;
		String paytmTxnId = "";
		StudentPayment studentPayment = studentPaymentService.getByOrderId(refundDTO.getOrderId());
		if (studentPayment == null) {
			StudentPaymentWork studentPaymentWork = studentPaymentWorkService.getByOrderId(refundDTO.getOrderId());
			if (studentPaymentWork != null) {
				studentId = studentPaymentWork.getStudentId();
				paytmTxnId = studentPaymentWork.getPaytmTxnId();
			}
		} else {
			studentId = studentPayment.getStudentId();
			paytmTxnId = studentPayment.getPaytmTxnId();
		}
		if (studentId == 0L || "".equals(paytmTxnId)) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		refundDTO.setStudentId(studentId);
		refundDTO.setPaytmTxnId(paytmTxnId);
		refundDTO.setRefOrderId("REF_" + refundDTO.getOrderId());
		StudentRefund refund = modelMapper.map(refundDTO, StudentRefund.class);
		refund = studentRefundService.create(refund);
		JSONObject paytmParams = new JSONObject();
		JSONObject body = new JSONObject();
		body.put("mid", propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MID));
		body.put("txnType", "REFUND");
		body.put("orderId", refundDTO.getOrderId());
		body.put("txnId", refundDTO.getPaytmTxnId());
		body.put("refId", refundDTO.getRefOrderId());
		body.put("refundAmount", refundDTO.getRefundAmount());

		String checksum = PaytmChecksum.generateSignature(body.toString(),
				propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MERCHANT_KEY));

		JSONObject head = new JSONObject();
		head.put("signature", checksum);

		paytmParams.put("body", body);
		paytmParams.put("head", head);

		String post_data = paytmParams.toString();
		URL url = new URL(propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_REFUND_URL));
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(post_data);
			requestWriter.close();
			Blob initRefundReqPayload = null;
			initRefundReqPayload = new SerialBlob(post_data.getBytes());
			refund.setInitRefundApiRequest(initRefundReqPayload);
			String responseData = "";
			InputStream is = connection.getInputStream();
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
			if ((responseData = responseReader.readLine()) != null) {
				Blob initRefundResPayload = null;
				initRefundResPayload = new SerialBlob(responseData.getBytes());
				refund.setInitRefundApiResponse(initRefundResPayload);
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(responseData);
				if (json.get("body") != null) {
					JSONObject jsonBody = (JSONObject) parser.parse(json.get("body").toString());
					JSONObject resultInfo = (JSONObject) parser.parse(jsonBody.get("resultInfo").toString());
					refund.setPaytmStatus(resultInfo.get("resultStatus").toString());
					if (!"TXN_FAILURE".equals(resultInfo.get("resultStatus").toString())) {
						String refundTxnId = jsonBody.get("refundId").toString();
						refund.setRefundPaytmTxnId(refundTxnId);
					}
					studentRefundService.update(refund);
				}
			}
			responseReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		refundDTO = modelMapper.map(refund, StudentRefundDTO.class);
		return new ResponseEntity<StudentRefundDTO>(refundDTO, HttpStatus.CREATED);
	}

	@PostMapping(value = "/admin/payment/refund/statusRefresh")
	public ResponseEntity<StudentRefundDTO> refreshRefundStatus(@RequestBody @Valid StudentRefundDTO refundDTO)
			throws Exception {
		List<StudentRefund> studentRefundList = studentRefundService.getPendingRefundList();
		if (studentRefundList != null && studentRefundList.size() > 0) {
			for (int i = 0; i < studentRefundList.size(); i++) {
				StudentRefund studentRefund = studentRefundList.get(i);
				JSONObject paytmParams = new JSONObject();
				JSONObject body = new JSONObject();
				body.put("mid", propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MID));
				body.put("orderId", studentRefund.getOrderId());
				body.put("refId", studentRefund.getRefOrderId());
				String checksum = PaytmChecksum.generateSignature(body.toString(),
						propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MERCHANT_KEY));
				JSONObject head = new JSONObject();
				head.put("signature", checksum);

				paytmParams.put("body", body);
				paytmParams.put("head", head);

				String post_data = paytmParams.toString();
				URL url = new URL(propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_REFUND_STATUS_URL));
				try {
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Content-Type", "application/json");
					connection.setDoOutput(true);

					DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
					requestWriter.writeBytes(post_data);
					requestWriter.close();
					Blob refundStatusReqPayload = null;
					refundStatusReqPayload = new SerialBlob(post_data.getBytes());
					studentRefund.setRefundStatusApiRequest(refundStatusReqPayload);
					String responseData = "";
					InputStream is = connection.getInputStream();
					BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
					if ((responseData = responseReader.readLine()) != null) {
						Blob refundStatusResPayload = null;
						refundStatusResPayload = new SerialBlob(responseData.getBytes());
						studentRefund.setRefundStatusApiResponse(refundStatusResPayload);
						JSONParser parser = new JSONParser();
						JSONObject json = (JSONObject) parser.parse(responseData);
						if (json.get("body") != null) {
							JSONObject jsonBody = (JSONObject) parser.parse(json.get("body").toString());
							JSONObject resultInfo = (JSONObject) parser.parse(jsonBody.get("resultInfo").toString());
							studentRefund.setPaytmStatus(resultInfo.get("resultStatus").toString());
							if ("TXN_SUCCESS".equals(resultInfo.get("resultStatus").toString())) {
								studentRefund.setFinalStatus("SUCCESS");
							}
							studentRefundService.update(studentRefund);
						}
					}
					responseReader.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
			refundDTO.setMessage("Refund status refreshed successfully.");
		} else {
			refundDTO.setMessage("No pending refund data available.");
		}
		return new ResponseEntity<StudentRefundDTO>(refundDTO, HttpStatus.OK);
	}

	@PostMapping(value = "/admin/payment/refund/orderList")
	public ResponseEntity<RefundListDTO> getRefundOrderList(@RequestBody @Valid RefundListDTO refundListDTO)
			throws Exception {
		JSONObject paytmParams = new JSONObject();
		JSONObject body = new JSONObject();
		RefundSearchData searchData = refundListDTO.getSearchData();
		if (searchData.getIsSort() != null && !"".equals(searchData.getIsSort())) {
			body.put("isSort", searchData.getIsSort());
		}
		if (searchData.getPageSize() != null && !"".equals(searchData.getPageSize())) {
			body.put("pageSize", searchData.getPageSize());
		}
		if (searchData.getPageNumber() != null && !"".equals(searchData.getPageNumber())) {
			body.put("pageNum", searchData.getPageNumber());
		}

		body.put("mid", propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MID));
		body.put("startDate", searchData.getStartDate() + "T00:00:00+05:30");
		body.put("endDate", searchData.getEndDate() + "T23:59:59+05:30");
		String checksum = PaytmChecksum.generateSignature(body.toString(),
				propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MERCHANT_KEY));
		JSONObject head = new JSONObject();
		head.put("tokenType", "CHECKSUM");
		head.put("clientId", "C11");
		head.put("signature", checksum);

		paytmParams.put("body", body);
		paytmParams.put("head", head);

		String post_data = paytmParams.toString();
		URL url = new URL(propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_REFUND_LIST_URL));
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
				if (json.get("status") != null && "SUCCESS".equals(json.get("status").toString())) {
					JSONArray orderList = (JSONArray) parser.parse(json.get("orders").toString());
					refundListDTO.setOrders(orderList);
				} else {
					refundListDTO.setMessage("Unable to fetch list at the moment. Please try again.");
				}
			}
			responseReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return new ResponseEntity<RefundListDTO>(refundListDTO, HttpStatus.OK);
	}
}
