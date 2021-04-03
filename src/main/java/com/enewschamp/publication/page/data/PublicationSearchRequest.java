package com.enewschamp.publication.page.data;

import java.time.LocalDate;
import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.publication.domain.common.PublicationGroupStatusType;
import com.enewschamp.publication.domain.common.PublicationStatusType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationSearchRequest extends PageData {

	private static final long serialVersionUID = 9108562541842148687L;

	private LocalDate publicationDateFrom;
	private LocalDate publicationDateTo;
	private String readingLevel1;
	private String readingLevel2;
	private String readingLevel3;
	private String readingLevel4;
	private String editorId;
	private String editionId;
	private String publisherId;
	private List<PublicationStatusType> status;

	public int getReadingLevel() {
		if (readingLevel1 != null && readingLevel1.equals("Y")) {
			return 1;
		} else if (readingLevel2 != null && readingLevel2.equals("Y")) {
			return 2;
		} else if (readingLevel3 != null && readingLevel3.equals("Y")) {
			return 3;
		} else if (readingLevel4 != null && readingLevel4.equals("Y")) {
			return 4;
		}
		return 0;
	}

}
