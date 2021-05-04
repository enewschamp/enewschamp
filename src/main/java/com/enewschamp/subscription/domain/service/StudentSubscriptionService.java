package com.enewschamp.subscription.domain.service;

import java.time.LocalDate;
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

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.student.subscription.repository.StudentSubscriptionRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionHistory;

@Service
public class StudentSubscriptionService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	StudentSubscriptionRepository repository;

	@Autowired
	StudentSubscriptionRepositoryCustomImpl repositoryCustom;

	@Autowired
	StudentSubscriptionHistoryService studentSubscriptionHistoryService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentSubscription create(StudentSubscription studentSubscription) {
		StudentSubscription studentSubscriptionEntity = null;
		Optional<StudentSubscription> existingStudentSubscription = repository
				.findById(studentSubscription.getStudentId());
		if (existingStudentSubscription.isPresent()) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		try {
			studentSubscriptionEntity = repository.save(studentSubscription);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return studentSubscriptionEntity;

	}

	public StudentSubscription update(StudentSubscription studentSubscription) {
		Long studentId = studentSubscription.getStudentId();
		String editionId = studentSubscription.getEditionId();
		StudentSubscription existingEntity = get(studentId, editionId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		StudentSubscriptionHistory studentSubscriptionHistory = modelMapper.map(existingEntity,
				StudentSubscriptionHistory.class);
		studentSubscriptionHistoryService.create(studentSubscriptionHistory);
		modelMapper.map(studentSubscription, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSubscription patch(StudentSubscription studentSubscription) {
		Long studentId = studentSubscription.getStudentId();
		String editionId = studentSubscription.getEditionId();

		StudentSubscription existingEntity = get(studentId, editionId);
		modelMapperForPatch.map(studentSubscription, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSubscription get(Long studentId, String editionId) {
		Optional<StudentSubscription> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public List<StudentSubscription> getSubscriptionRenewalList(LocalDate endDate) {
		return repository.getSubscriptionRenewalList(endDate);
	}

	public String getAudit(Long studentId) {
		StudentSubscription StudentDetails = new StudentSubscription();
		StudentDetails.setStudentId(studentId);
		return auditService.getEntityAudit(StudentDetails);
	}

	public StudentSubscription read(StudentSubscription studentSubscription) {
		Long studentsubscriptionId = studentSubscription.getStudentId();
		StudentSubscription studentSubscriptionEntity = get(studentsubscriptionId, null);
		return studentSubscriptionEntity;
	}

	public StudentSubscription close(StudentSubscription studentSubscriptionEntity) {
		Long studentsubscriptionId = studentSubscriptionEntity.getStudentId();
		StudentSubscription existingEntity = get(studentsubscriptionId, null);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public StudentSubscription reinstate(StudentSubscription studentSubscriptionEntity) {
		Long helpdeskId = studentSubscriptionEntity.getStudentId();
		StudentSubscription existingStudentSubscription = get(helpdeskId, null);
		if (existingStudentSubscription.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingStudentSubscription.setRecordInUse(RecordInUseType.Y);
		existingStudentSubscription.setOperationDateTime(null);
		return repository.save(existingStudentSubscription);
	}

	public Page<StudentSubscription> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentSubscription> studentSubscriptionList = repositoryCustom.findAll(pageable, searchRequest);
		return studentSubscriptionList;
	}

}