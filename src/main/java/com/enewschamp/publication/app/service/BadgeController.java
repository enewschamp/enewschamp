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
		String newImageName = badge.getBadgeId() + "_" + System.currentTimeMillis();
		String imageType = badgeDTO.getImageTypeExt();
		boolean saveFlag = commonService.saveImages("Admin", "badge", imageType, badgeDTO.getBase64Image(),
				newImageName, badge.getImageName());
		if (saveFlag) {
			badge.setImageName(newImageName + "." + imageType);
			badge = badgeService.update(badge);
		}
		badgeDTO = modelMapper.map(badge, BadgeDTO.class);
		return new ResponseEntity<BadgeDTO>(badgeDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/admin/badges/{badgeId}")
	public ResponseEntity<BadgeDTO> update(@RequestBody @Valid BadgeDTO badgeDTO, @PathVariable Long badgeId) {
		badgeDTO.setBadgeId(badgeId);
		Badge badge = modelMapper.map(badgeDTO, Badge.class);
		badge = badgeService.update(badge);
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
