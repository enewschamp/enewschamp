package com.enewschamp.app.common;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageRequestDTO implements Serializable {

	private static final long serialVersionUID = -371767964533607210L;

	@JsonInclude
	@NotNull
	private HeaderDTO header;

	@JsonInclude
	private JsonNode data;

	private JsonNode filter;

	public HeaderDTO getHeader() {
		if (this.header == null) {
			this.header = new HeaderDTO();
		}
		return this.header;
	}
}
