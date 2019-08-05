package com.enewschamp.publication.page.data;

import java.time.LocalDate;
import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.publication.domain.common.PublicationStatusType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PublicationSearchRequest extends PageData {

	private static final long serialVersionUID = 9108562541842148687L;
	
	private LocalDate publicationDate;
	private String readingLevel1;
	private String readingLevel2;
	private String readingLevel3;
	private String readingLevel4;
	private String editorId;
	private String editionId;
	private String publisherId;
	private List<PublicationStatusType> publicationStatusList;

}
