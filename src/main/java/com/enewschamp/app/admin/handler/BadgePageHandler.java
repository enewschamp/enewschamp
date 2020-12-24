package com.enewschamp.app.admin.handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.master.badge.service.BadgeService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Badge;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("BadgePageHandler")
@Slf4j
public class BadgePageHandler implements IPageHandler {
	@Autowired
	private BadgeService badgeService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;
	private Validator validator;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createBadge(pageRequest);
			break;
		case "Update":
			pageDto = updateBadge(pageRequest);
			break;
		case "Read":
			pageDto = readBadge(pageRequest);
			break;
		case "Close":
			pageDto = closeBadge(pageRequest);
			break;
		case "List":
			pageDto = listBadge(pageRequest);
			break;
		default:
			break;
		}
		return pageDto;
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

	@SneakyThrows
	private PageDTO createBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		BadgePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), BadgePageData.class);
		pageData.setNameId(pageRequest.getData().get("name").asText());
		pageData.setGenreId(pageRequest.getData().get("genre").asText());
		validate(pageData);
		Badge badge = mapBadgeData(pageRequest, pageData);
		badge = badgeService.create(badge);
		mapHeaderData(pageRequest, pageDto, pageData, badge);
		pageData.setBadgeId(badge.getBadgeId());
		pageData.setLastUpdate(badge.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto, BadgePageData pageData, Badge badge) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
	}

	private Badge mapBadgeData(PageRequestDTO pageRequest, BadgePageData pageData) {
		Badge badge = modelMapper.map(pageData, Badge.class);
		badge.setOperatorId(pageRequest.getData().get("operator").asText());
		badge.setRecordInUse(RecordInUseType.Y);
		return badge;
	}

	@SneakyThrows
	private PageDTO updateBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		BadgePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), BadgePageData.class);
		validate(pageData);
		Badge badge = mapBadgeData(pageRequest, pageData);
		badge = badgeService.update(badge);
		mapHeaderData(pageRequest, pageDto, pageData, badge);
		pageData.setBadgeId(badge.getBadgeId());
		pageData.setLastUpdate(badge.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		BadgePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), BadgePageData.class);
		Badge badge = modelMapper.map(pageData, Badge.class);
		badge.setBadgeId(pageRequest.getData().get("id").asLong());
		badge = badgeService.read(badge);
		mapHeaderData(pageRequest, pageDto, pageData, badge);
		mapPageData(pageData, badge);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapPageData(BadgePageData pageData, Badge  badge) {
		pageData.setReadingLevel(badge.getReadingLevel());
		pageData.setImageName(badge.getImageName());
		pageData.setRecordInUse(badge.getRecordInUse().toString());
		pageData.setOperator(badge.getOperatorId());
		pageData.setLastUpdate(badge.getOperationDateTime());
	}

	@SneakyThrows
	private PageDTO closeBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		BadgePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), BadgePageData.class);
		Badge badge = modelMapper.map(pageData, Badge.class);
		badge.setBadgeId(pageData.getId());
		badge = badgeService.close(badge);
		mapHeaderData(pageRequest, pageDto, pageData, badge);
		mapPageData(pageData,  badge);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listBadge(PageRequestDTO pageRequest) {
		Page<Badge> badgeList = badgeService.list(pageRequest.getData().get("pagination").get("pageNumber").asInt(),
				pageRequest.getData().get("pagination").get("pageSize").asInt());

		List<BadgePageData> list = mapAvatarData(badgeList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		dto.setHeader(pageRequest.getHeader());
		if ((badgeList.getNumber() + 1) == badgeList.getTotalPages()) {
			pageData.getPagination().setLastPage(true);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<BadgePageData> mapAvatarData(Page<Badge> page) {
		List<BadgePageData> badgePageDataList = new ArrayList<BadgePageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<Badge> pageDataList = page.getContent();
			for (Badge badge : pageDataList) {
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				BadgePageData badgePageData = modelMapper.map(badge, BadgePageData.class);
				badgePageDataList.add(badgePageData);
			}
		}
		return badgePageDataList;
	}

	private void validate(BadgePageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<BadgePageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
