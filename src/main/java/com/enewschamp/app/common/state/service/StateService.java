package com.enewschamp.app.common.state.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.state.entity.State;
import com.enewschamp.app.common.state.repository.StateRepository;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StatePageData;

@Service
public class StateService {

	@Autowired
	StateRepository stateRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public State create(State stateEntity) {
		return stateRepository.save(stateEntity);
	}

	public State update(State stateEntity) {
		Long StateId = stateEntity.getStateId();
		State existingState = get(StateId);
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
			throw new BusinessException(ErrorCodes.STATE_NOT_FOUND);
		}
	}
	
	public List<State> getStateForCountry(String countryId)
	{
		return stateRepository.getStateForCountry(countryId);
	}
	public StatePageData getState(String stateId)
	{
		Optional<State> stateentity= stateRepository.getState(stateId);
		StatePageData statePageData = new StatePageData();
		if(stateentity.isPresent())
		{
			State state = stateentity.get();
			
			statePageData.setId(state.getNameId());
			statePageData.setName(state.getDescription());
			
		}
		
		return statePageData;
	}
	
	
}
