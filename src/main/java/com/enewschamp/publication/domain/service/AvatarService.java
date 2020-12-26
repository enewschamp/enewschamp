package com.enewschamp.publication.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.country.entity.Country;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Avatar;
import com.enewschamp.security.entity.AppSecurity;
import com.enewschamp.subscription.app.dto.AvatarPageData;
import com.enewschamp.subscription.app.dto.CountryPageData;

@Service
public class AvatarService extends AbstractDomainService {

	@Autowired
	AvatarRepository repository;

	@Autowired
	AvatarRepositoryCustom repositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public Avatar create(Avatar avatar) {
		return repository.save(avatar);
	}

	public Avatar update(Avatar avatar) {
		Long avatarId = avatar.getAvatarId();
		Avatar existingAvatar = get(avatarId);
		modelMapper.map(avatar, existingAvatar);
		return repository.save(existingAvatar);
	}

	public Avatar patch(Avatar avatar) {
		Long avatarId = avatar.getAvatarId();
		Avatar existingEntity = get(avatarId);
		modelMapperForPatch.map(avatar, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long avatarId) {
		repository.deleteById(avatarId);
	}

	public Avatar get(Long avatarId) {
		Optional<Avatar> existingEntity = repository.findById(avatarId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.AVATAR_NOT_FOUND, String.valueOf(avatarId));
		}
	}

	public List<ListOfValuesItem> getLOV() {
		return toListOfValuesItems(repository.getAvatarLOV());
	}

	public List<AvatarPageData> getAvatarLOV() {
		List<Avatar> existingEntity = repository.getAvatarList();
		List<AvatarPageData> avatarPageDataList = new ArrayList<AvatarPageData>();
		for (Avatar avatar : existingEntity) {
			AvatarPageData avatarData = new AvatarPageData();
			avatarData.setId(avatar.getImageName());
			avatarData.setName(avatar.getNameId());
			avatarData.setGender(avatar.getGender());
			avatarData.setReadingLevel(avatar.getReadingLevel());
			avatarPageDataList.add(avatarData);
		}
		return avatarPageDataList;
	}

	public String getAudit(Long avatarId) {
		Avatar avatar = new Avatar();
		avatar.setAvatarId(avatarId);
		return auditService.getEntityAudit(avatar);
	}

	public Avatar read(Avatar avatar) {
		Long avatarId = avatar.getAvatarId();
		Avatar existingAvatar = get(avatarId);
		existingAvatar.setRecordInUse(RecordInUseType.Y);
		return repository.save(existingAvatar);
	}

	public Avatar close(Avatar avatarEntity) {
		Long avatarId = avatarEntity.getAvatarId();
		Avatar existingAvatar = get(avatarId);
		existingAvatar.setRecordInUse(RecordInUseType.N);
		return repository.save(existingAvatar);
	}

	public Page<Avatar> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Avatar> avatarList = repositoryCustom.findAvatars(pageable);
		return avatarList;
	}

}