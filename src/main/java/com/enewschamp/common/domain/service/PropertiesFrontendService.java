package com.enewschamp.common.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.properties.frontend.repository.PropertiesFrontendRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.common.app.dto.PropertiesFrontendDTO;
import com.enewschamp.common.domain.entity.PropertiesFrontend;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;

@Service
public class PropertiesFrontendService extends AbstractDomainService {

	@Autowired
	PropertiesFrontendRepository repository;

	@Autowired
	PropertiesFrontendRepositoryCustomImpl customRepository;

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
		PropertiesFrontend property = repository.getProperty(appName, name);
		return property == null ? null : property.getValue();
	}

	public PropertiesFrontend create(PropertiesFrontend properties) {
		PropertiesFrontend propertiesEntity = null;
		try {
			propertiesEntity = repository.save(properties);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return propertiesEntity;
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
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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

	public PropertiesFrontend read(PropertiesFrontend propertiesFrontendEntity) {
		Long propertiesId = propertiesFrontendEntity.getPropertyId();
		PropertiesFrontend existingEntity = get(propertiesId);
		return existingEntity;

	}

	public PropertiesFrontend close(PropertiesFrontend propertiesFrontendEntity) {
		Long propertiesId = propertiesFrontendEntity.getPropertyId();
		PropertiesFrontend propertiesFrontend = get(propertiesId);
		if (propertiesFrontend.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		propertiesFrontend.setRecordInUse(RecordInUseType.N);
		propertiesFrontend.setOperationDateTime(null);
		return repository.save(propertiesFrontend);
	}

	public PropertiesFrontend reinstate(PropertiesFrontend propertiesFrontendEntity) {
		Long propertiesId = propertiesFrontendEntity.getPropertyId();
		PropertiesFrontend propertiesFrontend = get(propertiesId);
		if (propertiesFrontend.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		propertiesFrontend.setRecordInUse(RecordInUseType.Y);
		propertiesFrontend.setOperationDateTime(null);
		return repository.save(propertiesFrontend);
	}

	public Page<PropertiesFrontend> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<PropertiesFrontend> propertiesFrontendList = customRepository.findAll(pageable, searchRequest);
		if (propertiesFrontendList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return propertiesFrontendList;
	}

}
