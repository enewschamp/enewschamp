package com.enewschamp.domain.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.entity.UserRoleKey;
import com.enewschamp.user.domain.service.UserRoleService;

import lombok.extern.java.Log;

@Component
@Log
public class StatusTransitionHandler {

	@Autowired
	UserRoleService userRoleService;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	public void validateStatusTransition(StatusTransitionDTO transition) {

	}

	public String findNextStatus(StatusTransitionDTO transition) {
		String toStatus = null;
		Map<String, Map<String, String>> entityStatusTransitionConfig = appConfig.getStatusTransitionConfig()
				.get(transition.getEntityName());
		if (entityStatusTransitionConfig != null) {
			Map<String, String> statusWiseTransitions = entityStatusTransitionConfig.get(transition.getFromStatus());
			if (statusWiseTransitions != null) {
				toStatus = statusWiseTransitions.get(transition.getAction());
			}
		}
		if (toStatus == null) {
			throw new BusinessException(ErrorCodes.STATUS_TRANSITION_NOT_FOUND,
					transition.getEntityName() + "#" + transition.getEntityId(), transition.getFromStatus(),
					transition.getAction());
		}
		return toStatus;
	}

	public void validateStateTransitionAccess(StatusTransitionDTO transition, String authorId, String editorId,
			String publisherId, String actionBy) {
		Map<String, Map<String, List<String>>> actionAccessConfig = appConfig.getActionAccessConfig()
				.get(transition.getEntityName());
		Map<String, List<String>> statusWiseActionAccessConfig = actionAccessConfig.get(transition.getFromStatus());
		List<String> accessRoles = statusWiseActionAccessConfig.get(transition.getAction());

		if(accessRoles == null) {
			return;
		}
		
		for (String role : accessRoles) {
			switch (role) {
			case "Author":
				if (!actionBy.equals(authorId)) {
					throw new BusinessException(ErrorCodes.ACTION_DISALLOWED_FOR_THIS_USER);
				}
				return;
			case "AnyPublisher":
				if (userRoleService.getByUserIdAndRole(actionBy, CommonConstants.PUBLISHER_ROLE) == null) {
					throw new BusinessException(ErrorCodes.ACTION_DISALLOWED_FOR_THIS_USER);
				}
				return;
			case "Editor":
				if (!actionBy.equals(editorId)) {
					throw new BusinessException(ErrorCodes.ACTION_DISALLOWED_FOR_THIS_USER);
				}
				return;
			case "AnyEditor":
				if (userRoleService.getByUserIdAndRole(actionBy, CommonConstants.EDITOR_ROLE) == null) {
					throw new BusinessException(ErrorCodes.ACTION_DISALLOWED_FOR_THIS_USER);
				}
				return;
			default:
				log.severe("Invalid role '" + role + "' configured in state transition access config.");
			}
		}

	}

}
