package com.enewschamp.subscription.app.dto;

import java.util.TreeMap;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TreeMap<String, String> paramMap;
	private TreeMap<String, String> inResponse;
	private String inErrorMessage;
	private int iniErrorCode;
	private String inFailingUrl;

}
