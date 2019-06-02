package com.enewschamp.app.fw.page.navigation.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.entity.PageNavigator;
import com.enewschamp.app.fw.page.navigation.repository.PageNavigatorRepository;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.google.common.reflect.TypeToken;

@Service
public class PageNavigationService {

	@Autowired
	PageNavigatorRepository pageNavigatorRepository;

	

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public PageNavigator create(PageNavigator pageNavigatorEntity) {
		return pageNavigatorRepository.save(pageNavigatorEntity);
	}

	public PageNavigator update(PageNavigator pageNavigator) {
		Long navId = pageNavigator.getNavId();
		PageNavigator existingPageNavigator = get(navId);
		modelMapper.map(pageNavigator, existingPageNavigator);
		return pageNavigatorRepository.save(existingPageNavigator);
	}

	public PageNavigator patch(PageNavigator PageNavigator) {
		Long navId = PageNavigator.getNavId();
		PageNavigator existingEntity = get(navId);
		modelMapperForPatch.map(PageNavigator, existingEntity);
		return pageNavigatorRepository.save(existingEntity);
	}

	public void delete(Long PageNavigatorId) {
		pageNavigatorRepository.deleteById(PageNavigatorId);
	}

	public PageNavigator get(Long navId) {
		Optional<PageNavigator> existingEntity = pageNavigatorRepository.findById(navId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.PAGE_NOT_FOUND);
		}
	}

	public List<PageNavigatorDTO> getNavList(String action, String operation, String currentPage) {
		List<PageNavigator> existingEntity = pageNavigatorRepository.getNavList( operation, currentPage);
		java.lang.reflect.Type listType = new TypeToken<List<PageNavigatorDTO>>() {
		}.getType();
		List<PageNavigatorDTO> pageNavList = modelMapper.map(existingEntity, listType);
		if (pageNavList.size() == 0) {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.PAGE_NOT_FOUND);

		}
		return pageNavList;
	}

	public PageNavigatorDTO getNavPage(String action, String operation, String currentPage) {
		PageNavigator existingEntity = pageNavigatorRepository.getNavPage(action, operation, currentPage);
		if (existingEntity == null) {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.PAGE_NOT_FOUND);

		}
		PageNavigatorDTO pageNav = modelMapper.map(existingEntity, PageNavigatorDTO.class);
		
		return pageNav;
	}

	public String getAudit(Long navId) {
		PageNavigator PageNavigator = new PageNavigator();
		PageNavigator.setNavId(navId);
		return auditService.getEntityAudit(PageNavigator);
	}
}
