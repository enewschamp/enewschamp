package com.enewschamp.article.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.publication.app.dto.HashTagDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HashTagPageData extends PageData {

	private static final long serialVersionUID = 2560121552740385594L;

	private String message;
	private List<HashTagDTO> hashTagList;
}