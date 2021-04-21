package com.enewschamp.app.common.state.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.state.repository.StateRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.state.entity.State;
import com.enewschamp.app.common.state.repository.StateRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StatePageData;

@Service
public class StateService extends AbstractDomainService {

	@Autowired
	StateRepository stateRepository;

	@Autowired
	private StateRepositoryCustomImpl customRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public State create(State stateEntity) {
		State state = null;
		try {
			state = stateRepository.save(stateEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return state;
	}

	public State update(State stateEntity) {
		Long StateId = stateEntity.getStateId();
		State existingState = get(StateId);
		if (existingState.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(stateEntity, existingState);
		return stateRepository.save(existingState);
	}

	public State patch(State State) {
		Long navId = State.getStateId();
		State existingEntity = get(navId);
		modelMapperForPatch.map(State, existingEntity);
		return stateRepository.save(existingEntity);
	}

	public void delete(Long StateId) {
		stateRepository.deleteById(StateId);
	}

	public State get(Long StateId) {
		Optional<State> existingEntity = stateRepository.findById(StateId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STATE_NOT_FOUND);
		}
	}

	public List<State> getStateForCountry(String countryId) {
		return stateRepository.getStateForCountry(countryId);
	}

	public List<StatePageData> getStatesForCountry(String country) {
		List<State> existingEntity = stateRepository.getStateForCountry(country);
		List<StatePageData> statePageDataList = new ArrayList<StatePageData>();
		for (State state : existingEntity) {
			StatePageData statePageData = new StatePageData();
			statePageData.setId(state.getNameId());
			statePageData.setName(state.getDescription());
			statePageDataList.add(statePageData);
		}
		return statePageDataList;
	}

	public StatePageData getState(String stateId) {
		Optional<State> stateentity = stateRepository.getState(stateId);
		StatePageData statePageData = new StatePageData();
		if (stateentity.isPresent()) {
			State state = stateentity.get();
			statePageData.setId(state.getNameId());
			statePageData.setName(state.getDescription());
		}
		return statePageData;
	}

	public State getByNameAndCountryId(String nameId, String countryId) {
		Optional<State> state = stateRepository.findByNameIdAndCountryId(nameId, countryId);
		if (state.isPresent())
			return state.get();
		else
			return null;
	}

	public State read(State stateEntity) {
		Long StateId = stateEntity.getStateId();
		State state = get(StateId);
		return state;
	}

	public State close(State stateEntity) {
		Long StateId = stateEntity.getStateId();
		State existingState = get(StateId);
		if (existingState.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingState.setRecordInUse(RecordInUseType.N);
		existingState.setOperationDateTime(null);
		return stateRepository.save(existingState);
	}

	public State reInstate(State stateEntity) {
		Long StateId = stateEntity.getStateId();
		State existingState = get(StateId);
		if (existingState.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingState.setRecordInUse(RecordInUseType.Y);
		existingState.setOperationDateTime(null);
		return stateRepository.save(existingState);
	}

	public Page<State> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<State> stateList = customRepository.findAll(pageable, searchRequest);
		return stateList;
	}

}