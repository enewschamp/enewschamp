package com.enewschamp.app.admin.dashboard.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.country.service.CountryService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.holiday.service.HolidayService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.master.badge.service.BadgeService;
import com.enewschamp.publication.domain.common.BadgeList;
import com.enewschamp.publication.domain.common.HolidayList;
import com.enewschamp.publication.domain.service.AvatarService;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.user.domain.service.UserService;

@Component("AdminDashBoardPageHandler")
public class AdminDashBoardPageHandler implements IPageHandler {

	@Autowired
	private UserService userService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private AvatarService avatarService;

	@Autowired
	private EditionService editionService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private BadgeService badgeService;

	@Autowired
	private HolidayService holidayService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "List":
			pageDto = listDashboardData(pageRequest);
			break;
		default:
			break;
		}
		return pageDto;
	}

	private PageDTO listDashboardData(PageRequestDTO pageRequest) {
		PageDTO dto = new PageDTO();
		AdminDashBoardPageData pageData = new AdminDashBoardPageData();
		List<UserView> userViews = userService.getAllUserView();
		List<CountryView> countries = countryService.getAllCountryView();
		List<AvatarView> avatars = avatarService.getAllAvatarView();
		List<EditionView> editions = editionService.getAllEditionView();
		List<GenreView> genres = genreService.getAllGenreView();
		List<BadgeList> badges = badgeService.getBadgeList();
		List<HolidayList> holidays = holidayService.getHolidayList();
		pageData.setUsers(userViews);
		pageData.setCountries(countries);
		pageData.setAvatars(avatars);
		pageData.setEditions(editions);
		pageData.setGenres(genres);
		pageData.setBadges(badges);
		pageData.setHolidays(holidays);
		dto.setData(pageData);
		dto.setHeader(pageRequest.getHeader());
		return dto;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		// TODO Auto-generated method stub
		return null;
	}
}
