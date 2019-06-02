package com.enewschamp.domain.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;

@Component
public class StatusTransitionHandler {

	@Autowired
	private EnewschampApplicationProperties appConfig;
	
	public void validateStatusTransition(StatusTransitionDTO transition) {
		
	}
	
//	public String findNextStatus(StatusTransitionDTO transition) {
//		return appConfig.getStatusTransitionConfig().get(transition.getEntityName()).get(transition.getFromStatus()).get(transition.getAction());
//	}
	
}
