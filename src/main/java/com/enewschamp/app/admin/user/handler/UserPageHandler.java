package com.enewschamp.app.admin.user.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
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
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.entity.User;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("UserPageHandler")
@Slf4j
public class UserPageHandler implements IPageHandler {
	@Autowired
	private UserService userService;
	@Autowired
	private CommonService commonService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createUser(pageRequest);
			break;
		case "Update":
			pageDto = updateUser(pageRequest);
			break;
		case "Read":
			pageDto = readUser(pageRequest);
			break;
		case "Close":
			pageDto = closeUser(pageRequest);
			break;
		case "Reinstate":
			pageDto = reInstateUser(pageRequest);
			break;
		case "List":
			pageDto = listUser(pageRequest);
			break;
		case "ActiveStatus":
			pageDto = activateUserStatus(pageRequest);
			break;
		case "ResetPassword":
			pageDto = resetPassword(pageRequest);
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
	private PageDTO createUser(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserPageData.class);
		validate(pageData, this.getClass().getName());
		if(!pageData.getPassword().equals(pageData.getConfirmPassword())) {
			throw new BusinessException(ErrorCodeConstants.PASSWORD_MISMATCH);
		}
		User user = saveImage(pageData);
		mapUser(pageRequest, pageDto, user);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateUser(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserPageData.class);
		validate(pageData, this.getClass().getName());
		User user = updateImage(pageData, pageData.getUserId());
		mapUser(pageRequest, pageDto, user);
		return pageDto;
	}

	private void mapUser(PageRequestDTO pageRequest, PageDTO pageDto, User user) {
		UserPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(user);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readUser(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserPageData.class);
		User user = modelMapper.map(pageData, User.class);
		user = userService.read(user);
		mapUser(pageRequest, pageDto, user);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reInstateUser(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserPageData.class);
		User user = modelMapper.map(pageData, User.class);
		user = userService.reInstate(user);
		mapUser(pageRequest, pageDto, user);
		return pageDto;
	}

	private UserPageData mapPageData(User user) {
		UserPageData pageData = modelMapper.map(user, UserPageData.class);
		pageData.setPassword(null);
		pageData.setPassword1(null);
		pageData.setPassword2(null);
		pageData.setLastUpdate(user.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeUser(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserPageData.class);
		User user = modelMapper.map(pageData, User.class);
		user = userService.close(user);
		mapUser(pageRequest, pageDto, user);
		return pageDto;
	}
	
	@SneakyThrows
	private PageDTO activateUserStatus(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserPageData.class);
		User user = modelMapper.map(pageData, User.class);
		user = userService.activate(user);
		mapUser(pageRequest, pageDto, user);
		return pageDto;
	}
	
	@SneakyThrows
	private PageDTO resetPassword(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserPageData.class);
		if(!pageData.getPassword().equals(pageData.getConfirmPassword())) {
			throw new BusinessException(ErrorCodeConstants.PASSWORD_MISMATCH);
		}
		User user = modelMapper.map(pageData, User.class);
		user = userService.resetPassword(user);
		mapUser(pageRequest, pageDto, user);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listUser(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<User> userList = userService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<UserPageData> list = mapUserData(userList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((userList.getNumber() + 1) == userList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<UserPageData> mapUserData(Page<User> page) {
		List<UserPageData> userPageDataList = new ArrayList<UserPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<User> pageDataList = page.getContent();
			for (User user : pageDataList) {
				UserPageData userPageData = modelMapper.map(user, UserPageData.class);
				userPageData.setLastUpdate(user.getOperationDateTime());
				userPageData.setPassword(null);
				userPageData.setPassword1(null);
				userPageData.setPassword2(null);
				userPageDataList.add(userPageData);
			}
		}
		return userPageDataList;
	}
	
	

	private User saveImage(UserPageData userDTO) {
		User user = modelMapper.map(userDTO, User.class);
		try {
			user.setRecordInUse(RecordInUseType.Y);
			user = userService.create(user);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getMessage());
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		boolean updateFlag = false;
		if ("Y".equalsIgnoreCase(userDTO.getImageUpdate())) {
			String newImageName = user.getUserId() + "_IMG_" + System.currentTimeMillis();
			String imageType = userDTO.getImageTypeExt();
			String currentImageName = user.getImageName();
			boolean saveImageFlag = commonService.saveImages("Admin", "user", imageType, userDTO.getImageBase64(),
					newImageName);
			if (saveImageFlag) {
				user.setImageName(newImageName + "." + imageType);
				updateFlag = true;
			} else {
				user.setImageName(null);
				updateFlag = true;
			}
			if (currentImageName != null && !"".equals(currentImageName)) {
				commonService.deleteImages("Admin", "user", currentImageName);
				updateFlag = true;
			}
		}

		if (updateFlag) {
			user = userService.update(user);
		}
		user.setImageBase64(null);
		return user;
	}

	private User updateImage(UserPageData userDto, String userId) {
		userDto.setUserId(userId);
		User user = userService.get(userId);
		if (user.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		String currentImageName = user.getImageName();
		userDto.setImageName(currentImageName);
		LocalDateTime lastSuccessfulLoginAttempt = user.getLastSuccessfulLoginAttempt();
		LocalDateTime lastUnSuccessfulLoginAttempt = user.getLastUnsuccessfulLoginAttempt();
		String isActive = user.getIsActive();
		String isAccountLocked = user.getIsAccountLocked();
		String forcePasswordChange = user.getForcePasswordChange();
		user = modelMapper.map(userDto, User.class);
		
		user.setRecordInUse(RecordInUseType.Y);
		user.setLastSuccessfulLoginAttempt(lastSuccessfulLoginAttempt);
		user.setLastUnsuccessfulLoginAttempt(lastUnSuccessfulLoginAttempt);
		user.setIsActive(isActive);
		user.setIsAccountLocked(isAccountLocked);
		user.setForcePasswordChange(forcePasswordChange);
		
		user = userService.update(user);
		boolean updateFlag = false;
		if ("Y".equalsIgnoreCase(userDto.getImageUpdate())) {
			String newImageName = user.getUserId() + "_IMG_" + System.currentTimeMillis();
			String imageType = userDto.getImageTypeExt();
			boolean saveImageFlag = commonService.saveImages("Admin", "user", imageType, userDto.getImageBase64(),
					newImageName);
			if (saveImageFlag) {
				user.setImageName(newImageName + "." + imageType);
				updateFlag = true;
			} else {
				user.setImageName(null);
				updateFlag = true;
			}
			if (currentImageName != null && !"".equals(currentImageName)) {
				commonService.deleteImages("Admin", "user", currentImageName);
				updateFlag = true;
			}
		}

		if (updateFlag) {
			user = userService.update(user);
		}
		user.setImageBase64(null);
		return user;
	}
	
	

}