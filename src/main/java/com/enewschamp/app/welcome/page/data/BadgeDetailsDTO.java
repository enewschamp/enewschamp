package com.enewschamp.app.welcome.page.data;

import java.time.LocalDate;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BadgeDetailsDTO extends MaintenanceDTO {

	private static final long serialVersionUID = 9149296760799057280L;

	private Long badgeId;
	private String badgeName;
	private LocalDate badgeGrantDate;
	private String badgeAwayPointsMessage;
	private String badgeGenre;
	private String base64Image;
	private String imageName;
}
