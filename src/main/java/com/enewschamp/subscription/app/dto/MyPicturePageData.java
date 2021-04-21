package com.enewschamp.subscription.app.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MyPicturePageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private String avatarName;

	@JsonInclude
	private String photoName;

	@JsonInclude
	private String imageApprovalRequired;

	private String photoBase64;

	private String imageTypeExt;

	private String deleteImage;

	@JsonInclude
	private List<AvatarPageData> avatarLOV;

}