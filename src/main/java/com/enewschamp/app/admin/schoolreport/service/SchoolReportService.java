package com.enewschamp.app.admin.schoolreport.service;

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
import com.enewschamp.app.admin.schoolreport.entity.SchoolReport;
import com.enewschamp.app.admin.schoolreport.repository.SchoolReportRepository;
import com.enewschamp.app.admin.schoolreport.repository.SchoolReportRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class SchoolReportService {

	@Autowired
	private SchoolReportRepository repository;

	@Autowired
	private SchoolReportRepositoryCustomImpl repositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public SchoolReport create(SchoolReport schoolReportEntity) {
		SchoolReport schoolReport = null;
		try {
			schoolReport = repository.save(schoolReportEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return schoolReport;
	}

	public SchoolReport update(SchoolReport schoolReportEntity) {
		Long schoolReportId = schoolReportEntity.getSchoolReportId();
		SchoolReport existingSchoolReport = get(schoolReportId);
		if (existingSchoolReport.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(schoolReportEntity, existingSchoolReport);
		return repository.save(existingSchoolReport);
	}

	public SchoolReport get(Long schoolReportId) {
		Optional<SchoolReport> existingEntity = repository.findById(schoolReportId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.SCHOOL_REPORTS_NOT_FOUND);
		}
	}

	public SchoolReport read(SchoolReport schoolReportEntity) {
		Long schoolReportId = schoolReportEntity.getSchoolReportId();
		SchoolReport schoolReport = get(schoolReportId);
		return schoolReport;
	}

	public SchoolReport close(SchoolReport schoolReportEntity) {
		Long schoolReportId = schoolReportEntity.getSchoolReportId();
		SchoolReport existingSchoolReport = get(schoolReportId);
		if (existingSchoolReport.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingSchoolReport.setRecordInUse(RecordInUseType.N);
		existingSchoolReport.setOperationDateTime(null);
		return repository.save(existingSchoolReport);
	}

	public SchoolReport reInstate(SchoolReport schoolReportEntity) {
		Long schoolReportId = schoolReportEntity.getSchoolReportId();
		SchoolReport existingSchoolReport = get(schoolReportId);
		if (existingSchoolReport.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingSchoolReport.setRecordInUse(RecordInUseType.Y);
		existingSchoolReport.setOperationDateTime(null);
		return repository.save(existingSchoolReport);
	}

	public Page<SchoolReport> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<SchoolReport> schoolReportList = repositoryCustom.findAll(pageable, searchRequest);
		return schoolReportList;
	}

}