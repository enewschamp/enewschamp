package com.enewschamp.user.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.dashboard.handler.UserView;
import com.enewschamp.app.admin.user.repository.UserRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.user.login.entity.UserAction;
import com.enewschamp.app.user.login.entity.UserActivityTracker;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.entity.User;

@Service
public class UserService extends AbstractDomainService {

	@Autowired
	UserRepository repository;

	@Autowired
	UserRepositoryCustomImpl customRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	PropertiesBackendService propertiesService;

	public User create(User user) {
		User userEntity = null;
		try {
			if (user.getCreationDateTime() == null) {
				user.setCreationDateTime(LocalDateTime.now());
			}
			userEntity = repository.save(user);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return userEntity;
	}

	public User update(User user) {
		String userId = user.getUserId();
		User existingUser = load(userId);
		if (existingUser.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		handlePasswords(user, existingUser);
		return repository.save(existingUser);
	}

	private void handlePasswords(User user, User existingUser) {
		if (StringUtils.isEmpty(user.getPassword()))
			user.setPassword(existingUser.getPassword());
		if (StringUtils.isEmpty(user.getPassword1()))
			user.setPassword1(existingUser.getPassword1());
		if (StringUtils.isEmpty(user.getPassword2()))
			user.setPassword2(existingUser.getPassword2());
		modelMapper.map(user, existingUser);
	}

	public User patch(User user) {
		String userId = user.getUserId();
		User existingEntity = load(userId);
		modelMapperForPatch.map(user, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(String userId) {
		repository.deleteById(userId);
	}

	public User load(String userId) {
		Optional<User> existingEntity = repository.findById(userId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.USER_NOT_FOUND, userId);
		}
	}

	public User get(String userId) {
		if (userId == null || "".equals(userId.trim())) {
			return null;
		}
		Optional<User> existingEntity = repository.findById(userId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		}
		return null;
	}

	public List<ListOfValuesItem> getPublisherLOV() {
		return toListOfValuesItems(repository.getPublisherLOV());
	}

	public List<ListOfValuesItem> getAuthorLOV() {
		return toListOfValuesItems(repository.getAuthorLOV());
	}

	public List<ListOfValuesItem> getEditorLOV() {
		return toListOfValuesItems(repository.getEditorLOV());
	}

	public String getAudit(String userId) {
		User user = new User();
		user.setUserId(userId);
		return auditService.getEntityAudit(user);
	}

	public boolean validateUser(String userId) {
		return get(userId) != null ? true : false;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean validatePassword(final String userId, final String password,
			UserActivityTracker userActivityTracker) {
		User user = get(userId);
		if (user == null) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.INVALID_USER_ID, userId);
		}
		if (!user.getIsActive().equalsIgnoreCase(RecordInUseType.Y.toString())) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.USER_IS_INACTIVE, userId);
		}
		if ("Y".equals(user.getIsAccountLocked()) && (user.getLastUnsuccessfulLoginAttempt() != null)) {
			LocalDate lastAttemptDate = user.getLastUnsuccessfulLoginAttempt().toLocalDate();
			LocalDate todayDate = LocalDateTime.now().toLocalDate();
			if (lastAttemptDate.isBefore(todayDate)) {
				user.setIsAccountLocked("N");
				user.setIncorrectLoginAttempts(0);
			}
		}
		if ("Y".equals(user.getIsAccountLocked())) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.USER_ACCOUNT_LOCKED, userId);
		}
		boolean isValid = false;
		if (!user.getPassword().equals(password)) {
			isValid = false;
			user.setLastUnsuccessfulLoginAttempt(LocalDateTime.now());
			user.setIncorrectLoginAttempts(user.getIncorrectLoginAttempts() + 1);
			if (user.getIncorrectLoginAttempts() == Integer
					.valueOf(propertiesService.getValue("Publisher", PropertyConstants.PUBLISHER_LOGIN_MAX_ATTEMPTS))) {
				user.setIsAccountLocked("Y");
			}
			repository.save(user);
		} else {
			user.setLastSuccessfulLoginAttempt(LocalDateTime.now());
			user.setLastUnsuccessfulLoginAttempt(null);
			user.setIncorrectLoginAttempts(0);
			isValid = true;
		}
		return isValid;
	}

	public void changePassword(final String userId, final String password, UserActivityTracker userActivityTracker) {
		User user = get(userId);
		if (!user.getRecordInUse().equals(RecordInUseType.Y)) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.USER_IS_INACTIVE, userId);
		}
		if (password.equals(user.getPassword()) || password.equals(user.getPassword1())
				|| password.equals(user.getPassword2())) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.USER_PASSWORD_SAME_OR_PREV_TWO, userId);
		}
		user.setPassword2(user.getPassword1());
		user.setPassword1(user.getPassword());
		user.setPassword(password);
		repository.save(user);
	}

	public void resetPassword(final String userId, final String password, UserActivityTracker userActivityTracker) {
		User user = get(userId);
		if (!user.getRecordInUse().equals(RecordInUseType.Y)) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.USER_IS_INACTIVE, userId);
		}
		user.setPassword(password);
		user.setIncorrectLoginAttempts(0);
		user.setLastUnsuccessfulLoginAttempt(null);
		user.setIsAccountLocked("N");
		repository.save(user);
	}

	public void changeTheme(final String userId, final String theme, UserActivityTracker userActivityTracker) {
		User user = get(userId);
		if (!user.getRecordInUse().equals(RecordInUseType.Y)) {
			userActivityTracker.setActionStatus(UserAction.FAILURE);
			userLoginBusiness.auditUserActivity(userActivityTracker);
			throw new BusinessException(ErrorCodeConstants.USER_IS_INACTIVE, userId);
		}
		user.setTheme(theme);
		repository.save(user);
	}

	public User read(User userEntity) {
		String userId = userEntity.getUserId();
		User user = get(userId);
		return user;
	}

	public User close(User userEntity) {
		String userId = userEntity.getUserId();
		User existingUser = get(userId);
		if (existingUser.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingUser.setRecordInUse(RecordInUseType.N);
		existingUser.setOperationDateTime(null);
		return repository.save(existingUser);
	}

	public User reInstate(User userEntity) {
		String UserId = userEntity.getUserId();
		User existingUser = get(UserId);
		if (existingUser.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingUser.setRecordInUse(RecordInUseType.Y);
		existingUser.setOperationDateTime(null);
		return repository.save(existingUser);
	}

	public Page<User> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<User> userList = customRepository.findAll(pageable, searchRequest);
		if (userList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return userList;
	}
	
	public List<UserView> getAllUserView(){
		return repository.findAllProjectedBy();
	}
}
