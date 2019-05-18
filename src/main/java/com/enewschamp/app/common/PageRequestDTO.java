package com.enewschamp.app.common;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PageRequestDTO implements Serializable {
	
	private static final long serialVersionUID = -371767964533607210L;

	@NotNull
	private HeaderDTO header;
	
	private JsonNode data;
} 
