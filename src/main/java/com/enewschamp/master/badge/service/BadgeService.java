package com.enewschamp.master.badge.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.state.entity.State;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.master.badge.repository.BadgeRepository;
import com.enewschamp.master.badge.repository.BadgeRepositoryCustom;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Badge;

@Service
public class BadgeService {

	@Autowired
	BadgeRepository badgeRepository;
	
	@Autowired
	BadgeRepositoryCustom badgeRepositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public Badge create(Badge badgeEntity) {
		return badgeRepository.save(badgeEntity);
	}

	public Badge update(Badge badgeEntity) {
		Long badgeId = badgeEntity.getBadgeId();
		Badge existingBadge = get(badgeId);
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
	
	public Badge read(Badge badgeEntity) {
		Long badgeId = badgeEntity.getBadgeId();
		Badge existingBadge = get(badgeId);
		existingBadge.setRecordInUse(RecordInUseType.Y);
		return badgeRepository.save(existingBadge);
	}

	public Badge close(Badge badgeEntity) {
		Long badgeId = badgeEntity.getBadgeId();
		Badge existingBadge = get(badgeId);
		existingBadge.setRecordInUse(RecordInUseType.N);
		return badgeRepository.save(existingBadge);
	}

	public Page<Badge> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Badge> badgeList = badgeRepositoryCustom.findBadges(pageable);
		return badgeList;
	}
}
