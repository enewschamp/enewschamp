package com.enewschamp.app.fw.page.navigation.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.BusinessRulesPlugin;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorRulesDTO;
import com.enewschamp.app.fw.page.navigation.rules.plugin.IPageNavRuleDataPlugin;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationRulesService;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

@Service
public class PageNavRuleExecutionService {

	@Autowired
	private ApplicationContext context;

	@Autowired
	PageNavigationRulesService pageNavigationRulesService;

	@Autowired
	BusinessRulesPlugin businessRulePlugin;

	private List<PageNavigatorRulesDTO> getAdditionalRules(Long navId) {
		List<PageNavigatorRulesDTO> rulesList = pageNavigationRulesService.getNavRuleList(navId);
		return rulesList;
	}

	public Map<String, String> getNextPageBasedOnRule(PageRequestDTO pageRequest, PageDTO page,
			PageNavigatorDTO pageNavDto) {
		Long navId = pageNavDto.getNavId();
		List<PageNavigatorRulesDTO> rules = getAdditionalRules(navId);
		Map<String, String> dataMap = new HashMap<String, String>();
		Map<String, String> nextPageData = new HashMap<String, String>();
		boolean successEval = false;
		for (PageNavigatorRulesDTO rule : rules) {
			String ruleStr = rule.getRule();
			if (!"".equals(ruleStr)) {
				dataMap = businessRulePlugin.initializeRuleVariables(pageRequest, page, ruleStr, dataMap);
				// System.out.println(">>>>>ruleStr>>>>>" + ruleStr);
				successEval = execRule(ruleStr, dataMap);
				// System.out.println(">>>>>dataMap>>>>>" + dataMap);
				// System.out.println(">>>>>successEval>>>>>" + successEval);
				if (successEval == true) {
					nextPageData.put("nextPage", rule.getNextPage());
					nextPageData.put("nextPageOperation", rule.getNextPageOperation());
					nextPageData.put("nextPageLoadMethod", rule.getNextPageLoadMethod());
					nextPageData.put("controlWorkEntryOrExit", rule.getControlWorkEntryOrExit());
					break;
				}
			}
		}
		return nextPageData;
	}

	private boolean execRule(String ruleStr, Map<String, String> dataMap) {
		// System.out.println("ruleStr :" + ruleStr);

		boolean success = false;

		Evaluator eval = new Evaluator();

		eval.setVariables(dataMap);
		// System.out.println(eval.getVariables());
		try {

			// System.out.println("On evaluation :"+ eval.evaluate(ruleStr));
			String result = eval.evaluate(ruleStr);
			// System.out.println("String result " + result + "Boolean result : " +
			// eval.getBooleanResult(ruleStr));

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
