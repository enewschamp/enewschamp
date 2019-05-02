package com.enewschamp.publication.app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsArticleQuizDTO extends AbstractDTO {

	private static final long serialVersionUID = 9149296760799057280L;

	private long newsArticleQuizId = 0L;
	
	@NotNull
	@Size(max=10)
	private long newsArticleId = 0L;
	
	@NotNull
	@Size(max=99)
	private String question;
	
	@NotNull
	@Size(max=99)
	private String opt1;
	
	@NotNull
	@Size(max=99)
	private String opt2;
	
	@Size(max=99)
	private String opt3;
	
	@Size(max=99)
	private String opt4;
	
	@NotNull
	private int correctOpt = 0;

}
