package com.enewschamp.publication.app.service;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.app.common.CommonService;
import com.enewschamp.master.badge.service.BadgeService;
import com.enewschamp.publication.app.dto.BadgeDTO;
import com.enewschamp.publication.domain.entity.Badge;

@RestController
@RequestMapping("/enewschamp-api/v1")
public class BadgeController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private BadgeService badgeService;

	@Autowired
	CommonService commonService;

	@PostMapping(value = "/admin/badges")
	public ResponseEntity<BadgeDTO> create(@RequestBody @Valid BadgeDTO badgeDTO) {
		Badge badge = modelMapper.map(badgeDTO, Badge.class);
		badge = badgeService.create(badge);
		boolean updateFlag = false;
		if ("Y".equalsIgnoreCase(badgeDTO.getImageUpdate())) {
			String newImageName = badge.getBadgeId() + "_IMG_" + System.currentTimeMillis();
			String imageType = badgeDTO.getImageTypeExt();
			String currentImageName = badge.getImageName();
			boolean saveImageFlag = commonService.saveImages("Admin", "badge", imageType, badgeDTO.getBase64Image(),
					newImageName);
			if (saveImageFlag) {
				badge.setImageName(newImageName + "." + imageType);
				updateFlag = true;
			} else {
				badge.setImageName(null);
				updateFlag = true;
			}
			if (currentImageName != null && !"".equals(currentImageName)) {
				commonService.deleteImages("Admin", "badge", currentImageName);
				updateFlag = true;
			}
		}
		if ("Y".equalsIgnoreCase(badgeDTO.getSuccessImageUpdate())) {
			String successImageType = badgeDTO.getSuccessImageTypeExt();
			String newSucessImageName = badge.getBadgeId() + "_SI_" + System.currentTimeMillis();
			String currentSuccessImageName = badge.getSuccessImageName();
			boolean saveSuccessImageFlag = commonService.saveImages("Admin", "badge", successImageType,
					badgeDTO.getBase64SuccessImage(), newSucessImageName);
			if (saveSuccessImageFlag) {
				badge.setSuccessImageName(newSucessImageName + "." + successImageType);
				updateFlag = true;
			} else {
				badge.setSuccessImageName(null);
				updateFlag = true;
			}
			if (currentSuccessImageName != null && !"".equals(currentSuccessImageName)) {
				commonService.deleteImages("Admin", "badge", currentSuccessImageName);
				updateFlag = true;
			}
		}
		if ("Y".equalsIgnoreCase(badgeDTO.getAudioFileUpdate())) {
			String audioFileName = badge.getBadgeId() + "_" + System.currentTimeMillis();
			String audioFileType = badgeDTO.getAudioFileTypeExt();
			String currentAudioFileName = badge.getAudioFileName();
			boolean saveAudioFileFlag = commonService.saveAudioFile("Admin", "badge", audioFileType,
					badgeDTO.getBase64AudioFile(), audioFileName);
			if (saveAudioFileFlag) {
				badge.setAudioFileName(audioFileName + "." + audioFileType);
				updateFlag = true;
			} else {
				badge.setAudioFileName(null);
				updateFlag = true;
			}
			if (currentAudioFileName != null && !"".equals(currentAudioFileName)) {
				commonService.deleteAudios("Admin", "badge", currentAudioFileName);
				updateFlag = true;
			}
		}
		if (updateFlag) {
			badge = badgeService.update(badge);
		}
		badgeDTO = modelMapper.map(badge, BadgeDTO.class);
		return new ResponseEntity<BadgeDTO>(badgeDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/admin/badges/{badgeId}")
	public ResponseEntity<BadgeDTO> update(@RequestBody @Valid BadgeDTO badgeDTO, @PathVariable Long badgeId) {
		badgeDTO.setBadgeId(badgeId);
		Badge badge = badgeService.get(badgeId);
		String currentImageName = badge.getImageName();
		String currentSuccessImageName = badge.getSuccessImageName();
		String currentAudioFileName = badge.getAudioFileName();
		badgeDTO.setAudioFileName(currentAudioFileName);
		badgeDTO.setSuccessImageName(currentSuccessImageName);
		badgeDTO.setImageName(currentImageName);
		badge = modelMapper.map(badgeDTO, Badge.class);
		badge = badgeService.update(badge);
		boolean updateFlag = false;
		if ("Y".equalsIgnoreCase(badgeDTO.getImageUpdate())) {
			String newImageName = badge.getBadgeId() + "_IMG_" + System.currentTimeMillis();
			String imageType = badgeDTO.getImageTypeExt();
			boolean saveImageFlag = commonService.saveImages("Admin", "badge", imageType, badgeDTO.getBase64Image(),
					newImageName);
			if (saveImageFlag) {
				badge.setImageName(newImageName + "." + imageType);
				updateFlag = true;
			} else {
				badge.setImageName(null);
				updateFlag = true;
			}
			if (currentImageName != null && !"".equals(currentImageName)) {
				commonService.deleteImages("Admin", "badge", currentImageName);
				updateFlag = true;
			}
		}
		if ("Y".equalsIgnoreCase(badgeDTO.getSuccessImageUpdate())) {
			String successImageType = badgeDTO.getSuccessImageTypeExt();
			String newSucessImageName = badge.getBadgeId() + "_SI_" + System.currentTimeMillis();
			boolean saveSuccessImageFlag = commonService.saveImages("Admin", "badge", successImageType,
					badgeDTO.getBase64SuccessImage(), newSucessImageName);
			if (saveSuccessImageFlag) {
				badge.setSuccessImageName(newSucessImageName + "." + successImageType);
				updateFlag = true;
			} else {
				badge.setSuccessImageName(null);
				updateFlag = true;
			}
			if (currentSuccessImageName != null && !"".equals(currentSuccessImageName)) {
				commonService.deleteImages("Admin", "badge", currentSuccessImageName);
				updateFlag = true;
			}
		}
		if ("Y".equalsIgnoreCase(badgeDTO.getAudioFileUpdate())) {
			String audioFileName = badge.getBadgeId() + "_" + System.currentTimeMillis();
			String audioFileType = badgeDTO.getAudioFileTypeExt();
			boolean saveAudioFileFlag = commonService.saveAudioFile("Admin", "badge", audioFileType,
					badgeDTO.getBase64AudioFile(), audioFileName);
			if (saveAudioFileFlag) {
				badge.setAudioFileName(audioFileName + "." + audioFileType);
				updateFlag = true;
			} else {
				badge.setAudioFileName(null);
				updateFlag = true;
			}
			if (currentAudioFileName != null && !"".equals(currentAudioFileName)) {
				commonService.deleteAudios("Admin", "badge", currentAudioFileName);
				updateFlag = true;
			}
		}
		if (updateFlag) {
			badge = badgeService.update(badge);
		}
		badgeDTO = modelMapper.map(badge, BadgeDTO.class);
		return new ResponseEntity<BadgeDTO>(badgeDTO, HttpStatus.OK);
	}

	@PatchMapping(value = "/admin/badges/{badgeId}")
	public ResponseEntity<BadgeDTO> patch(@RequestBody BadgeDTO badgeDTO, @PathVariable Long badgeId) {
		badgeDTO.setBadgeId(badgeId);
		Badge badge = modelMapper.map(badgeDTO, Badge.class);
		badge = badgeService.patch(badge);
		badgeDTO = modelMapper.map(badge, BadgeDTO.class);
		return new ResponseEntity<BadgeDTO>(badgeDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/admin/badges/{badgeId}")
	public ResponseEntity<Void> delete(@PathVariable Long badgeId) {
		badgeService.delete(badgeId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping(value = "/admin/badges/{badgeId}")
	public ResponseEntity<BadgeDTO> get(@PathVariable Long badgeId) {
		Badge badge = badgeService.get(badgeId);
		BadgeDTO badgeDTO = modelMapper.map(badge, BadgeDTO.class);
		badgeService.get(badgeId);
		return new ResponseEntity<BadgeDTO>(badgeDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/admin/badges/{badgeId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long badgeId) {
		String audit = badgeService.getAudit(badgeId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}
}
