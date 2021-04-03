package com.enewschamp.publication.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.publication.app.dto.HashTagDTO;
import com.enewschamp.publication.domain.entity.HashTag;
import com.google.common.reflect.TypeToken;

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
		if (hashTag.getRecordInUse() == null) {
			hashTag.setRecordInUse(RecordInUseType.Y);
		}
		return repository.save(hashTag);
	}

	public HashTag update(HashTag hashTag) {
		String hashTagId = hashTag.getHashTag();
		HashTag existingHashTag = get(hashTagId);
		modelMapper.map(hashTag, existingHashTag);
		if (existingHashTag.getRecordInUse() == null) {
			existingHashTag.setRecordInUse(RecordInUseType.Y);
		}
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
			return null;
		}
	}

	public String getAudit(String hashTagId) {
		HashTag hashTag = new HashTag();
		hashTag.setHashTag(hashTagId);
		return auditService.getEntityAudit(hashTag);
	}

	public List<HashTagDTO> getHashTagByName(String hashTag) {
		List<HashTag> hashtagList = repository.getHashTagByName(hashTag);
		java.lang.reflect.Type listType = new TypeToken<List<HashTagDTO>>() {
		}.getType();
		List<HashTagDTO> hashtagDTOList = modelMapper.map(hashtagList, listType);
		return hashtagDTOList;
	}
}