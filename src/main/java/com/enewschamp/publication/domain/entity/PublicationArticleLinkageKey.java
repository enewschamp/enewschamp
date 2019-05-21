package com.enewschamp.publication.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
@MappedSuperclass
public class PublicationArticleLinkageKey implements Serializable {
	
	private static final long serialVersionUID = 464080117813550319L;

	@Id
	@NotNull
	@Column(name = "PublicationID", length=10)
	private long publicationId = 0L;
	
	@Id
	@NotNull
	@Column(name = "NewsArticleID", length=10)
	private long newsArticleId = 0L;
	
}
