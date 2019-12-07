package com.enewschamp.app.launchapp.page.rule.plugin;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.rules.plugin.IPageNavRuleDataPlugin;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "LaunchAppRulePlugin")
public class LaunchAppRulePlugin implements IPageNavRuleDataPlugin {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	StudentRegistrationService regService;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Map<String, String> loadPluginData(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {
		return dataMap;
	}

	public Map<String, String> validateLoginCredentials(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String Fn_ValidateLoginCredentials = "Pass";
<<<<<<< Updated upstream
		String emailId = pageRequest.getHeader().getEmailID();
		//String deviceId = pageRequest.getHeader().getDeviceId();
=======
		String emailId = pageRequest.getHeader().getEmailId();
		// String deviceId = pageRequest.getHeader().getDeviceId();
>>>>>>> Stashed changes
		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			Fn_ValidateLoginCredentials = "Fail";
		}
		dataMap.put("Fn_ValidateLoginCredentials", Fn_ValidateLoginCredentials);
		return dataMap;
	}

}
