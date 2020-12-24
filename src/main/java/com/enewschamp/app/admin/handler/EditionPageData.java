package com.enewschamp.app.admin.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EditionPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@NotNull
	private String id;
	private String editionName;
    @NotNull
	private Long languageId;
	private String recordInUse;
	private String operator;
	private LocalDateTime lastUpdate;
}
