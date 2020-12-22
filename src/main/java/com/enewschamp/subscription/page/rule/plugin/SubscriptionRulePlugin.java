package com.enewschamp.subscription.page.rule.plugin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.rules.plugin.IPageNavRuleDataPlugin;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;

@Component(value = "SubscriptionRulePlugin")
public class SubscriptionRulePlugin implements IPageNavRuleDataPlugin {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	StudentControlBusiness StudentControlBusiness;

	@Override
	public Map<String, String> loadPluginData(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {

		return dataMap;
	}

	public Map<String, String> getSubscriptionType(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		dataMap.put("subscriptionType", pageRequest.getData().get("subscriptionSelected").asText());
		return dataMap;
	}
}
