package com.enewschamp.app.fw.page.navigation.rules;

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
		dataMap = enrichData(dataMap, pageRequest);
		boolean successEval = false;
		for (PageNavigatorRulesDTO rule : rules) {
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
						dataMap = (Map<String, String>) m.invoke(plugin, pageRequest, page, dataMap);
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
				System.out.println(">>>>>dataMap>>>>>" + dataMap);
				System.out.println(">>>>>ruleStr>>>>>" + ruleStr);
				System.out.println(">>>>>successEval>>>>>" + successEval);
				if (successEval == true) {
					nextPageData.put("nextPage", rule.getNextPage());
					nextPageData.put("nextPageOperation", rule.getNextPageOperation());
					break;
				}
			}
		}
		return nextPageData;
	}

	private Map<String, String> enrichData(Map<String, String> dataMap, final PageRequestDTO pageRequest) {
		dataMap.put("operation", pageRequest.getHeader().getOperation());
		dataMap.put("action", pageRequest.getHeader().getAction());
<<<<<<< Updated upstream
		dataMap.put("editionId", pageRequest.getHeader().getEditionID());
		dataMap.put("currentPageName", pageRequest.getHeader().getPageName());
		// dataMap.put("publicationDate",
		// pageRequest.getHeader().getPublicationdate().toString());
		dataMap.put("emailId", pageRequest.getHeader().getEmailID());
=======
		dataMap.put("editionId", pageRequest.getHeader().getEditionId());
		dataMap.put("currentPageName", pageRequest.getHeader().getPageName());
		// dataMap.put("publicationDate",
		// pageRequest.getHeader().getPublicationDate().toString());
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
