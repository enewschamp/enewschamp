package com.enewschamp.publication.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;
import com.enewschamp.publication.domain.common.PublicationStatusType;
import com.enewschamp.publication.domain.service.PublicationBusinessPolicy;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "Publication")
public class Publication extends BaseEntity {

	private static final long serialVersionUID = -6656836773546374871L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publication_id_generator")
	@SequenceGenerator(name = "publication_id_generator", sequenceName = "pub_id_seq", allocationSize = 1)
	@Column(name = "PublicationID", updatable = false, nullable = false)
	private Long publicationId;

	@NotNull
	@Column(name = "PublicationGroupID")
	private long publicationGroupId = 0L;

	@NotNull
	@Column(name = "EditionID", length = 6)
	private String editionId;

	@NotNull
	@Column(name = "ReadingLevel")
	private int readingLevel = 0;

	@NotNull
	@Column(name = "PublishDate")
	private LocalDate publishDate;

	@Column(name = "Status", length = 25)
	@Enumerated(EnumType.STRING)
	private PublicationStatusType status;

	@NotNull
	@Column(name = "EditorId", length = ForeignKeyColumnLength.UserId)
	private String editorId;

	@NotNull
	@Column(name = "PublisherId", length = ForeignKeyColumnLength.UserId)
	private String publisherId;

	@Column(name = "Comments")
	@Lob
	private String comments;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "publication_Id")
	private List<PublicationArticleLinkage> articleLinkages;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		new PublicationBusinessPolicy(this).validateAndThrow();
		if(operationDateTime == null) {
			operationDateTime = LocalDateTime.now();
		}
		if(articleLinkages != null && publicationId != 0) {
			for(PublicationArticleLinkage linkage: articleLinkages) {
				linkage.setPublicationId(publicationId);
			}
		}
	}
}
