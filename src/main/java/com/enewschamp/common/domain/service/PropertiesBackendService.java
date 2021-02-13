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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.properties.backend.repository.PropertiesBackendRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.common.app.dto.PropertiesBackendDTO;
import com.enewschamp.common.domain.entity.PropertiesBackend;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;

@Service
public class PropertiesBackendService extends AbstractDomainService {

	@Autowired
	PropertiesBackendRepository repository;

	@Autowired
	PropertiesBackendRepositoryCustomImpl customRepository;

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
		PropertiesBackend propertiesEntity = null;
		try {
			propertiesEntity = repository.save(properties);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return propertiesEntity;
	}

	public PropertiesBackend update(PropertiesBackend properties) {
		Long propertyId = properties.getPropertyId();
		PropertiesBackend existingProperties = get(propertyId);
		if (existingProperties.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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

	public PropertiesBackend read(PropertiesBackend propertiesBackendEntity) {
		Long propertiesId = propertiesBackendEntity.getPropertyId();
		PropertiesBackend existingEntity = get(propertiesId);
		return existingEntity;

	}

	public PropertiesBackend close(PropertiesBackend propertiesBackendEntity) {
		Long propertiesId = propertiesBackendEntity.getPropertyId();
		PropertiesBackend propertiesBackend = get(propertiesId);
		if (propertiesBackend.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		propertiesBackend.setRecordInUse(RecordInUseType.N);
		propertiesBackend.setOperationDateTime(null);
		return repository.save(propertiesBackend);
	}

	public PropertiesBackend reinstate(PropertiesBackend propertiesBackendEntity) {
		Long propertiesId = propertiesBackendEntity.getPropertyId();
		PropertiesBackend propertiesBackend = get(propertiesId);
		if (propertiesBackend.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		propertiesBackend.setRecordInUse(RecordInUseType.Y);
		propertiesBackend.setOperationDateTime(null);
		return repository.save(propertiesBackend);
	}

	public Page<PropertiesBackend> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<PropertiesBackend> propertiesBackendList = customRepository.findAll(pageable, searchRequest);
		if (propertiesBackendList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return propertiesBackendList;
	}

	public int createAll(List<PropertiesBackend> properties) {
		int noOfRecords = 0;
		try {
			noOfRecords = repository.saveAll(properties).size();
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return noOfRecords;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void clean() {
		repository.truncate();
		repository.deleteSequences();
		repository.initializeSequence();
	}

}
