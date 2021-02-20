package com.enewschamp.app.admin.avatar.handler;

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
import org.springframework.beans.factory.annotation.Autowired;
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
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Avatar;
import com.enewschamp.publication.domain.service.AvatarService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("AvatarPageHandler")
@Slf4j
public class AvatarPageHandler implements IPageHandler {
	@Autowired
	private AvatarService avatarService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private CommonService commonService;
	
	private Validator validator;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createAvatar(pageRequest);
			break;
		case "Update":
			pageDto = updateAvatar(pageRequest);
			break;
		case "Read":
			pageDto = readAvatar(pageRequest);
			break;
		case "Close":
			pageDto = closeAvatar(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateAvatar(pageRequest);
			break;
		case "List":
			pageDto = listAvatar(pageRequest);
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
	private PageDTO createAvatar(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AvatarPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), AvatarPageData.class);
		validate(pageData);
		Avatar avatar = mapAvatarData(pageRequest, pageData);
		avatar = avatarService.create(avatar);
		String newImageName = avatar.getAvatarId() + "_" + System.currentTimeMillis();
		String imageType = pageData.getImageTypeExt();
		boolean saveFlag = commonService.saveImages("Admin", "avatar", imageType, pageData.getBase64Image(),
				newImageName, avatar.getImageName());
		if (saveFlag) {
			avatar.setImageName(newImageName + "." + imageType);
			avatar = avatarService.update(avatar);
		}
        mapAvatar(pageRequest, pageDto, avatar);
		return pageDto;
	}


	@SneakyThrows
	private PageDTO updateAvatar(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AvatarPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), AvatarPageData.class);
		validate(pageData);
		Avatar avatar = mapAvatarData(pageRequest, pageData);
		avatar = avatarService.update(avatar);
		mapAvatar(pageRequest, pageDto, avatar);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readAvatar(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AvatarPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), AvatarPageData.class);
		Avatar avatar = modelMapper.map(pageData, Avatar.class);
		avatar = avatarService.read(avatar);
		mapAvatar(pageRequest, pageDto, avatar);
		return pageDto;
	}

	private AvatarPageData mapPageData(Avatar avatar) {
		AvatarPageData pageData = modelMapper.map(avatar, AvatarPageData.class);
		pageData.setLastUpdate(avatar.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeAvatar(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AvatarPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), AvatarPageData.class);
		Avatar avatar = modelMapper.map(pageData, Avatar.class);
		avatar = avatarService.close(avatar);
		mapAvatar(pageRequest, pageDto, avatar);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateAvatar(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AvatarPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), AvatarPageData.class);
		Avatar avatar = modelMapper.map(pageData, Avatar.class);
		avatar = avatarService.reinstate(avatar);
		mapAvatar(pageRequest, pageDto, avatar);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listAvatar(PageRequestDTO pageRequest) {
		Page<Avatar> avatarList = avatarService.list(
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<AvatarPageData> list = mapAvatarData(avatarList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((avatarList.getNumber() + 1) == avatarList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<AvatarPageData> mapAvatarData(Page<Avatar> page) {
		List<AvatarPageData> avatarPageDataList = new ArrayList<AvatarPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<Avatar> pageDataList = page.getContent();
			for (Avatar avatar : pageDataList) {
				AvatarPageData avatarPageData = modelMapper.map(avatar, AvatarPageData.class);
				avatarPageData.setLastUpdate(avatar.getOperationDateTime());
				avatarPageDataList.add(avatarPageData);
			}
		}
		return avatarPageDataList;
	}
	
	private void mapAvatar(PageRequestDTO pageRequest, PageDTO pageDto, Avatar avatar) {
		AvatarPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(avatar);
		pageDto.setData(pageData);
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
		pageDto.getHeader().setUserId(null);
		pageDto.getHeader().setDeviceId(null);
	}

	private Avatar mapAvatarData(PageRequestDTO pageRequest, AvatarPageData pageData) {
		Avatar avatar = modelMapper.map(pageData, Avatar.class);
		avatar.setRecordInUse(RecordInUseType.Y);
		return avatar;
	}


	private void validate(AvatarPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<AvatarPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error("Validation failed: " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
