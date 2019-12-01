package com.enewschamp.publication.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.publication.app.dto.PublicationGroupDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationGroupPageData extends PageData {

	private static final long serialVersionUID = 2946660959822945736L;

	private PublicationGroupDTO publicationGroup;
	private List<ListOfValuesItem> editorLOV;
	private List<ListOfValuesItem> publisherLOV;
	private List<ListOfValuesItem> editionsLOV;
}
