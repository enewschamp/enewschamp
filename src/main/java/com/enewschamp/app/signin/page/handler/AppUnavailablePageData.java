package com.enewschamp.app.signin.page.handler;

import java.io.Serializable;
import java.time.LocalDate;

import com.enewschamp.app.common.PageData;

import lombok.Data;

@Data
public class AppUnavailablePageData extends PageData implements Serializable {

	private static final long serialVersionUID = -7777503117172470732L;
	private String message;
}
