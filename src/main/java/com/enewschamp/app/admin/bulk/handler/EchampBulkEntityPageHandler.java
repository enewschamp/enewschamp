package com.enewschamp.app.admin.bulk.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.admin.page.navigator.service.PageNavigatorService;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.uicontrols.entity.UIControls;
import com.enewschamp.app.common.uicontrols.entity.UIControlsGlobal;
import com.enewschamp.app.common.uicontrols.entity.UIControlsRules;
import com.enewschamp.app.common.uicontrols.service.UIControlsGlobalService;
import com.enewschamp.app.common.uicontrols.service.UIControlsRulesService;
import com.enewschamp.app.common.uicontrols.service.UIControlsService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.entity.PageNavigator;
import com.enewschamp.app.fw.page.navigation.entity.PageNavigatorRules;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationRulesService;
import com.enewschamp.common.domain.entity.ErrorCodes;
import com.enewschamp.common.domain.entity.PropertiesBackend;
import com.enewschamp.common.domain.entity.PropertiesFrontend;
import com.enewschamp.common.domain.service.ErrorCodesService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.common.domain.service.PropertiesFrontendService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("BulkUploadPageHandler")
@Transactional
public class EchampBulkEntityPageHandler implements IPageHandler {
	@Autowired
	private PageNavigatorService pageNavigatorService;
	
	@Autowired
	private PageNavigationRulesService pageNavigationRulesService;
	
	@Autowired
	private UIControlsGlobalService uiControlsGlobalService;
	
	@Autowired
	private UIControlsService uiControlsService;
	
	@Autowired
	private UIControlsRulesService uiControlsRulesService;
	
	@Autowired
	private PropertiesBackendService propertiesBackendService;
	
	@Autowired
	private PropertiesFrontendService propertiesFrontendService;
	
	@Autowired
	private ErrorCodesService errorCodeService;
	
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Insert":
			pageDto = insertAll(pageRequest);
			break;
		default:
			break;
		}
		return pageDto;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@SneakyThrows
	@Transactional
	private PageDTO insertAll(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EchampBulkEntityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				EchampBulkEntityPageData.class);
		
		List<PageNavigator> pageNavigators = mapPageNavigators(pageRequest, pageData);
		List<PageNavigatorRules> pageNavigatorRules = mapPageNavigatorRules(pageRequest, pageData);
		List<UIControlsGlobal> uiControlsGlobals = mapUiControlsGlobals(pageRequest, pageData);
		List<UIControls> uiControls = mapUIControls(pageRequest, pageData);
		List<UIControlsRules> uiControlsRules = mapUIControlsRules(pageRequest, pageData);
		List<PropertiesBackend> propertiesBackends = mapPropertiesBackends(pageRequest, pageData);
		List<PropertiesFrontend> propertiesFrontends =  mapPropertiesFrontends(pageRequest, pageData);
		List<ErrorCodes> errorCodes = mapErrorCodes(pageRequest, pageData);
		
		pageNavigatorService.createAll(pageNavigators);
		pageNavigationRulesService.createAll(pageNavigatorRules);
		uiControlsGlobalService.createAll(uiControlsGlobals);
		uiControlsService.createAll(uiControls);
		uiControlsRulesService.createAll(uiControlsRules);
		propertiesBackendService.createAll(propertiesBackends);
		propertiesFrontendService.createAll(propertiesFrontends);
		errorCodeService.createAll(errorCodes);
		pageDto.setData(null);
		return pageDto;
	}


	private List<PageNavigator> mapPageNavigators(PageRequestDTO pageRequest, EchampBulkEntityPageData pageData) {
		List<PageNavigator> pageNavigators = pageData.getPageNavigators()
				  .stream()
				  .map(navigator -> modelMapper.map(navigator, PageNavigator.class))
				  .collect(Collectors.toList());	
		return pageNavigators;
	}

	private List<PageNavigatorRules> mapPageNavigatorRules(PageRequestDTO pageRequest, EchampBulkEntityPageData pageData) {
		List<PageNavigatorRules> pageNavigatorRules = pageData.getPageNavigatorRules()
				  .stream()
				  .map(navigatorRules -> modelMapper.map(navigatorRules, PageNavigatorRules.class))
				  .collect(Collectors.toList());	
		return pageNavigatorRules;
	}
	
	private List<UIControlsGlobal> mapUiControlsGlobals(PageRequestDTO pageRequest, EchampBulkEntityPageData pageData) {
		List<UIControlsGlobal> uiControlsGlobals = pageData.getUiControlsGlobals()
				  .stream()
				  .map(uiControlsGlobal -> modelMapper.map(uiControlsGlobal, UIControlsGlobal.class))
				  .collect(Collectors.toList());	
		return uiControlsGlobals;
	}
	
	private List<UIControls> mapUIControls(PageRequestDTO pageRequest, EchampBulkEntityPageData pageData) {
		List<UIControls> uiControls = pageData.getUiControls()
				  .stream()
				  .map(uiControl -> modelMapper.map(uiControl, UIControls.class))
				  .collect(Collectors.toList());	
		return uiControls;
	}
	
	private List<UIControlsRules> mapUIControlsRules(PageRequestDTO pageRequest, EchampBulkEntityPageData pageData) {
		List<UIControlsRules> uiControlsRules = pageData.getUiControlsRules()
				  .stream()
				  .map(uiControlRule -> modelMapper.map(uiControlRule, UIControlsRules.class))
				  .collect(Collectors.toList());	
		return uiControlsRules;
	}
	
	private List<PropertiesBackend> mapPropertiesBackends(PageRequestDTO pageRequest, EchampBulkEntityPageData pageData) {
		List<PropertiesBackend> propertiesBackends = pageData.getPropertiesBackends()
				  .stream()
				  .map(propertiesBackend -> modelMapper.map(propertiesBackend, PropertiesBackend.class))
				  .collect(Collectors.toList());	
		return propertiesBackends;
	}
	
	private List<PropertiesFrontend> mapPropertiesFrontends(PageRequestDTO pageRequest, EchampBulkEntityPageData pageData) {
		List<PropertiesFrontend> propertiesFrontends = pageData.getPropertiesFrontends()
				  .stream()
				  .map(propertiesFrontend -> modelMapper.map(propertiesFrontend, PropertiesFrontend.class))
				  .collect(Collectors.toList());	
		return propertiesFrontends;
	}
	
	private List<ErrorCodes> mapErrorCodes(PageRequestDTO pageRequest, EchampBulkEntityPageData pageData) {
		List<ErrorCodes> propertiesFrontends = pageData.getErrorCodes()
				  .stream()
				  .map(errorCode -> modelMapper.map(errorCode, ErrorCodes.class))
				  .collect(Collectors.toList());	
		return propertiesFrontends;
	}
	
	
}
