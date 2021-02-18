package com.enewschamp.app.admin.student.registration.bulk.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.domain.service.RepositoryImpl;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.entity.StudentSubscription;

@Repository
public class BulkStudentCustomImpl extends RepositoryImpl {
	@PersistenceContext
	private EntityManager entityManager;

	public List<BulkStudentRegistrationPageData2> findArticles() {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> q = cb.createQuery(Tuple.class);
		Root<StudentControl> studentControlRoot = q.from(StudentControl.class);
    	Root<StudentDetails> studentDetailsRoot = q.from(StudentDetails.class);
		Root<StudentSchool> studentSchoolRoot = q.from(StudentSchool.class);
		Root<StudentPreferences> studentPreferencesRoot = q.from(StudentPreferences.class);
		Root<StudentSubscription> studentSubscriptionRoot = q.from(StudentSubscription.class);
		Root<StudentRegistration> studentRegistrationRoot = q.from(StudentRegistration.class);
		Root<StudentActivity> studentActivityRoot = q.from(StudentActivity.class);
	//	Join<StudentRegistration,StudentActivity> j1 = sRoot.join("studentId");
		q.select(cb.tuple(studentRegistrationRoot, studentActivityRoot, studentSubscriptionRoot, studentPreferencesRoot,studentSchoolRoot, studentDetailsRoot, studentControlRoot )).where(
			     cb.equal(studentRegistrationRoot.get("studentId"), studentActivityRoot.get("studentId")),
			     cb.equal(studentRegistrationRoot.get("studentId"), studentSubscriptionRoot.get("studentId")),
			     cb.equal(studentRegistrationRoot.get("studentId"), studentPreferencesRoot.get("studentId")),
			     cb.equal(studentRegistrationRoot.get("studentId"), studentSchoolRoot.get("studentId")),
			     cb.equal(studentRegistrationRoot.get("studentId"), studentDetailsRoot.get("studentId")),
			     cb.equal(studentRegistrationRoot.get("studentId"), studentControlRoot.get("studentId"))
			     );
//		q.select(cb.tuple(studentControlRoot,studentDetailsRoot, studentSchoolRoot, studentPreferencesRoot,studentSubscriptionRoot, studentRegistrationRoot, studentActivityRoot)).where(
//			    cb.and(cb.equal(studentRegistrationRoot.get("studentId"), studentControlRoot.get("studentId")),
//			    	   cb.equal(studentRegistrationRoot.get("studentId"), studentDetailsRoot.get("studentId")),
//			    	   cb.equal(studentRegistrationRoot.get("studentId"), studentSchoolRoot.get("studentId")),
//			    	   cb.equal(studentRegistrationRoot.get("studentId"), studentPreferencesRoot.get("studentId")),
//			    	   cb.equal(studentRegistrationRoot.get("studentId"), studentSubscriptionRoot.get("studentId")),
//			    	   cb.equal(studentRegistrationRoot.get("studentId"), studentActivityRoot.get("studentId"))
//			    		));
//		q.select(cb.tuple(sRoot, sgRoot)).where(
//				cb.equal(sRoot.get("studentId"), sgRoot.get("studentId")));
		//q.multiselect(j1);
	//	List<Tuple> l = entityManager.createQuery(q).getResultList(); 
		List<Tuple> l = entityManager.createQuery(q).getResultList();
		List<BulkStudentRegistrationPageData2> bulkList = new ArrayList<BulkStudentRegistrationPageData2>();
		for (Tuple t : l) {
			BulkStudentRegistrationPageData2 pageData = new BulkStudentRegistrationPageData2();
			StudentRegistration s = (StudentRegistration) t.get(0);
			StudentActivity sg = (StudentActivity) t.get(1);
			StudentSubscription sc = (StudentSubscription) t.get(2);
			StudentPreferences sf = (StudentPreferences) t.get(3);
			StudentSchool ss = (StudentSchool) t.get(4);
			StudentDetails sd = (StudentDetails) t.get(5);
			StudentControl scs = (StudentControl) t.get(6);
			pageData.setStudentRegistration(s);
			pageData.setStudentSubscription(sc);
			pageData.setStudentControl(scs);
			pageData.setStudentSchool(ss);
			pageData.setStudentPreferences(sf);
			pageData.setStudentDetails(sd);
			bulkList.add(pageData);

		    System.out.println("Student Registration is : " + s);
		    System.out.println("Student Activity is : " + sg);
		    System.out.println("Student subscription is : " + sc);
		    System.out.println("Student preferences is : " + sf);
		    System.out.println("Student school is : " + ss);
		    System.out.println("Student Detail is : " + sd);
		    System.out.println("Student Controls is : " + scs);
		    
		}
		System.out.println("tuple list: "+l);
		return bulkList;
	}
	
	  public List<Object[]> findAllBy() {
	        String jpql = "SELECT " +
	            " sr, sa " +
	        " FROM StudentRegistration sr " +
	            " INNER JOIN StudentActivity sa";
	        Query query = entityManager.createQuery(jpql);
	           // " JOIN StudentActivity sa ";
//	            " JOIN EntityD ed " +
//	            " JOIN EntityE ee " +
//	            " JOIN EntityF ef " +
//	        " WHERE " +
//	            " TRUNC(ee.date) = TRUNC(:date) "
	    //    ;

//	        //conditions based on screen filter parameters
//	        if(amount!=null && amount>0L) {
//	            jpql += " AND ef.amount = :amount ";
//	        }
//	        if(name!=null && name.trim().length()>0) {
//	            jpql += " AND LOWER(ec.name) LIKE LOWER('%' || :name || '%') ";
//	        }
//	        if(projectId!=null && projectId>0L) {
//	            jpql += " AND ec.projectId = :projectId ";
//	        }
//	        if(divisionId!=null && divisionId>0L) {
//	            jpql += " AND ed.divisionId = :divisionId ";
//	        }

	       
	       // query.setParameter("date", filterDate);

//	        if(amount!=null && amount>0L) {
//	            query.setParameter("amount", amount);
//	        }
//	        if(name!=null && name.trim().length()>0) {
//	            query.setParameter("name", name);
//	        }
//	        if(projectId!=null && projectId>0L) {
//	            query.setParameter("projectId", projectId);
//	        }
//	        if(divisionId!=null && divisionId>0L) {
//	            query.setParameter("divisionId", divisionId);
//	        }
	        List<Object[]> list = query.getResultList();
	        System.out.println(list);
	        return query.getResultList();

	}

}