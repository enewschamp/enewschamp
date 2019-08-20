package com.enewschamp.master.badge.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.master.badge.entity.Badge;
import com.enewschamp.master.badge.repository.BadgeRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class BadgeService {

	@Autowired
	BadgeRepository badgeRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public Badge create(Badge badgeEntity) {
		return badgeRepository.save(badgeEntity);
	}

	public Badge update(Badge badgeEntity) {
		String trendsDailyId = badgeEntity.getBadgeId();
		Badge existingBadge = get(trendsDailyId);
		modelMapper.map(badgeEntity, existingBadge);
		return badgeRepository.save(existingBadge);
	}

	public Badge patch(Badge trendsDaily) {
		String navId = trendsDaily.getBadgeId();
		Badge existingEntity = get(navId);
		modelMapperForPatch.map(trendsDaily, existingEntity);
		return badgeRepository.save(existingEntity);
	}

	public void delete(String BadgeId) {
		badgeRepository.deleteById(BadgeId);
	}

	public Badge get(String badgeId) {
		Optional<Badge> existingEntity = badgeRepository.findById(badgeId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException( ErrorCodes.STUD_BADGES_NOT_FOUND,
					"Badge not found!");
		}
	}
	public Badge getBadgeForEdition( String editionId) {
		Optional<Badge> existingEntity = badgeRepository.getBadgeForEdition(editionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException( ErrorCodes.BADGE_NOT_FOUND,
					"Badge not found!");
		}
	}
	public Badge getBadgeForGenreAndEdition( String editionId, String genreId) {
		Optional<Badge> existingEntity = badgeRepository.getBadgeForGenreAndEdition(editionId, genreId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException( ErrorCodes.BADGE_NOT_FOUND,
					"Badge not found!");
		}
	}
	
	public Badge getBadgeForStudent( String editionId, String genreId, Long studPoints) {
		Optional<Badge> existingEntity = badgeRepository.getBadgeForStudent(editionId, genreId,studPoints);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}
	
}
