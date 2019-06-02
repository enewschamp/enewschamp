package com.enewschamp.app.common;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class HeaderDTO implements Serializable {
	
	private static final long serialVersionUID = -2727382165795060076L;
	
	private RequestStatusType requestStatus;
	private String failureMessage;
	private String module;
	private String pageName;
	private String action;
	private String operation;
	private String editionID;
	private LocalDate publicationdate;
	private String emailID;
	private Integer pageNumber;
	private Integer pageSize;
	private Integer pageCount;
	private Integer recordCount;
	private Boolean isLastPage;
	
}
