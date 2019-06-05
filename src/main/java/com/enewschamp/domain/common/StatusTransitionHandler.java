package com.enewschamp.domain.common;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.problem.BusinessException;

@Component
public class StatusTransitionHandler {

	@Autowired
	private EnewschampApplicationProperties appConfig;
	
	public void validateStatusTransition(StatusTransitionDTO transition) {
		
	}
	
	public String findNextStatus(StatusTransitionDTO transition) {
		String toStatus = null;
		Map<String, Map<String, String>> entityStatusTransitionConfig = appConfig.getStatusTransitionConfig().get(transition.getEntityName());
		if(entityStatusTransitionConfig != null) {
			Map<String, String> statusWiseTransitions = entityStatusTransitionConfig.get(transition.getFromStatus());
			if(statusWiseTransitions != null) {
				toStatus = statusWiseTransitions.get(transition.getAction());
			}
		}
		if(toStatus == null) {
			throw new BusinessException(ErrorCodes.STATUS_TRANSITION_NOT_FOUND);
		}
		return toStatus;
	}
	
}
