package com.enewschamp.app.common.uicontrols.rules;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.enewschamp.app.common.uicontrols.dto.UIControlsRulesDTO;
import com.enewschamp.app.common.uicontrols.rules.plugin.PageControlsRulePlugin;
import com.enewschamp.app.common.uicontrols.service.UIControlsRulesService;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
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
	PageControlsRulePlugin pageControlRulePlugin;

	private List<UIControlsRulesDTO> getAdditionalRules(Long uiControlId) {
		List<UIControlsRulesDTO> rulesList = uiControlsRulesService.getUIControlsRuleList(uiControlId);
		return rulesList;
	}

	public UIControlsDTO getControlParamsBasedOnRule(PageRequestDTO pageRequest, PageDTO pageResponse,
			UIControlsDTO uiControlsDTO) {
		if (PageAction.next.toString().equalsIgnoreCase(uiControlsDTO.getAction())) {
			uiControlsDTO.setVisibility(
					(pageControlRulePlugin.isNextPageAvailable(pageRequest, pageResponse)) ? "VISIBLE" : "HIDDEN");
		} else if (PageAction.previous.toString().equalsIgnoreCase(uiControlsDTO.getAction())) {
			uiControlsDTO.setVisibility(
					(pageControlRulePlugin.isPreviousPageAvailable(pageRequest, pageResponse)) ? "VISIBLE" : "HIDDEN");
		} else if (PageAction.LeftSwipe.toString().equalsIgnoreCase(uiControlsDTO.getAction())) {
			uiControlsDTO.setVisibility(
					(pageControlRulePlugin.isNextPageAvailable(pageRequest, pageResponse)) ? "VISIBLE" : "HIDDEN");
		} else if (PageAction.RightSwipe.toString().equalsIgnoreCase(uiControlsDTO.getAction())) {
			uiControlsDTO.setVisibility(
					(pageControlRulePlugin.isPreviousPageAvailable(pageRequest, pageResponse)) ? "VISIBLE" : "HIDDEN");
		}
		Long uiControld = uiControlsDTO.getUiControlId();
		List<UIControlsRulesDTO> rules = getAdditionalRules(uiControld);
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap = enrichData(dataMap, pageRequest);
		boolean successEval = false;
		for (UIControlsRulesDTO rule : rules) {
			if (rule.getPluginClass() != null) {
				String className = rule.getPluginClass().split(">>")[0];
				String methodName = rule.getPluginClass().split(">>")[1];
				Class cls = getPluginClassBean(className).getClass();
				Class[] params = new Class[3];
				params[0] = PageRequestDTO.class;
				params[1] = PageDTO.class;
				params[2] = Map.class;
				try {
					IPageNavRuleDataPlugin plugin = getPluginClassBean(className);
					Method m = cls.getDeclaredMethod(methodName, params);
					try {
						dataMap = (Map<String, String>) m.invoke(plugin, pageRequest, pageResponse, dataMap);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String ruleStr = rule.getRule();
				successEval = execRule(ruleStr, dataMap);
				if (successEval == true) {
					if (rule.getVisibility() != null && rule.getVisibility() != "") {
						uiControlsDTO.setVisibility(rule.getVisibility());
					}
					if (rule.getAction() != null && rule.getAction() != "") {
						uiControlsDTO.setAction(rule.getAction());
					}
					if (rule.getMandatory() != null && rule.getMandatory() != "") {
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
<<<<<<< Updated upstream
		dataMap.put("editionId", pageRequest.getHeader().getEditionID());
		dataMap.put("currentPageName", pageRequest.getHeader().getPageName());
		dataMap.put("emailId", pageRequest.getHeader().getEmailID());
=======
		dataMap.put("editionId", pageRequest.getHeader().getEditionId());
		dataMap.put("currentPageName", pageRequest.getHeader().getPageName());
		dataMap.put("emailId", pageRequest.getHeader().getEmailId());
>>>>>>> Stashed changes

		return dataMap;
	}

	private boolean execRule(String ruleStr, Map<String, String> dataMap) {
		System.out.println("ruleStr :" + ruleStr);

		boolean success = false;

		Evaluator eval = new Evaluator();

		eval.setVariables(dataMap);
		System.out.println(eval.getVariables());
		try {

			// System.out.println("On evaluation :"+ eval.evaluate(ruleStr));
			String result = eval.evaluate(ruleStr);
			System.out.println("String result " + result + "Boolean result : " + eval.getBooleanResult(ruleStr));

			if (eval.getBooleanResult(ruleStr)) {
				success = true;
			}

		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return success;
	}

	private IPageNavRuleDataPlugin getPluginClassBean(String pluginClass) {
		return (IPageNavRuleDataPlugin) context.getBean(pluginClass);
	}
}
