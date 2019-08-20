package com.enewschamp.app.fw.page.navigation.rules.plugin;

import java.io.Serializable;
import java.util.Map;

import com.enewschamp.app.common.PageRequestDTO;

public interface IPageNavRuleDataPlugin extends Serializable {

	public Map<String, String> loadPluginData(PageRequestDTO pageRequest, Map<String, String> dataMap);
}
