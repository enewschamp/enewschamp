package com.enewschamp.app.admin.audit;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampEntityConfig;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("AdminAuditPageHandler")
@Slf4j
public class AdminAuditPageHandler implements IPageHandler {

	@Autowired
	private AuditService auditService;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private EnewschampEntityConfig entityConfig;

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

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Audit":
			pageDto = auditEntity(pageRequest);
		default:
			break;
		}
		return pageDto;
	}

	@SneakyThrows
	private PageDTO auditEntity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AdminAuditPageData pageData = new AdminAuditPageData();
		String classname = pageRequest.getData().get("audit").get("entityName").asText();
		String fieldName = pageRequest.getData().get("audit").get("fieldName").asText();
		Long fieldValue = pageRequest.getData().get("audit").get("fieldValue").asLong();
		String packageName = entityConfig.getEntityConfig().get(classname.toLowerCase());
		if (packageName == null) {
			log.error(ErrorCodeConstants.ENTITY_PACKAGE_NOT_FOUND + classname);
			throw new BusinessException(ErrorCodeConstants.ENTITY_PACKAGE_NOT_FOUND, classname);
		}
		try {
			Class<?> cls = Class.forName(packageName + "." + classname);
			Object clsInstance = (Object) cls.newInstance();
			Field field = clsInstance.getClass().getDeclaredField(fieldName);
			ReflectionUtils.setField(field, clsInstance, fieldValue);
			mapHeaderData(pageRequest, pageDto);
			String records = auditService.getEntityAudit(clsInstance);
			pageData.setRecords(mapper.readValue(records, JsonNode.class));
		} catch (ClassNotFoundException e) {
			log.error(ErrorCodeConstants.ENTITY_CLASS_NOT_FOUND + classname + " " + e.getMessage());
			throw new BusinessException(ErrorCodeConstants.ENTITY_CLASS_NOT_FOUND, classname);
		} catch (NoSuchFieldException e) {
			log.error(ErrorCodeConstants.ENTITY_FIELD_NOT_FOUND + packageName + " " + e.getMessage());
			throw new BusinessException(ErrorCodeConstants.ENTITY_FIELD_NOT_FOUND, packageName);
		}
		pageDto.setData(pageData);
		return pageDto;
	}

}
