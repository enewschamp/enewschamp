package com.enewschamp.app.admin.handler;

import java.time.LocalDateTime;

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
	private String nameId;
	private Gender gender;
	private int readingLevel;
	private String imageName;
	private String base64Image;
	private String recordInUse;
	private String operator;
	private LocalDateTime lastUpdate;
}
