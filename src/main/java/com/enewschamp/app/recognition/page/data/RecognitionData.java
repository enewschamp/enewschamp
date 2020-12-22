package com.enewschamp.app.recognition.page.data;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.enewschamp.app.common.AbstractDTO;
import com.enewschamp.subscription.app.dto.SchoolProgramLOV;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Entity
public class RecognitionData extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	@Id
	private Long studentBadgesId;
	private String editionId;
	private int readingLevel;
	private Long badgeId;
	private String badgeName;
	private LocalDateTime badgeGrantDate;
	private String badgeGenre;
	private Long monthYear;

}
