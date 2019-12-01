package com.enewschamp.subscription.page.rule.plugin;

import java.io.IOException;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.rules.plugin.IPageNavRuleDataPlugin;
import com.enewschamp.app.school.entity.SchoolPricing;
import com.enewschamp.app.school.service.SchoolPricingService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentSchoolPageData;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "SubscripitionSchoolDetailsRulePlugin")
public class SubscripitionSchoolDetailsRulePlugin implements IPageNavRuleDataPlugin {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ObjectMapper objectMapper;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	SchoolPricingService schoolPricingService;
	
	@Override
	public Map<String, String> loadPluginData(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {

		StudentSchoolPageData studentSchoolPageData = null;
		try {
			studentSchoolPageData = objectMapper.readValue(pageRequest.getData().toString(),
					StudentSchoolPageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodes.SREVER_ERROR,
					"Error in mapping Subscription Period Page Data fields. Contact System administrator!");

		}
		Long schoolId = studentSchoolPageData.getSchoolId();
		String editionId = pageRequest.getHeader().getEditionID();
		
		SchoolPricing schoolpricing = schoolPricingService.getPricingForInstitution(schoolId, editionId);
		if(schoolpricing!=null)
		{
			dataMap.put("schoolPayment", "Y");
		}
		else
		{
			dataMap.put("schoolPayment", "N");
	
		}
		return dataMap;
	}
	
	

}
