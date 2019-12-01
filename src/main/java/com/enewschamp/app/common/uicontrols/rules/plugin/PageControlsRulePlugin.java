package com.enewschamp.app.common.uicontrols.rules.plugin;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.rules.plugin.IPageNavRuleDataPlugin;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "PageControlsRulePlugin")
public class PageControlsRulePlugin implements IPageNavRuleDataPlugin {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isNextPageAvailable(PageRequestDTO pageRequest, PageDTO page) {
		return (page.getHeader().getPageNo() < page.getHeader().getPageCount());
	}

	public boolean isPreviousPageAvailable(PageRequestDTO pageRequest, PageDTO page) {
		return (page.getHeader().getPageNo() > 1);
	}

	@Override
	public Map<String, String> loadPluginData(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
