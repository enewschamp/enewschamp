package com.enewschamp.master.badge.service;

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

import com.enewschamp.app.admin.repository.BadgeRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.master.badge.repository.BadgeRepository;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.common.BadgeList;
import com.enewschamp.publication.domain.entity.Badge;

@Service
public class BadgeService {

	@Autowired
	BadgeRepository badgeRepository;

	@Autowired
	BadgeRepositoryCustomImpl badgeRepositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public Badge create(Badge badgeEntity) {
		Badge badge = null;
		try {
			badge = badgeRepository.save(badgeEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return badge;
	}

	public Badge update(Badge badgeEntity) {
		Long badgeId = badgeEntity.getBadgeId();
		Badge existingBadge = get(badgeId);
		if(existingBadge.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(badgeEntity, existingBadge);
		return badgeRepository.save(existingBadge);
	}

	public Badge patch(Badge badgeEntity) {
		Long badgeId = badgeEntity.getBadgeId();
		Badge existingEntity = get(badgeId);
		modelMapperForPatch.map(badgeEntity, existingEntity);
		return badgeRepository.save(existingEntity);
	}

	public void delete(Long BadgeId) {
		badgeRepository.deleteById(BadgeId);
	}

	public Badge get(Long badgeId) {
		Optional<Badge> existingEntity = badgeRepository.findById(badgeId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUD_BADGES_NOT_FOUND);
		}
	}

	public Badge getBadgeForEdition(String editionId) {
		Optional<Badge> existingEntity = badgeRepository.getBadgeForEdition(editionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.BADGE_NOT_FOUND);
		}
	}

	public Badge getBadgeForGenreAndEdition(String editionId, String genreId) {
		Optional<Badge> existingEntity = badgeRepository.getBadgeForGenreAndEdition(editionId, genreId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.BADGE_NOT_FOUND);
		}
	}

	public Badge getBadgeDetails(String badgeName, int readingLevel, String editionId, String genreId) {
		Optional<Badge> existingEntity = badgeRepository.getBadgeDetails(badgeName, genreId, readingLevel, editionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.BADGE_NOT_FOUND);
		}
	}

	public Badge getBadgeForStudent(String editionId, int readingLevel, String genreId, Long studPoints) {
		List<Badge> badgeList = badgeRepository.getBadgeForStudent(editionId, readingLevel, genreId, studPoints);
		Badge badge = null;
		if (!badgeList.isEmpty()) {
			badge = badgeList.get(0);
		}
		return badge;
	}

	public Badge getNextBadge(String editionId, Long studPoints) {
		List<Badge> badgeList = badgeRepository.getNextBadge(editionId, studPoints);
		Badge badge = null;
		if (!badgeList.isEmpty()) {
			badge = badgeList.get(0);
		}
		return badge;
	}

	public Badge getNextBadgeForGenre(String editionId, String genreId, int readingLevel, Long studPoints) {
		List<Badge> badgeList = badgeRepository.getNextBadgeForGenre(editionId, genreId, readingLevel, studPoints);
		Badge badge = null;
		if (!badgeList.isEmpty()) {
			badge = badgeList.get(0);
		}
		return badge;
	}

	public String getAudit(Long badgeId) {
		Badge badge = new Badge();
		badge.setBadgeId(badgeId);
		return auditService.getEntityAudit(badge);
	}

	public Badge read(Badge badge) {
		Long badgeId = badge.getBadgeId();
		Badge badgeEntity = get(badgeId);
		return badgeEntity;

	}

	public Badge close(Badge badgeEntity) {
		Long badgeId = badgeEntity.getBadgeId();
		Badge existingBadge = get(badgeId);
		if (existingBadge.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingBadge.setRecordInUse(RecordInUseType.N);
		existingBadge.setOperationDateTime(null);
		return badgeRepository.save(existingBadge);
	}

	public Badge reInstate(Badge badgeEntity) {
		Long badgeId = badgeEntity.getBadgeId();
		Badge existingCountry = get(badgeId);
		if (existingCountry.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingCountry.setRecordInUse(RecordInUseType.Y);
		existingCountry.setOperationDateTime(null);
		return badgeRepository.save(existingCountry);
	}

	public List<BadgeList> getBadgeList() {
		return badgeRepository.getBadgeList();
	}

	public Page<Badge> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Badge> badgeList = badgeRepositoryCustom.findAll(pageable, null);
		if(badgeList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return badgeList;
	}
}
