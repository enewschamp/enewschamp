package com.enewschamp.app.recognition.page.handler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.handler.NewsArticlePageHandler;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.recognition.page.data.RecognitionPageData;
import com.enewschamp.app.student.badges.business.StudentBadgesBusiness;
import com.enewschamp.app.student.badges.entity.StudentBadges;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "RecognitionPageHandler")
public class RecognitionPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	NewsArticlePageHandler newsArticlePageHandler;

	@Autowired
	StudentBadgesBusiness studentBadgesBusiness;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String action = pageNavigationContext.getActionName();
		int pageNo = pageNavigationContext.getPageRequest().getHeader().getPageNo();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();

		// if (PageAction.upswipe.toString().equalsIgnoreCase(action)) {
		// pageDto = newsArticlePageHandler.loadPage(pageNavigationContext);
		// }

		if (PageAction.downswipe.toString().equalsIgnoreCase(action)) {

		}
		if (PageAction.upswipe.toString().equalsIgnoreCase(action)) {
			pageNo = pageNo + 1;

			Page<StudentBadges> studbadges = studentBadgesBusiness.getStudentBadges(studentId, editionId, pageNo);
			RecognitionPageData pageData = new RecognitionPageData();
			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(studbadges.isLast());
			header.setPageCount(studbadges.getTotalPages());
			header.setRecordCount(studbadges.getNumberOfElements());
			header.setPageNo(studbadges.getNumber() + 1);
			pageDto.setHeader(header);

			if (!studbadges.isEmpty()) {
				pageData.setRecognitions(studbadges.getContent());

			}
			pageDto.setData(pageData);

		}
		if (PageAction.recognitions.toString().equalsIgnoreCase(action)
				|| PageAction.MyActivity.toString().equalsIgnoreCase(action)) {
			Page<StudentBadges> studbadges = studentBadgesBusiness.getStudentBadges(studentId, editionId, pageNo);
			RecognitionPageData pageData = new RecognitionPageData();
			HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
			header.setIsLastPage(studbadges.isLast());
			header.setPageCount(studbadges.getTotalPages());
			header.setRecordCount(studbadges.getNumberOfElements());
			header.setPageNo(studbadges.getNumber() + 1);
			pageDto.setHeader(header);

			if (!studbadges.isEmpty()) {
				pageData.setRecognitions(studbadges.getContent());
			}
			pageDto.setData(pageData);

			// else
			// {
			// throw new BusinessException(ErrorCodes.STUD_BADGES_NOT_FOUND);
			// }
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
