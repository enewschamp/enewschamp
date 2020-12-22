package com.enewschamp.app.common.uicontrols.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.BusinessRulesPlugin;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.enewschamp.app.common.uicontrols.dto.UIControlsRulesDTO;
import com.enewschamp.app.common.uicontrols.service.UIControlsRulesService;
import com.enewschamp.app.fw.page.navigation.rules.plugin.IPageNavRuleDataPlugin;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

@Service
public class UIControlsRuleExecutionService {

	@Autowired
	private ApplicationContext context;

	@Autowired
	UIControlsRulesService uiControlsRulesService;

	@Autowired
	BusinessRulesPlugin businessRulePlugin;

	private List<UIControlsRulesDTO> getAdditionalRules(Long uiControlId) {
		List<UIControlsRulesDTO> rulesList = uiControlsRulesService.getUIControlsRuleList(uiControlId);
		return rulesList;
	}

	public UIControlsDTO getControlParamsBasedOnRule(PageRequestDTO pageRequest, PageDTO pageResponse,
			UIControlsDTO uiControlsDTO) {
		Long uiControld = uiControlsDTO.getUiControlId();
		System.out.println(">>>>>>>>>uiControld>>>>>>>>>>" + uiControld);
		List<UIControlsRulesDTO> rules = getAdditionalRules(uiControld);
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap = enrichData(dataMap, pageRequest);
		boolean successEval = false;
		for (UIControlsRulesDTO rule : rules) {
			String ruleStr = rule.getRule();
			if (!"".equals(ruleStr)) {
				dataMap = businessRulePlugin.initializeRuleVariables(pageRequest, pageResponse, ruleStr, dataMap);
				successEval = execRule(ruleStr, dataMap);
				if (successEval == true) {
					if (rule.getVisibility() != null && !"".equals(rule.getVisibility())) {
						uiControlsDTO.setVisibility(rule.getVisibility());
					}
					if (rule.getAction() != null && !"".equals(rule.getAction())) {
						uiControlsDTO.setAction(rule.getAction());
					}
					if (rule.getMandatory() != null && !"".equals(rule.getMandatory())) {
						uiControlsDTO.setMandatory(rule.getMandatory());
					}
					break;
				}
			}
		}
		return uiControlsDTO;
	}

	private Map<String, String> enrichData(Map<String, String> dataMap, final PageRequestDTO pageRequest) {
		dataMap.put("operation", pageRequest.getHeader().getOperation());
		dataMap.put("action", pageRequest.getHeader().getAction());
		dataMap.put("editionId", pageRequest.getHeader().getEditionId());
		dataMap.put("currentPageName", pageRequest.getHeader().getPageName());
		dataMap.put("emailId", pageRequest.getHeader().getEmailId());
		return dataMap;
	}

	private boolean execRule(String ruleStr, Map<String, String> dataMap) {
		System.out.println("ruleStr :" + ruleStr);
		boolean success = false;
		Evaluator eval = new Evaluator();
		eval.setVariables(dataMap);
		System.out.println(eval.getVariables());
		try {
			String result = eval.evaluate(ruleStr);
			System.out.println("String result " + result + "Boolean result : " + eval.getBooleanResult(ruleStr));
			if (eval.getBooleanResult(ruleStr)) {
				success = true;
			}
		} catch (EvaluationException e) {
			e.printStackTrace();
		}
		return success;
	}

	private IPageNavRuleDataPlugin getPluginClassBean(String pluginClass) {
		return (IPageNavRuleDataPlugin) context.getBean(pluginClass);
	}
}
