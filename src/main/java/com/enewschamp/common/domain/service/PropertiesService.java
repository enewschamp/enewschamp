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
import com.enewschamp.common.app.dto.PropertiesDTO;
import com.enewschamp.common.domain.entity.Properties;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;

@Service
public class PropertiesService extends AbstractDomainService {

	@Autowired
	PropertiesRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	public PropertiesService(PropertiesRepository repository) {
		this.repository = repository;
	}

	public String getValue(String propertyName) {
		Properties property = repository.getProperty(propertyName);
		return property == null ? null : property.getPropertyValue();
	}

	public Properties create(Properties properties) {
		return repository.save(properties);
	}

	public Properties update(Properties properties) {
		Long propertyId = properties.getPropertyId();
		Properties existingProperties = get(propertyId);
		modelMapper.map(properties, existingProperties);
		return repository.save(existingProperties);
	}

	public Properties patch(Properties properties) {
		Long propertyId = properties.getPropertyId();
		Properties existingEntity = get(propertyId);
		modelMapperForPatch.map(properties, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long propertyId) {
		repository.deleteById(propertyId);
	}

	public Properties get(Long propertyId) {
		Optional<Properties> existingEntity = repository.findById(propertyId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.PROPERTY_NOT_FOUND, String.valueOf(propertyId));
		}
	}

	public List<ListOfValuesItem> getLOV() {
		return toListOfValuesItems(repository.getPropertiesLOV());
	}

	public String getProperty(String property) {
		Properties properties = repository.getProperty(property);
		return ((properties == null) ? "" : properties.getPropertyValue());
	}

	public List<PropertiesDTO> getPropertyList() {
		List<Properties> propertiesList = repository.getProperties();
		List<PropertiesDTO> propertiesDTOList = new ArrayList<PropertiesDTO>();
		for (Properties properties : propertiesList) {
			PropertiesDTO propertiesDTO = new PropertiesDTO();
			propertiesDTO.setPropertyName(properties.getPropertyName());
			propertiesDTO.setPropertyValue(properties.getPropertyValue());
			propertiesDTOList.add(propertiesDTO);
		}
		return propertiesDTOList;
	}

	public String getAudit(Long propertyId) {
		Properties properties = new Properties();
		properties.setPropertyId(propertyId);
		return auditService.getEntityAudit(properties);
	}

}
