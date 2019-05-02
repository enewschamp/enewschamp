package com.enewschamp.publication.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
@MappedSuperclass
public class PublicationArticleKey implements Serializable {
	
	private static final long serialVersionUID = 464080117813550319L;

	@NotNull
	@Column(name = "PublicationID", length=10)
	private long publicationId = 0L;
	
	@NotNull
	@Column(name = "NewsArticleID", length=10)
	private long newsArticleId = 0L;
	
}
