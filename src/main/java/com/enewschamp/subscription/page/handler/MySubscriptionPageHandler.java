package com.enewschamp.subscription.page.handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.KeyProperty;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.common.country.service.CountryService;
import com.enewschamp.app.common.state.service.StateService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.school.service.SchoolService;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.MySubscriptionPageData;
import com.enewschamp.subscription.app.dto.StudentPaymentDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.SchoolDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentShareAchievementsBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.entity.StudentPayment;
import com.enewschamp.subscription.domain.entity.StudentPaymentFailed;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionHistory;
import com.enewschamp.subscription.domain.service.StudentPaymentFailedService;
import com.enewschamp.subscription.domain.service.StudentPaymentService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionHistoryService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.enewschamp.user.app.dto.StudentRefundDTO;
import com.enewschamp.user.domain.entity.StudentRefund;
import com.enewschamp.user.domain.service.StudentRefundService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.pg.merchant.PaytmChecksum;

@Component(value = "MySubscriptionPageHandler")
public class MySubscriptionPageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	StudentRegistrationService studentRegistrationService;

	@Autowired
	StudentRefundService studentRefundService;

	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Autowired
	SchoolDetailsBusiness schoolDetailsBusiness;

	@Autowired
	StudentDetailsBusiness studentDetailsBusiness;

	@Autowired
	StudentShareAchievementsBusiness studentShareAchievementsBusiness;

	@Autowired
	SubscriptionBusiness studentSubscriptionBusiness;

	@Autowired
	StudentSubscriptionService studentSubscriptionService;

	@Autowired
	StudentSubscriptionHistoryService studentSubscriptionHistoryService;

	@Autowired
	StudentPaymentService studentPaymentService;

	@Autowired
	StudentPaymentFailedService studentPaymentFailedService;

	@Autowired
	PropertiesBackendService propertiesService;

	@Autowired
	CityService cityService;

	@Autowired
	CountryService countryService;

	@Autowired
	StateService stateService;

	@Autowired
	SchoolService schoolService;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		MySubscriptionPageData mySubscriptionPageData = new MySubscriptionPageData();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentSubscriptionDTO studentSubscriptionDTO = studentSubscriptionBusiness
				.getStudentSubscriptionFromMaster(studentId, editionId);
		List<StudentPayment> studentPaymentList = studentPaymentService.getAllByStudentIdAndEdition(studentId,
				editionId);
		List<StudentPaymentDTO> studentPaymentListDTO = new ArrayList<StudentPaymentDTO>();
		for (int i = 0; i < studentPaymentList.size(); i++) {
			StudentPaymentDTO studentPaymentDTO = modelMapper.map(studentPaymentList.get(i), StudentPaymentDTO.class);
			studentPaymentListDTO.add(studentPaymentDTO);
		}
		List<StudentPaymentFailed> studentPaymentFailedList = studentPaymentFailedService
				.getAllByStudentIdAndEdition(studentId, editionId);
		List<StudentPaymentDTO> studentPaymentFailedListDTO = new ArrayList<StudentPaymentDTO>();
		for (int i = 0; i < studentPaymentFailedList.size(); i++) {
			StudentPaymentDTO studentPaymentDTO = modelMapper.map(studentPaymentFailedList.get(i),
					StudentPaymentDTO.class);
			studentPaymentFailedListDTO.add(studentPaymentDTO);
		}

		List<StudentRefund> studentRefundList = studentRefundService.getAllByStudentIdAndEdition(studentId, editionId);
		List<StudentRefundDTO> studentRefundDTOList = new ArrayList<StudentRefundDTO>();
		for (int i = 0; i < studentRefundList.size(); i++) {
			StudentRefundDTO studentRefundDTO = modelMapper.map(studentRefundList.get(i), StudentRefundDTO.class);
			studentRefundDTOList.add(studentRefundDTO);
		}

		List<StudentSubscriptionHistory> studentSubscriptionList = studentSubscriptionHistoryService
				.getAllByStudentIdAndEdition(studentId, editionId);
		mySubscriptionPageData.setPaymentHistory(studentPaymentListDTO);
		mySubscriptionPageData.setFailedPaymentHistory(studentPaymentFailedListDTO);
		mySubscriptionPageData.setRefundHistory(studentRefundDTOList);
		mySubscriptionPageData.setSubscriptionHistory(studentSubscriptionList);
		mySubscriptionPageData.setSubscription(studentSubscriptionDTO);
		pageDto.setData(mySubscriptionPageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
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
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
				}
			} catch (NoSuchMethodException nsmEx) {
				nsmEx.printStackTrace();
			} catch (SecurityException seEx) {
				seEx.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handleCancelSubscriptionAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		String operation = pageRequest.getHeader().getOperation();
		String editionId = pageRequest.getHeader().getEditionId();
		String emailId = pageRequest.getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentSubscriptionDTO studentSubscriptionDTO = modelMapper
				.map(studentSubscriptionService.get(studentId, editionId), StudentSubscriptionDTO.class);
		try {
			JSONObject paytmParams = new JSONObject();
			JSONObject body = new JSONObject();
			body.put("mid", KeyProperty.MID);
			body.put("subsId", studentSubscriptionDTO.getSubscriptionId());
			String checksum = PaytmChecksum.generateSignature(body.toString(), KeyProperty.MERCHANT_KEY);
			JSONObject head = new JSONObject();
			head.put("signature", checksum);
			head.put("tokenType", "AES");
			paytmParams.put("body", body);
			paytmParams.put("head", head);

			String post_data = paytmParams.toString();
			URL url = new URL(
					propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_CANCEL_SUBSCRIPTION_URL));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(post_data);
			System.out.println(">>>>>>>>>>>>>>post_data>>>>>>>>>>>>" + post_data);
			System.out.println(">>>>>>>>>>>>>>URL>>>>>>>>>>>>"
					+ propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_CANCEL_SUBSCRIPTION_URL));
			requestWriter.close();
			String responseData = "";
			InputStream is = connection.getInputStream();
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
			if ((responseData = responseReader.readLine()) != null) {
				System.out.println(">>>>>>>>>>>>>>responseData>>>>>>>>>>>>" + responseData);
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(responseData);
				if (json.get("body") != null) {
					JSONObject jsonBody = (JSONObject) parser.parse(json.get("body").toString());
					JSONObject resultInfo = (JSONObject) parser.parse(jsonBody.get("resultInfo").toString());
					String status = resultInfo.get("status").toString();
					if ("SUCCESS".equals(status)) {
						studentSubscriptionDTO.setAutoRenewal("N");
						StudentSubscription studentSubscription = modelMapper.map(studentSubscriptionDTO,
								StudentSubscription.class);
						studentSubscriptionService.update(studentSubscription);
					} else {
						responseReader.close();
						throw new BusinessException(ErrorCodeConstants.UNSUBSCRIBE_NOT_SUCCESSFUL);
					}
				}
			}
			responseReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new BusinessException(ErrorCodeConstants.UNSUBSCRIBE_NOT_SUCCESSFUL);
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

}
