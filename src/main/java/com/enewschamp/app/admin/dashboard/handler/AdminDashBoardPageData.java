package com.enewschamp.app.admin.dashboard.handler;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.publication.domain.common.BadgeList;
import com.enewschamp.publication.domain.common.HolidayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AdminDashBoardPageData extends PageData {
	private static final long serialVersionUID = 1L;
	
	private List<GenreView> genres;

	private List<BadgeList> badges;

	private List<HolidayList> holidays;
	
	private List<CountryView> countries;
	
	private List<AvatarView> avatars;
	
	private List<EditionView> editions;
	
	private List<UserView> users;
} 
