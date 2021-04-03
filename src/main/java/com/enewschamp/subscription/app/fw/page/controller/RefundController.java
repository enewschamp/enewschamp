package com.enewschamp.subscription.app.fw.page.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.common.domain.service.ErrorCodesService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.security.service.AppSecurityService;
import com.enewschamp.subscription.domain.business.StudentPaymentBusiness;
import com.enewschamp.subscription.domain.service.StudentPaymentService;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.user.app.dto.StudentRefundDTO;
import com.enewschamp.user.domain.entity.StudentRefund;
import com.enewschamp.user.domain.service.RefundService;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.paytm.pg.merchant.PaytmChecksum;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class RefundController {

	@Autowired
	AppSecurityService appSecService;

	@Autowired
	ErrorCodesService errorCodeService;

	@Autowired
	RefundService refundService;

	@Autowired
	private PropertiesBackendService propertiesService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StudentPaymentWorkService studentPaymentWorkService;

	@Autowired
	StudentPaymentService studentPaymentService;

	@Autowired
	StudentPaymentBusiness studentPaymentBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@PostMapping(value = "/admin/refunds")
	public ResponseEntity<StudentRefundDTO> refund(@RequestBody @Valid StudentRefundDTO refundDTO) throws Exception {
		Long studentId = studentPaymentService.getStudentByOrderIdAndTxnId(refundDTO.getOrderId(),
				refundDTO.getPaytmTxnId());
		refundDTO.setStudentId(studentId);
		refundDTO.setOperatorId("Admin");
		refundDTO.setRecordInUse(RecordInUseType.Y);
		StudentRefund refund = modelMapper.map(refundDTO, StudentRefund.class);
		refund = refundService.create(refund);
		JSONObject paytmParams = new JSONObject();
		JSONObject body = new JSONObject();
		body.put("mid", propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MID));
		body.put("txnType", "REFUND");
		body.put("orderId", refundDTO.getOrderId());
		body.put("txnId", refundDTO.getPaytmTxnId());
		body.put("refId", refundDTO.getRefOrderId());
		body.put("refundAmount", refundDTO.getRefundAmount());

//		String checksum = PaytmChecksum.generateSignature(body.toString(),
//				propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MERCHANT_KEY));

		JSONObject head = new JSONObject();
		//head.put("signature", checksum);

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
					String refundTxnId = jsonBody.get("refundId").toString();
					refund.setRefundPaytmTxnId(refundTxnId);
					JSONObject resultInfo = (JSONObject) parser.parse(jsonBody.get("resultInfo").toString());
					refund.setPaytmStatus(resultInfo.get("resultStatus").toString());
					refundService.update(refund);
				}
			}
			responseReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		refundDTO = modelMapper.map(refund, StudentRefundDTO.class);
		return new ResponseEntity<StudentRefundDTO>(refundDTO, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/refundStatus", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public void refundStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {

	}

	@RequestMapping(value = "/refundList", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public void refundList(HttpServletRequest request, HttpServletResponse response) throws IOException {

	}

	public static void main(String[] args) throws Exception {
		// refundListPayload();
		// refundPayload();
		refundStatusPayload();
		// orderListPayload();
	}

	public static void refundPayload() throws Exception {
		JSONObject body = new JSONObject();
		body.put("mid", "ErzAtT88266326392189");
		body.put("txnType", "REFUND");
		body.put("orderId", "P1617371784754_47");
		body.put("txnId", "20210402111212800110168876802483641");
		body.put("refId", "REFUNDID_1234566_47");
		body.put("refundAmount", "100.00");
		//String checksum = PaytmChecksum.generateSignature(body.toString(), "kk3pYLy_DD4uk9NR");
		//System.out.println(">>>>>>>>>>>>>>" + checksum);
		System.out.println(">>>>>>>>>>>>>>" + body.toString());
	}

	public static void orderListPayload() throws Exception {
		JSONObject body = new JSONObject();
		body.put("mid", "ErzAtT88266326392189");
		body.put("startDate", "2021-04-01T00:34:00+05:30");
		body.put("endDate", "2021-04-02T15:44:24+05:30");
		body.put("pageSize", "50");
		body.put("pageNumber", "1");
		body.put("orderSearchType", "TRANSACTION");
		body.put("orderSearchStatus", "SUCCESS");
		//String checksum = PaytmChecksum.generateSignature(body.toString(), "kk3pYLy_DD4uk9NR");
		//System.out.println(">>>>>>>>>>>>>>" + checksum);
		System.out.println(">>>>>>>>>>>>>>" + body.toString());
	}

	public static void refundListPayload() throws Exception {
		JSONObject body = new JSONObject();
		body.put("mid", "ErzAtT88266326392189");
		body.put("isSort", "true");
		body.put("startDate", "2021-04-01T00:34:00+05:30");
		body.put("endDate", "2021-04-03T00:05:24+05:30");
		body.put("pageSize", 10);
		body.put("pageNum", 1);
		//String checksum = PaytmChecksum.generateSignature(body.toString(), "kk3pYLy_DD4uk9NR");
		//System.out.println(">>>>>>>>>>>>>>" + checksum);
		System.out.println(">>>>>>>>>>>>>>" + body.toString());
	}

	public static void refundStatusPayload() throws Exception {
		JSONObject body = new JSONObject();
		body.put("mid", "ErzAtT88266326392189");
		body.put("orderId", "P1614710190757_33");
		body.put("refId", "REFUNDID_1234566");
	//	String checksum = PaytmChecksum.generateSignature(body.toString(), "kk3pYLy_DD4uk9NR");
	//	System.out.println(">>>>>>>>>>>>>>" + checksum);
		System.out.println(">>>>>>>>>>>>>>" + body.toString());
	}
}