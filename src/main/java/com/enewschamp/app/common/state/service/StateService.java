package com.enewschamp.app.common.state.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.state.entity.State;
import com.enewschamp.app.common.state.repository.StateRepository;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;

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
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.STATE_NOT_FOUND,
					"State not found!");
		}
	}
	
	public List<State> getStateForCountry(String countryId)
	{
		return stateRepository.getStateForCountry(countryId);
	}
}
