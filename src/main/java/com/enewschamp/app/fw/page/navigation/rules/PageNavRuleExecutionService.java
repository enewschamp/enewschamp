package com.enewschamp.app.fw.page.navigation.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorRulesDTO;
import com.enewschamp.app.fw.page.navigation.rules.plugin.IPageNavRuleDataPlugin;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationRulesService;

import net.sourceforge.jeval.EvaluationConstants;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

@Service
public class PageNavRuleExecutionService {

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	PageNavigationRulesService pageNavigationRulesService;
	
	private List<PageNavigatorRulesDTO> getAdditionalRules(Long navId)
	{
		List<PageNavigatorRulesDTO> rulesList = pageNavigationRulesService.getNavRuleList(navId);
		return rulesList;
	}
	
	public String getNextPageBasedOnRule(PageRequestDTO pageRequest,PageNavigatorDTO pageNavDto)
	{
		Long navId = pageNavDto.getNavId();
		List<PageNavigatorRulesDTO> rules = getAdditionalRules(navId);
		Map<String, String> dataMap = new HashMap<String,String>();
		dataMap = enrichData(dataMap, pageRequest);
		boolean successEval=false;
		String nextPage="";
		for(PageNavigatorRulesDTO rule: rules)
		{
			if(rule.getPluginClass()!=null) {
			dataMap = getPluginClassBean(rule.getPluginClass()).loadPluginData(pageRequest, dataMap);
			
			String ruleStr = rule.getRule();
			
			successEval = execRule(ruleStr,dataMap);
			if(successEval==true){
				nextPage = rule.getNextpage();
				break;
				}
			}
		}
		return nextPage;
	}
	
	private Map<String, String> enrichData(Map<String, String> dataMap,final PageRequestDTO pageRequest )
	{
		dataMap.put("operation", pageRequest.getHeader().getOperation());
		dataMap.put("action",pageRequest.getHeader().getAction());
		dataMap.put("editionId",pageRequest.getHeader().getEditionID());
		dataMap.put("currentPageName", pageRequest.getHeader().getPageName());
		//dataMap.put("publicationDate", pageRequest.getHeader().getPublicationdate().toString());
		dataMap.put("emailId",  pageRequest.getHeader().getEmailID());

		return dataMap;
	}
	
	private boolean execRule(String ruleStr, Map<String, String> dataMap)
	{
		System.out.println("ruleStr :"+ruleStr);
		
		boolean success = false;
		
		Evaluator eval = new Evaluator();
		
		eval.setVariables(dataMap);
		System.out.println(eval.getVariables());
		try {

			//System.out.println("On evaluation :"+ eval.evaluate(ruleStr));
			String result = eval.evaluate(ruleStr);
			System.out.println("String result "+result+"Boolean result : "+eval.getBooleanResult(ruleStr));

			if(eval.getBooleanResult(ruleStr))
			{
				success=true;
			}
			
		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
	private IPageNavRuleDataPlugin getPluginClassBean(String pluginClass)
	{
		return (IPageNavRuleDataPlugin)context.getBean(pluginClass);
	}
}
