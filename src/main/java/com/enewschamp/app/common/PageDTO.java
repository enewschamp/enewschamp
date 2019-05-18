package com.enewschamp.app.common;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PageDTO implements Serializable {
	
	@NotNull
	private HeaderDTO header;
	
	@JsonIgnore
	protected String pageName;
	
	private PageData data;
} 
