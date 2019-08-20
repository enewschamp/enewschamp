package com.enewschamp.app.student.badges.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.enewschamp.app.student.badges.dto.StudentBadgesDTO;
import com.enewschamp.app.student.badges.entity.StudentBadges;
import com.enewschamp.app.student.badges.service.StudentBadgesService;
import com.enewschamp.app.student.monthlytrends.dto.TrendsMonthlyTotalDTO;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.master.badge.entity.Badge;
import com.enewschamp.master.badge.service.BadgeService;

@Service
public class StudentBadgesBusiness {

	@Autowired
	StudentBadgesService studentBadgesService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	BadgeService bagdeService;
	
	
	public StudentBadgesDTO saveAcvitity(StudentBadgesDTO studentBadgesDTO)
	{
		StudentBadges  studentBadges  = modelMapper.map(studentBadgesDTO, StudentBadges.class);
		studentBadges = studentBadgesService.create(studentBadges);
		StudentBadgesDTO studentBadgesDTONew = modelMapper.map(studentBadges, StudentBadgesDTO.class);
		return studentBadgesDTONew;
	}
	
	public StudentBadgesDTO getDailyTrend(Long studentId, String editionId, Long monthYear)
	{
		StudentBadges  studentBadges = studentBadgesService.getStudentBadges(studentId, editionId, monthYear);
		StudentBadgesDTO studentActivityDTO = modelMapper.map(studentBadges, StudentBadgesDTO.class);
		return studentActivityDTO;
	}
	
	public StudentBadges grantBadge(Long studentId, String editionId,Long monthYear, String genreId,TrendsMonthlyTotalDTO trendsMonthlyTotalDTO  )
	{
System.out.println("Monthly points in grantBadge()"+ trendsMonthlyTotalDTO.getQuizQCorrect());
		Badge badge =  bagdeService.getBadgeForStudent(editionId, genreId,trendsMonthlyTotalDTO.getQuizQCorrect() );
		StudentBadges studBadge=null;
		if(badge!=null) {
		 studBadge = new StudentBadges();
		studBadge.setStudentId(studentId);	
		studBadge.setBadgeId(badge.getBadgeId());
		studBadge.setEditionId(editionId);
		studBadge.setYearMonth(monthYear);
		studBadge.setOperatorId("SYSTEM");
		studBadge.setRecordInUse(RecordInUseType.Y);
		studBadge.setBadgeImage(badge.getImage());
		
		 studentBadgesService.create(studBadge);
		}
		return studBadge;
		
	}
	
	public Page<StudentBadges> getStudentBadges(Long studentId, String editionId, int pageNo)
	{
		Page<StudentBadges> studentBadges = studentBadgesService.getStudetbadges(studentId, editionId, pageNo);
		return studentBadges;
	}
}
