package com.enewschamp.app.admin.page.navigator.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.page.navigator.repository.PageNavigatorRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.fw.page.navigation.entity.PageNavigator;
import com.enewschamp.app.fw.page.navigation.repository.PageNavigatorRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class PageNavigatorService {
	@Autowired
	private PageNavigatorRepository repository;
	@Autowired
	private PageNavigatorRepositoryCustomImpl repositoryCustom;
	@Autowired
	private ModelMapper modelMapper;

	public PageNavigator create(PageNavigator pageNavigator) {
		PageNavigator pageNavigatorEntity = null;
		try {
			pageNavigatorEntity = repository.save(pageNavigator);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return pageNavigatorEntity;
	}

	public PageNavigator update(PageNavigator pageNavigator) {
		Long pageNavigatorId = pageNavigator.getNavId();
		PageNavigator existingPageNavigator = get(pageNavigatorId);
		if (existingPageNavigator.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(pageNavigator, existingPageNavigator);
		return repository.save(existingPageNavigator);
	}

	public PageNavigator get(Long pageNavigatorId) {
		Optional<PageNavigator> existingEntity = repository.findById(pageNavigatorId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.PAGE_NAVIGATOR_NOT_FOUND, String.valueOf(pageNavigatorId));
		}
	}

	public PageNavigator read(PageNavigator pageNavigator) {
		Long pageNavigatorId = pageNavigator.getNavId();
		PageNavigator existingPageNavigator = get(pageNavigatorId);
		return existingPageNavigator;
	}

	public PageNavigator close(PageNavigator pageNavigator) {
		Long pageNavigatorId = pageNavigator.getNavId();
		PageNavigator existingPageNavigator = get(pageNavigatorId);
		if (existingPageNavigator.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingPageNavigator.setRecordInUse(RecordInUseType.N);
		existingPageNavigator.setOperationDateTime(null);
		return repository.save(existingPageNavigator);
	}

	public PageNavigator reInstate(PageNavigator pageNavigator) {
		Long pageNavigatorId = pageNavigator.getNavId();
		PageNavigator existingPageNavigator = get(pageNavigatorId);
		if (existingPageNavigator.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingPageNavigator.setRecordInUse(RecordInUseType.Y);
		existingPageNavigator.setOperationDateTime(null);
		return repository.save(existingPageNavigator);
	}

	public Page<PageNavigator> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<PageNavigator> pageList = repositoryCustom.findAll(pageable, searchRequest);
		if (pageList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return pageList;
	}
	
	public int createAll(List<PageNavigator> pageNavigators) {
		int noOfRecords = 0;
		try {
			noOfRecords = repository.saveAll(pageNavigators).size();
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return noOfRecords;
	}

}
