package com.enewschamp.common.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.common.app.dto.PropertiesBackendDTO;
import com.enewschamp.common.domain.entity.PropertiesBackend;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;

@Service
public class PropertiesBackendService extends AbstractDomainService {

	@Autowired
	PropertiesBackendRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	public PropertiesBackendService(PropertiesBackendRepository repository) {
		this.repository = repository;
	}

	public String getValue(String appName, String name) {
		PropertiesBackend property = repository.getProperty(appName, name);
		return property == null ? null : property.getValue();
	}

	public PropertiesBackend create(PropertiesBackend properties) {
		return repository.save(properties);
	}

	public PropertiesBackend update(PropertiesBackend properties) {
		Long propertyId = properties.getPropertyId();
		PropertiesBackend existingProperties = get(propertyId);
		modelMapper.map(properties, existingProperties);
		return repository.save(existingProperties);
	}

	public PropertiesBackend patch(PropertiesBackend properties) {
		Long propertyId = properties.getPropertyId();
		PropertiesBackend existingEntity = get(propertyId);
		modelMapperForPatch.map(properties, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long propertyId) {
		repository.deleteById(propertyId);
	}

	public PropertiesBackend get(Long propertyId) {
		Optional<PropertiesBackend> existingEntity = repository.findById(propertyId);
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
		PropertiesBackend properties = repository.getProperty(appName, property);
		return ((properties == null) ? "" : properties.getValue());
	}

	public List<PropertiesBackendDTO> getPropertyList(String appName) {
		List<PropertiesBackend> propertiesList = repository.getPropertiesBackend(appName);
		List<PropertiesBackendDTO> propertiesDTOList = new ArrayList<PropertiesBackendDTO>();
		for (PropertiesBackend properties : propertiesList) {
			PropertiesBackendDTO propertiesDTO = new PropertiesBackendDTO();
			propertiesDTO.setName(properties.getName());
			propertiesDTO.setValue(properties.getValue());
			propertiesDTOList.add(propertiesDTO);
		}
		return propertiesDTOList;
	}

	public String getAudit(Long propertyId) {
		PropertiesBackend properties = new PropertiesBackend();
		properties.setPropertyId(propertyId);
		return auditService.getEntityAudit(properties);
	}

}
