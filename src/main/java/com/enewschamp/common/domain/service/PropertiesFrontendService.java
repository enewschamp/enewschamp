package com.enewschamp.common.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.common.app.dto.PropertiesFrontendDTO;
import com.enewschamp.common.domain.entity.PropertiesBackend;
import com.enewschamp.common.domain.entity.PropertiesFrontend;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;

@Service
public class PropertiesFrontendService extends AbstractDomainService {

	@Autowired
	PropertiesFrontendRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	public PropertiesFrontendService(PropertiesFrontendRepository repository) {
		this.repository = repository;
	}

	public String getValue(String appName, String name) {
		// PropertiesFrontend property = repository.getProperty(appName, name);
		// return property == null ? null : property.getValue();
		String arr[] = name.split("\\.");
		String value = "";
		// System.out.println(">>>>>>>>>>>property name>>>>>>>>>>>>>>" +
		// name.split("\\.")[0]);
		PropertiesFrontend property = repository.getProperty(appName, name.split("\\.")[0]);
		if (arr.length > 1) {
			JSONParser parser = new JSONParser();
			try {
				// System.out.println(">>>>>>>>>property_value>>>>>>>>>>>>>>>>" +
				// property.getValue());
				JSONObject json = (JSONObject) parser.parse(property.getValue());
				if (json.get(arr[1]) != null) {
					value = json.get(arr[1]).toString();
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			value = property.getValue();
		}
		return value;
	}

	public PropertiesFrontend create(PropertiesFrontend properties) {
		return repository.save(properties);
	}

	public PropertiesFrontend update(PropertiesFrontend properties) {
		Long propertyId = properties.getPropertyId();
		PropertiesFrontend existingProperties = get(propertyId);
		modelMapper.map(properties, existingProperties);
		return repository.save(existingProperties);
	}

	public PropertiesFrontend patch(PropertiesFrontend properties) {
		Long propertyId = properties.getPropertyId();
		PropertiesFrontend existingEntity = get(propertyId);
		modelMapperForPatch.map(properties, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long propertyId) {
		repository.deleteById(propertyId);
	}

	public PropertiesFrontend get(Long propertyId) {
		Optional<PropertiesFrontend> existingEntity = repository.findById(propertyId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.PROPERTY_NOT_FOUND, String.valueOf(propertyId));
		}
	}

	public List<ListOfValuesItem> getLOV() {
		return toListOfValuesItems(repository.getPropertiesLOV());
	}

	public String getProperty(String appName, String property) {
		PropertiesFrontend properties = repository.getProperty(appName, property);
		return ((properties == null) ? "" : properties.getValue());
	}

	public List<PropertiesFrontendDTO> getPropertyList(String appName) {
		List<PropertiesFrontend> propertiesList = repository.getPropertiesFrontend(appName);
		List<PropertiesFrontendDTO> propertiesDTOList = new ArrayList<PropertiesFrontendDTO>();
		for (PropertiesFrontend properties : propertiesList) {
			PropertiesFrontendDTO propertiesDTO = new PropertiesFrontendDTO();
			propertiesDTO.setName(properties.getName());
			propertiesDTO.setValue(properties.getValue());
			propertiesDTOList.add(propertiesDTO);
		}
		return propertiesDTOList;
	}

	public String getAudit(Long propertyId) {
		PropertiesFrontend properties = new PropertiesFrontend();
		properties.setPropertyId(propertyId);
		return auditService.getEntityAudit(properties);
	}

}
