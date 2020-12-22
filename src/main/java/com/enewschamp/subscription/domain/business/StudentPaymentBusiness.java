package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.subscription.app.dto.StudentPaymentDTO;
import com.enewschamp.subscription.app.dto.StudentPaymentWorkDTO;
import com.enewschamp.subscription.domain.entity.StudentPayment;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.service.StudentPaymentService;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;

@Service
public class StudentPaymentBusiness {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StudentPaymentService studentPaymentService;

	@Autowired
	StudentPaymentWorkService studentPaymentWorkService;

	public void saveAsMaster(StudentPaymentDTO studentPaymentPageDTO) {
		StudentPayment studentPayment = modelMapper.map(studentPaymentPageDTO, StudentPayment.class);
		studentPayment.setRecordInUse(RecordInUseType.Y);
		studentPayment.setOperatorId("SYSTEM");
		studentPaymentService.create(studentPayment);
	}

	public void saveAsWork(StudentPaymentWorkDTO studentPaymentWorkDTO) {
		StudentPaymentWork studentPref = modelMapper.map(studentPaymentWorkDTO, StudentPaymentWork.class);
		studentPaymentWorkService.create(studentPref);
	}

	public void workToMaster(Long studentId, String editionId) {
		StudentPaymentWork workEntity = studentPaymentWorkService.getByStudentIdAndEdition(studentId, editionId);
		if (workEntity != null) {
			StudentPayment masterEntity = modelMapper.map(workEntity, StudentPayment.class);
			masterEntity.setOperatorId("SYSTEM");
			masterEntity.setRecordInUse(RecordInUseType.Y);
			studentPaymentService.create(masterEntity);
		}
	}

}
