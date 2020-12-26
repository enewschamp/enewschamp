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
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;
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
		pageData.setNameId(pageRequest.getData().get("name").asText());
		validate(pageData);
		Avatar avatar = mapAvatarData(pageRequest, pageData);
		//avatar.setNameId(pageData.getNameId());
		avatar = avatarService.create(avatar);
		mapHeaderData(pageRequest, pageDto, pageData, avatar);
		pageData.setAvatarId(avatar.getAvatarId());
		pageData.setLastUpdate(avatar.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto, AvatarPageData pageData, Avatar avatar) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
	}

	private Avatar mapAvatarData(PageRequestDTO pageRequest, AvatarPageData pageData) {
		Avatar avatar = modelMapper.map(pageData, Avatar.class);
		avatar.setOperatorId(pageRequest.getData().get("operator").asText());
		avatar.setRecordInUse(RecordInUseType.Y);
		return avatar;
	}

	@SneakyThrows
	private PageDTO updateAvatar(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AvatarPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), AvatarPageData.class);
		validate(pageData);
		Avatar avatar = mapAvatarData(pageRequest, pageData);
		avatar = avatarService.update(avatar);
		mapHeaderData(pageRequest, pageDto, pageData, avatar);
		pageData.setAvatarId(avatar.getAvatarId());
		pageData.setLastUpdate(avatar.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readAvatar(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AvatarPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), AvatarPageData.class);
		Avatar avatar = modelMapper.map(pageData, Avatar.class);
		avatar.setAvatarId(pageRequest.getData().get("id").asLong());
		avatar = avatarService.read(avatar);
		mapHeaderData(pageRequest, pageDto, pageData, avatar);
		mapPageData(pageData, avatar);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapPageData(AvatarPageData pageData, Avatar avatar) {
		pageData.setReadingLevel(avatar.getReadingLevel());
		pageData.setImageName(avatar.getImageName());
		pageData.setGender(avatar.getGender());
		//pageData.setRecordInUse(avatar.getRecordInUse().toString());
		//pageData.setOperator(avatar.getOperatorId());
		pageData.setLastUpdate(avatar.getOperationDateTime());
	}

	@SneakyThrows
	private PageDTO closeAvatar(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AvatarPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), AvatarPageData.class);
		Avatar avatar = modelMapper.map(pageData, Avatar.class);
		avatar.setAvatarId(pageData.getId());
		avatar = avatarService.close(avatar);
		mapHeaderData(pageRequest, pageDto, pageData, avatar);
		mapPageData(pageData, avatar);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listAvatar(PageRequestDTO pageRequest) {
		Page<Avatar> avatarList = avatarService.list(pageRequest.getData().get("pagination").get("pageNumber").asInt(),
				pageRequest.getData().get("pagination").get("pageSize").asInt());

		List<AvatarPageData> list = mapAvatarData(avatarList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		dto.setHeader(pageRequest.getHeader());
		if ((avatarList.getNumber() + 1) == avatarList.getTotalPages()) {
			pageData.getPagination().setLastPage(true);
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
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				AvatarPageData avatarPageData = modelMapper.map(avatar, AvatarPageData.class);
				avatarPageDataList.add(avatarPageData);
			}
		}
		return avatarPageDataList;
	}

	private void validate(AvatarPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<AvatarPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
