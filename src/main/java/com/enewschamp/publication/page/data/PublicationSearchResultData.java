package com.enewschamp.publication.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.publication.app.dto.PublicationSummaryDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationSearchResultData extends PageData {

	private static final long serialVersionUID = 7730442567434572236L;
	private List<PublicationSummaryDTO> publications;

}
