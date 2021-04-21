package com.enewschamp.publication.domain.service;

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

import com.enewschamp.app.admin.avatar.repository.AvatarRepositoryCustomImpl;
import com.enewschamp.app.admin.dashboard.handler.AvatarView;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Avatar;
import com.enewschamp.subscription.app.dto.AvatarPageData;

@Service
public class AvatarService extends AbstractDomainService {

	@Autowired
	AvatarRepository repository;

	@Autowired
	AvatarRepositoryCustomImpl repositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public Avatar create(Avatar avatarEntity) {
		Avatar avatar = null;
		try {
			avatar = repository.save(avatarEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return avatar;
	}

	public Avatar update(Avatar avatar) {
		Long avatarId = avatar.getAvatarId();
		Avatar existingAvatar = get(avatarId);
		if (existingAvatar.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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
		Avatar avatarEntity = get(avatarId);
		return avatarEntity;
	}

	public Avatar close(Avatar avatarEntity) {
		Long avatarId = avatarEntity.getAvatarId();
		Avatar existingAvatar = get(avatarId);
		if (existingAvatar.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingAvatar.setRecordInUse(RecordInUseType.N);
		existingAvatar.setOperationDateTime(null);
		return repository.save(existingAvatar);
	}

	public Avatar reinstate(Avatar appSecurityEntity) {
		Long avatarId = appSecurityEntity.getAvatarId();
		Avatar existingAvatar = get(avatarId);
		if (existingAvatar.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingAvatar.setRecordInUse(RecordInUseType.Y);
		existingAvatar.setOperationDateTime(null);
		return repository.save(existingAvatar);
	}

	public Page<Avatar> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Avatar> avatarList = repositoryCustom.findAll(pageable, null);
		if (avatarList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return avatarList;
	}

	public List<AvatarView> getAllAvatarView() {
		return repository.findAllProjectedBy();
	}

}