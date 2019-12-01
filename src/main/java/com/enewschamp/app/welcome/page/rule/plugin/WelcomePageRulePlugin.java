package com.enewschamp.app.welcome.page.rule.plugin;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.rules.plugin.IPageNavRuleDataPlugin;
import com.enewschamp.app.school.service.SchoolPricingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "WelcomePageRulePlugin")
public class WelcomePageRulePlugin implements IPageNavRuleDataPlugin {

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
		return dataMap;
	}

}
