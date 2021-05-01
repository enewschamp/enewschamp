package com.enewschamp.app.common.uicontrols.rules.plugin;

import java.io.Serializable;
import java.util.Map;

import com.enewschamp.app.common.PageRequestDTO;

public interface IUIRuleDataPlugin extends Serializable {

	public Map<String, String> loadPluginData(PageRequestDTO pageRequest, Map<String, String> dataMap);
}
