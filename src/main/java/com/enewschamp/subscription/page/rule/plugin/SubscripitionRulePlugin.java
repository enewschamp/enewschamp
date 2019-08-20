package com.enewschamp.subscription.page.rule.plugin;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.rules.plugin.IPageNavRuleDataPlugin;

@Component(value = "SubscripitionRulePlugin")
public class SubscripitionRulePlugin implements IPageNavRuleDataPlugin {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Map<String, String> loadPluginData(PageRequestDTO pageRequest, Map<String, String> dataMap) {

		return dataMap;
	}

}
