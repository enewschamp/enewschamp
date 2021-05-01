package com.enewschamp.app.admin.errorcode.handler;

import java.util.List;

import lombok.Data;

@Data
public class ErrorCodesPageRoot {
	private boolean cleanRequired;
	private List<ErrorCodesPageData> errorCodes;
}
