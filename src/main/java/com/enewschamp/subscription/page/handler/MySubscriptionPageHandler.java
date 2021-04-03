package com.enewschamp.subscription.page.handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
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
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.SchoolDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentShareAchievementsBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.entity.StudentPayment;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.service.StudentPaymentService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
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
	StudentPaymentService studentPaymentService;

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
		List<StudentPayment> studentPayment = studentPaymentService.getAllByStudentIdAndEdition(studentId, editionId);
		mySubscriptionPageData.setPaymentHistory(studentPayment);
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
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			try {
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
					// e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handleCancelSubscription(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		String operation = pageRequest.getHeader().getOperation();
		String editionId = pageRequest.getHeader().getEditionId();
		String emailId = pageRequest.getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentSubscription studentSubscription = studentSubscriptionService.get(studentId, editionId);
		try {
			JSONObject paytmParams = new JSONObject();
			JSONObject body = new JSONObject();
			body.put("mid", propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MID));
			body.put("subsId", studentSubscription.getSubscriptionId());

			String checksum = PaytmChecksum.generateSignature(body.toString(),
					propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_MERCHANT_KEY));

			JSONObject head = new JSONObject();
			head.put("signature", checksum);

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
					String status = resultInfo.get("message").toString();
					if ("SUCCESS".equals(status)) {
						studentSubscription.setAutoRenewal("N");
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
