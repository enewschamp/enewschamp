package com.enewschamp.subscription.app.dto;

import com.enewschamp.domain.common.Gender;

import lombok.Data;

@Data
public class AvatarPageData {
	private String id;
	private String name;
	private Gender gender;
	private int readingLevel;
}
