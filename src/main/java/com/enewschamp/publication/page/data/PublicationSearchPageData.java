package com.enewschamp.publication.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.page.dto.ListOfValuesItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationSearchPageData extends PageData {

	private static final long serialVersionUID = -1126364223371401140L;
	private List<ListOfValuesItem> editionsLOV;
	private List<ListOfValuesItem> authorLOV;
	private List<ListOfValuesItem> editorLOV;
	private List<ListOfValuesItem> publisherLOV;

}