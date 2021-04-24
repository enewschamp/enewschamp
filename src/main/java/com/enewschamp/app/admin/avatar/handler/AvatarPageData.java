package com.enewschamp.app.admin.avatar.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.enewschamp.domain.common.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AvatarPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long avatarId;
	@NotNull(message = MessageConstants.AVATAR_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.AVATAR_NAME_NOT_EMPTY)
	private String nameId;
	@NotNull(message = MessageConstants.GENDER_NOT_NULL)
	private Gender gender;
	@NotNull(message = MessageConstants.READING_LEVEL_NOT_NULL)
	private int readingLevel;
	private String imageName;
	private String imageBase64;
	private String imageTypeExt;
	private String imageUpdate;

	@JsonIgnore
	public String getImageBase64() {
		return this.imageBase64;
	}

	@JsonProperty
	public void setImageBase64(String imageBase64) {
		this.imageBase64 = imageBase64;
	}
}
