package com.enewschamp.subscription.app.fw.page.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

import com.enewschamp.app.common.KeyProperty;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.common.domain.service.ErrorCodesService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.security.service.AppSecurityService;
import com.enewschamp.subscription.domain.business.StudentPaymentBusiness;
import com.enewschamp.subscription.domain.service.StudentPaymentService;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.user.app.dto.OrderListDTO;
import com.enewschamp.user.app.dto.OrderSearchData;
import com.enewschamp.user.app.dto.RefundListDTO;
import com.enewschamp.user.domain.service.StudentRefundService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.pg.merchant.PaytmChecksum;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class OrderController {

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

	@PostMapping(value = "/admin/payment/order/orderList")
	public ResponseEntity<OrderListDTO> getOrderList(@RequestBody @Valid OrderListDTO orderListDTO) throws Exception {
		JSONObject paytmParams = new JSONObject();
		JSONObject body = new JSONObject();
		OrderSearchData searchData = orderListDTO.getSearchData();
		if (searchData.getIsSort() != null && !"".equals(searchData.getIsSort())) {
			body.put("isSort", searchData.getIsSort());
		}
		if (searchData.getPageSize() != null && !"".equals(searchData.getPageSize())) {
			body.put("pageSize", Integer.valueOf(searchData.getPageSize()));
		}
		if (searchData.getPageNumber() != null && !"".equals(searchData.getPageNumber())) {
			body.put("pageNumber", Integer.valueOf(searchData.getPageNumber()));
		}
		if (searchData.getFromDate() != null && !"".equals(searchData.getFromDate())) {
			body.put("fromDate", searchData.getFromDate() + "T00:00:00+05:30");
		}
		if (searchData.getToDate() != null && !"".equals(searchData.getToDate())) {
			body.put("toDate", searchData.getToDate() + "T23:59:59+05:30");
		}
		if (searchData.getMerchantOrderId() != null && !"".equals(searchData.getMerchantOrderId())) {
			body.put("merchantOrderId", searchData.getMerchantOrderId());
		}
		if (searchData.getOrderSearchStatus() != null && !"".equals(searchData.getOrderSearchStatus())) {
			body.put("orderSearchStatus", searchData.getOrderSearchStatus());
		}
		if (searchData.getOrderSearchType() != null && !"".equals(searchData.getOrderSearchType())) {
			body.put("orderSearchType", searchData.getOrderSearchType());
		}
		if (searchData.getPayMode() != null && !"".equals(searchData.getPayMode())) {
			body.put("payMode", searchData.getPayMode());
		}
		body.put("mid", KeyProperty.MID);
		String checksum = PaytmChecksum.generateSignature(body.toString(), KeyProperty.MERCHANT_KEY);
		JSONObject head = new JSONObject();
		head.put("tokenType", "CHECKSUM");
		head.put("signature", checksum);
		head.put("requestTimestamp", "");

		paytmParams.put("body", body);
		paytmParams.put("head", head);

		String post_data = paytmParams.toString();
		System.out.println(">>>>>post_data>>>>>>" + post_data);
		System.out.println(">>>>>checksum>>>>>>" + checksum);

		URL url = new URL(propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_ORDER_LIST_URL));
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
				System.out.println(">>>>>>>>>>>>>>>" + responseData);
				if (json.get("body") != null) {
					JSONObject jsonBody = (JSONObject) parser.parse(json.get("body").toString());
					JSONObject resultInfo = (JSONObject) parser.parse(jsonBody.get("resultInfo").toString());
					if ("SUCCESS".equals(resultInfo.get("resultStatus").toString())) {
						JSONArray orderList = (JSONArray) jsonBody.get("orders");
						orderListDTO.setOrders(orderList);
					} else {
						orderListDTO.setMessage("Unable to fetch list at the moment. Please try again.");
					}
				} else {
					orderListDTO.setMessage("Unable to fetch list at the moment. Please try again.");
				}
			}
			responseReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
			orderListDTO.setMessage("Unable to fetch list at the moment. Please try again.");
		}
		return new ResponseEntity<OrderListDTO>(orderListDTO, HttpStatus.OK);
	}
}
