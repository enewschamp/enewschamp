package com.enewschamp.app.admin.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.enewschamp.domain.common.Gender;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AvatarPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long avatarId = 0L;
	@NotNull(message = MessageConstants.AVATAR_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.AVATAR_NAME_NOT_EMPTY)
	private String nameId;
	@NotNull(message = MessageConstants.GENDER_NOT_NULL)
	@NotEmpty(message = MessageConstants.GENDER_NOT_EMPTY)
	private Gender gender;
	@NotNull(message = MessageConstants.READING_LEVEL_NOT_NULL)
	@NotEmpty(message = MessageConstants.READING_LEVEL_NOT_EMPTY)
	private int readingLevel;
	private String imageName;
	private String base64Image;
}
