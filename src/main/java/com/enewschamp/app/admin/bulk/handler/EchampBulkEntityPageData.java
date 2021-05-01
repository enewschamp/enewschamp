package com.enewschamp.app.admin.bulk.handler;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.enewschamp.app.common.uicontrols.dto.UIControlsGlobalDTO;
import com.enewschamp.app.common.uicontrols.dto.UIControlsRulesDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorRulesDTO;
import com.enewschamp.common.domain.entity.ErrorCodes;
import com.enewschamp.common.domain.entity.PropertiesBackend;
import com.enewschamp.common.domain.entity.PropertiesFrontend;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EchampBulkEntityPageData extends PageData {

	private static final long serialVersionUID = 1L;
	private List<PageNavigatorDTO> pageNavigators;
	private List<PageNavigatorRulesDTO> pageNavigatorRules;
	private List<UIControlsGlobalDTO> uiControlsGlobals;
	private List<UIControlsDTO> uiControls;
	private List<UIControlsRulesDTO> uiControlsRules;
	private List<PropertiesBackend> propertiesBackends;
	private List<PropertiesFrontend> propertiesFrontends;
	private List<ErrorCodes> errorCodes;

}
