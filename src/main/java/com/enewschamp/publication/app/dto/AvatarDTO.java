package com.enewschamp.publication.app.dto;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AvatarDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -3428291258057090659L;

	private Long avatarId;

	@NotNull
	private String nameId;

	@NotNull
	private String gender;

	@NotNull
	private String readingLevel;

	private String imageName;

	private String base64Image;

	private String imageTypeExt = "jpg";
}
