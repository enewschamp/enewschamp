package com.enewschamp.publication.app.service;

import java.util.List;

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
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.publication.app.dto.AvatarDTO;
import com.enewschamp.publication.domain.entity.Avatar;
import com.enewschamp.publication.domain.service.AvatarService;
import com.google.common.net.MediaType;

@RestController
@RequestMapping("/enewschamp-api/v1")
public class AvatarController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private AvatarService avatarService;

	@Autowired
	CommonService commonService;

	@PostMapping(value = "/admin/avatars")
	public ResponseEntity<AvatarDTO> create(@RequestBody @Valid AvatarDTO avatarDTO) {
		Avatar avatar = modelMapper.map(avatarDTO, Avatar.class);
		avatar = avatarService.create(avatar);
		String newImageName = avatar.getAvatarId() + "_" + System.currentTimeMillis();
		String imageType = avatarDTO.getImageTypeExt();
		boolean saveFlag = commonService.saveImages("Admin", "avatar", imageType, avatarDTO.getBase64Image(),
				newImageName, avatar.getImageName());
		if (saveFlag) {
			avatar.setImageName(newImageName + "." + imageType);
			avatar = avatarService.update(avatar);
		}
		avatarDTO = modelMapper.map(avatar, AvatarDTO.class);
		return new ResponseEntity<AvatarDTO>(avatarDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/admin/avatars/{avatarId}")
	public ResponseEntity<AvatarDTO> update(@RequestBody @Valid AvatarDTO avatarDTO, @PathVariable Long avatarId) {
		avatarDTO.setAvatarId(avatarId);
		Avatar avatar = modelMapper.map(avatarDTO, Avatar.class);
		avatar = avatarService.update(avatar);
		avatarDTO = modelMapper.map(avatar, AvatarDTO.class);
		return new ResponseEntity<AvatarDTO>(avatarDTO, HttpStatus.OK);
	}

	@PatchMapping(value = "/admin/avatars/{avatarId}")
	public ResponseEntity<AvatarDTO> patch(@RequestBody AvatarDTO avatarDTO, @PathVariable Long avatarId) {
		avatarDTO.setAvatarId(avatarId);
		Avatar avatar = modelMapper.map(avatarDTO, Avatar.class);
		avatar = avatarService.patch(avatar);
		avatarDTO = modelMapper.map(avatar, AvatarDTO.class);
		return new ResponseEntity<AvatarDTO>(avatarDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/admin/avatars/{avatarId}")
	public ResponseEntity<Void> delete(@PathVariable Long avatarId) {
		avatarService.delete(avatarId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping(value = "/admin/avatars/{avatarId}")
	public ResponseEntity<AvatarDTO> get(@PathVariable Long avatarId) {
		Avatar avatar = avatarService.get(avatarId);
		AvatarDTO avatarDTO = modelMapper.map(avatar, AvatarDTO.class);
		avatarService.get(avatarId);
		return new ResponseEntity<AvatarDTO>(avatarDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/admin/avatarLOV")
	public ResponseEntity<List<ListOfValuesItem>> getLOV() {

		return new ResponseEntity<List<ListOfValuesItem>>(avatarService.getLOV(), HttpStatus.OK);
	}

	@GetMapping(value = "/admin/avatars/{avatarId}/audit", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getAudit(@PathVariable Long avatarId) {
		String audit = avatarService.getAudit(avatarId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}

}
