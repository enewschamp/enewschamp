package com.enewschamp.publication.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.publication.domain.entity.HashTag;

@Service
public class HashTagService {

	@Autowired
	HashTagRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	public HashTag create(HashTag hashTag) {
		return repository.save(hashTag);
	}
	
	public HashTag update(HashTag hashTag) {
		String hashTagId = hashTag.getHashTag();
		HashTag existingHashTag = get(hashTagId);
		modelMapper.map(hashTag, existingHashTag);
		return repository.save(existingHashTag);
	}
	
	public HashTag patch(HashTag hashTag) {
		String hashTagId = hashTag.getHashTag();
		HashTag existingEntity = get(hashTagId);
		modelMapperForPatch.map(hashTag, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(String hashTag) {
		repository.deleteById(hashTag);
	}
	
	public HashTag get(String hashTag) {
		Optional<HashTag> existingEntity = repository.findById(hashTag);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.HASH_TAG_NOT_FOUND, "HashTag not found!");
		}
	}
	
	public String getAudit(String hashTagId) {
		HashTag hashTag = new HashTag();
		hashTag.setHashTag(hashTagId);
		return auditService.getEntityAudit(hashTag);
	}
	
}
