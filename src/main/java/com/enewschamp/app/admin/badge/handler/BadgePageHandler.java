package com.enewschamp.app.admin.badge.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
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
	CommonService commonService;

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
		case "Reinstate":
			pageDto = reinstateBadge(pageRequest);
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
		validate(pageData);
		Badge badge = saveImageAndAudio(pageData);
		mapBadge(pageRequest, pageDto, badge);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		BadgePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), BadgePageData.class);
		validate(pageData);
		Badge badge = updateImageAndAudio(pageData, pageData.getBadgeId());
		mapBadge(pageRequest, pageDto, badge);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		BadgePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), BadgePageData.class);
		Badge badge = modelMapper.map(pageData, Badge.class);
		badge = badgeService.read(badge);
		mapBadge(pageRequest, pageDto, badge);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		BadgePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), BadgePageData.class);
		Badge badge = modelMapper.map(pageData, Badge.class);
		badge = badgeService.close(badge);
		mapBadge(pageRequest, pageDto, badge);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		BadgePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), BadgePageData.class);
		Badge badge = modelMapper.map(pageData, Badge.class);
		badge = badgeService.reInstate(badge);
		mapBadge(pageRequest, pageDto, badge);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listBadge(PageRequestDTO pageRequest) {
//		AdminSearchRequest searchRequest = objectMapper
//				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<Badge> badgeList = badgeService.list(
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<BadgePageData> list = mapBadgeData(badgeList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((badgeList.getNumber() + 1) == badgeList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private BadgePageData mapPageData(Badge badge) {
		BadgePageData pageData = modelMapper.map(badge, BadgePageData.class);
		pageData.setLastUpdate(badge.getOperationDateTime());
		pageData.setBase64Image(null);
		return pageData;
	}

	private void mapBadge(PageRequestDTO pageRequest, PageDTO pageDto, Badge badge) {
		BadgePageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(badge);
		pageDto.setData(pageData);
	}

	public List<BadgePageData> mapBadgeData(Page<Badge> page) {
		List<BadgePageData> badgePageDataList = new ArrayList<BadgePageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<Badge> pageDataList = page.getContent();
			for (Badge badge : pageDataList) {
				BadgePageData badgePageData = modelMapper.map(badge, BadgePageData.class);
				badgePageData.setLastUpdate(badge.getOperationDateTime());
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
				log.error("Validation failed: " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}

	private Badge updateImageAndAudio(BadgePageData badgeDTO, Long badgeId) {
		badgeDTO.setBadgeId(badgeId);
		Badge badge = badgeService.get(badgeId);
		if (badge.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		String currentImageName = badge.getImageName();
		String currentSuccessImageName = badge.getSuccessImageName();
		String currentAudioFileName = badge.getAudioFileName();
		badgeDTO.setAudioFileName(currentAudioFileName);
		badgeDTO.setSuccessImageName(currentSuccessImageName);
		badgeDTO.setImageName(currentImageName);
		badge = modelMapper.map(badgeDTO, Badge.class);
		badge = badgeService.update(badge);
		boolean updateFlag = false;
		if ("Y".equalsIgnoreCase(badgeDTO.getImageUpdate())) {
			String newImageName = badge.getBadgeId() + "_IMG_" + System.currentTimeMillis();
			String imageType = badgeDTO.getImageTypeExt();
			boolean saveImageFlag = commonService.saveImages("Admin", "badge", imageType, badgeDTO.getBase64Image(),
					newImageName);
			if (saveImageFlag) {
				badge.setImageName(newImageName + "." + imageType);
				updateFlag = true;
			} else {
				badge.setImageName(null);
				updateFlag = true;
			}
			if (currentImageName != null && !"".equals(currentImageName)) {
				commonService.deleteImages("Admin", "badge", currentImageName);
				updateFlag = true;
			}
		}
		if ("Y".equalsIgnoreCase(badgeDTO.getSuccessImageUpdate())) {
			String successImageType = badgeDTO.getSuccessImageTypeExt();
			String newSucessImageName = badge.getBadgeId() + "_SI_" + System.currentTimeMillis();
			boolean saveSuccessImageFlag = commonService.saveImages("Admin", "badge", successImageType,
					badgeDTO.getBase64SuccessImage(), newSucessImageName);
			if (saveSuccessImageFlag) {
				badge.setSuccessImageName(newSucessImageName + "." + successImageType);
				updateFlag = true;
			} else {
				badge.setSuccessImageName(null);
				updateFlag = true;
			}
			if (currentSuccessImageName != null && !"".equals(currentSuccessImageName)) {
				commonService.deleteImages("Admin", "badge", currentSuccessImageName);
				updateFlag = true;
			}
		}
		if ("Y".equalsIgnoreCase(badgeDTO.getAudioFileUpdate())) {
			String audioFileName = badge.getBadgeId() + "_" + System.currentTimeMillis();
			String audioFileType = badgeDTO.getAudioFileTypeExt();
			boolean saveAudioFileFlag = commonService.saveAudioFile("Admin", "badge", audioFileType,
					badgeDTO.getBase64AudioFile(), audioFileName);
			if (saveAudioFileFlag) {
				badge.setAudioFileName(audioFileName + "." + audioFileType);
				updateFlag = true;
			} else {
				badge.setAudioFileName(null);
				updateFlag = true;
			}
			if (currentAudioFileName != null && !"".equals(currentAudioFileName)) {
				commonService.deleteAudios("Admin", "badge", currentAudioFileName);
				updateFlag = true;
			}
		}
		if (updateFlag) {
			badge = badgeService.update(badge);
		}
		badge.setRecordInUse(RecordInUseType.Y);
		return badge;
	}

	private Badge saveImageAndAudio(BadgePageData badgeDTO) {
		Badge badge = modelMapper.map(badgeDTO, Badge.class);
		try {
			badge = badgeService.create(badge);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getMessage());
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		boolean updateFlag = false;
		if ("Y".equalsIgnoreCase(badgeDTO.getImageUpdate())) {
			String newImageName = badge.getBadgeId() + "_IMG_" + System.currentTimeMillis();
			String imageType = badgeDTO.getImageTypeExt();
			String currentImageName = badge.getImageName();
			boolean saveImageFlag = commonService.saveImages("Admin", "badge", imageType, badgeDTO.getBase64Image(),
					newImageName);
			if (saveImageFlag) {
				badge.setImageName(newImageName + "." + imageType);
				updateFlag = true;
			} else {
				badge.setImageName(null);
				updateFlag = true;
			}
			if (currentImageName != null && !"".equals(currentImageName)) {
				commonService.deleteImages("Admin", "badge", currentImageName);
				updateFlag = true;
			}
		}
		if ("Y".equalsIgnoreCase(badgeDTO.getSuccessImageUpdate())) {
			String successImageType = badgeDTO.getSuccessImageTypeExt();
			String newSucessImageName = badge.getBadgeId() + "_SI_" + System.currentTimeMillis();
			String currentSuccessImageName = badge.getSuccessImageName();
			boolean saveSuccessImageFlag = commonService.saveImages("Admin", "badge", successImageType,
					badgeDTO.getBase64SuccessImage(), newSucessImageName);
			if (saveSuccessImageFlag) {
				badge.setSuccessImageName(newSucessImageName + "." + successImageType);
				updateFlag = true;
			} else {
				badge.setSuccessImageName(null);
				updateFlag = true;
			}
			if (currentSuccessImageName != null && !"".equals(currentSuccessImageName)) {
				commonService.deleteImages("Admin", "badge", currentSuccessImageName);
				updateFlag = true;
			}
		}
		if ("Y".equalsIgnoreCase(badgeDTO.getAudioFileUpdate())) {
			String audioFileName = badge.getBadgeId() + "_" + System.currentTimeMillis();
			String audioFileType = badgeDTO.getAudioFileTypeExt();
			String currentAudioFileName = badge.getAudioFileName();
			boolean saveAudioFileFlag = commonService.saveAudioFile("Admin", "badge", audioFileType,
					badgeDTO.getBase64AudioFile(), audioFileName);
			if (saveAudioFileFlag) {
				badge.setAudioFileName(audioFileName + "." + audioFileType);
				updateFlag = true;
			} else {
				badge.setAudioFileName(null);
				updateFlag = true;
			}
			if (currentAudioFileName != null && !"".equals(currentAudioFileName)) {
				commonService.deleteAudios("Admin", "badge", currentAudioFileName);
				updateFlag = true;
			}
		}
		if (updateFlag) {
			badge = badgeService.update(badge);
		}
		badge.setRecordInUse(RecordInUseType.Y);
		return badge;
	}
}