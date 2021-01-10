package com.enewschamp.app.admin.student.subscription.handler;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentSubscriptionPageData extends PageData {

	private static final long serialVersionUID = 1L;

	private long studentId = 0L;

	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EDITION_ID_NOT_EMPTY)
	private String editionId;

	@NotNull(message = MessageConstants.SUBSCRIPTION_SELECTED_NOT_NULL)
	@NotEmpty(message = MessageConstants.SUBSCRIPTION_SELECTED_NOT_EMPTY)
	private String subscriptionSelected;

	@NotNull(message = MessageConstants.START_DATE_NOT_NULL)
	private LocalDate startDate;

	@NotNull(message = MessageConstants.END_DATE_NOT_NULL)
	private LocalDate endDate;

	@NotNull(message = MessageConstants.AUTO_RENEWAL_NOT_NULL)
	@NotEmpty(message = MessageConstants.AUTO_RENEWAL_NOT_EMPTY)
	private String autoRenewal;

}
