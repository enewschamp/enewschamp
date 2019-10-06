package com.enewschamp.app.welcome.page.handler;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.badges.business.StudentBadgesBusiness;
import com.enewschamp.app.student.badges.entity.StudentBadges;
import com.enewschamp.app.student.login.entity.StudentLogin;
import com.enewschamp.app.student.login.service.StudentLoginBusiness;
import com.enewschamp.app.student.monthlytrends.business.TrendsMonthlyByGenreBusiness;
import com.enewschamp.app.student.monthlytrends.business.TrendsMonthlyTotalBusiness;
import com.enewschamp.app.student.monthlytrends.dto.TrendsMonthlyByGenreDTO;
import com.enewschamp.app.student.monthlytrends.dto.TrendsMonthlyTotalDTO;
import com.enewschamp.app.trends.service.TrendsService;
import com.enewschamp.app.welcome.page.data.WelcomePageData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.master.badge.entity.Badge;
import com.enewschamp.master.badge.service.BadgeService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Edition;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.subscription.app.dto.StudentDetailsDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "WelcomePageHandler")
public class WelcomePageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	TrendsService trendsService;
	@Autowired
	StudentDetailsBusiness studentDetailsBusiness;

	@Autowired
	EditionService editionService;

	@Autowired
	StudentLoginBusiness studentLoginBusiness;

	@Autowired
	StudentBadgesBusiness studentBadgesBusiness;
	@Autowired
	BadgeService badgeService;
	@Autowired
	TrendsMonthlyTotalBusiness trendsMonthlyTotalBusiness;

	@Autowired
	TrendsMonthlyByGenreBusiness trendsMonthlyByGenreBusiness;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		//no page number to be returned..
		
		pageDto.getHeader().setPageNo(0);
		
		String operation = pageNavigationContext.getPageRequest().getHeader().getOperation();
		String action = pageNavigationContext.getPageRequest().getHeader().getAction();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionID();
		String editionName = "";
		String deviceId = pageNavigationContext.getPageRequest().getHeader().getDeviceId();
		String badgeGenre = "";
		Long yearMonth = null;
		Long pointsToNextBadge = 0L;
		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodes.STUDENT_DTLS_NOT_FOUND);
		}

		if (PageAction.WelcomeUser.toString().equalsIgnoreCase(action)) {
			WelcomePageData pageData = new WelcomePageData();

			StudentDetailsDTO studentDetailsDTO = studentDetailsBusiness.getStudentDetailsFromMaster(studentId);
			pageData.setStudentName(studentDetailsDTO.getName());
			pageData.setUserImage(studentDetailsDTO.getPhoto());

			Edition edition = editionService.getEdition(editionId);
			editionName = edition.getEditionName();
			pageData.setEditionName(editionName);

			StudentLogin studentLogin = studentLoginBusiness.getLoginDetails(deviceId, emailId);
			if(studentLogin!=null) {
			LocalDateTime lastLoginTime = studentLogin.getLastLoginTime();

			if (lastLoginTime != null)
				pageData.setLastLoginDatetime(lastLoginTime);
			}

			// get Student Badge..
			StudentBadges studentbadge = studentBadgesBusiness.getLastestBadge(studentId, editionId);
			Badge badge = null;
			if (studentbadge != null) {
				badge = badgeService.get(studentbadge.getBadgeId());
				badgeGenre = badge.getGenreId();
				System.out.println(" badgeGenre :"+badgeGenre);
				yearMonth = studentbadge.getYearMonth();
				pageData.setBadgeDate(studentbadge.getOperationDateTime().toLocalDate());
				pageData.setBadgeImage(studentbadge.getBadgeImage());

			}

			// display the next bagde message..
			// get the existing points for this edition..
			if (badgeGenre != null && !"".equals(badgeGenre)) {
				// get data from Trends Monthly by Genre

				TrendsMonthlyByGenreDTO trendsMonthlyByGenreDTO = trendsMonthlyByGenreBusiness
						.getMonthlyTrend(studentId, editionId, "" + yearMonth, badgeGenre);

				if (trendsMonthlyByGenreDTO != null) {
					Long points = trendsMonthlyByGenreDTO.getQuizQCorrect();
					Badge nextbadge = badgeService.getNextBadgeForGenre(editionId, trendsMonthlyByGenreDTO.getGenreId(),
							points);
					Long monthlyPointsToScore = nextbadge.getMonthlyPointsToScore();
					pointsToNextBadge = monthlyPointsToScore - points;
					String message = "Just " + pointsToNextBadge + " away from winning a " + nextbadge.getName();
					pageData.setNextBadgeMessage(message);

				}

			} else {
				// get data from Trends Monthly Total

				TrendsMonthlyTotalDTO trendsMonthlyTotalDTO = trendsMonthlyTotalBusiness.getMonthlyTrend(studentId,
						editionId, "" + yearMonth);
				if (trendsMonthlyTotalDTO != null) {
					Long points = trendsMonthlyTotalDTO.getQuizQCorrect();
					Badge nextbadge = badgeService.getNextBadge(editionId, points);
					Long monthlyPointsToScore = nextbadge.getMonthlyPointsToScore();
					pointsToNextBadge = monthlyPointsToScore - points;
					String message = "Just " + pointsToNextBadge + " away from winning a " + nextbadge.getName();
					pageData.setNextBadgeMessage(message);
				}

			}
			pageDto.setData(pageData);
		}

		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();

		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();

		return pageDto;
	}

}
