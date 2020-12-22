package com.enewschamp.app.champs.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.champs.page.data.ChampsPageData;
import com.enewschamp.app.champs.page.data.ChampsSearchData;
import com.enewschamp.app.common.CommonFilterData;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PaginationData;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.dto.ChampStudentDTO;
import com.enewschamp.app.student.service.StudentChampService;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "ChampsPageHandler")
public class ChampsPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentChampService studentChampService;

	@Autowired
	StudentControlBusiness studentControlBusiness;
	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Autowired
	CommonService commonService;

	@Autowired
	private PropertiesService propertiesService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		String methodName = pageNavigationContext.getLoadMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[1];
			params[0] = PageNavigationContext.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			try {
				return (PageDTO) m.invoke(this, pageNavigationContext);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		return pageDTO;
	}

	public PageDTO loadChampsPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		ChampsPageData pageData = new ChampsPageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
		int pageNo = 1;
		int pageSize = Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		if (pageData.getPagination() != null) {
			pageNo = pageData.getPagination().getPageNumber() > 0 ? pageData.getPagination().getPageNumber() : 1;
			pageSize = pageData.getPagination().getPageSize() > 0 ? pageData.getPagination().getPageSize()
					: Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		} else {
			PaginationData paginationData = new PaginationData();
			paginationData.setPageNumber(pageNo);
			paginationData.setPageSize(pageSize);
			pageData.setPagination(paginationData);
		}
		ChampsSearchData champsSearchData = new ChampsSearchData();
		champsSearchData.setLimitDate(commonService.getLimitDate(PropertyConstants.VIEW_RECOGNITIONS_LIMIT, emailId));
		CommonFilterData filterData = pageData.getFilter();
		if (filterData != null) {
			if (filterData.getMonth() == null || "".equals(filterData.getMonth())) {
				LocalDate currdate = LocalDate.now();
				int month = currdate.getMonthValue();
				String monthStr = (month < 10) ? "0" + month : "" + month;
				int year = currdate.getYear();
				String newYearMonth = year + monthStr;
				filterData.setMonth(newYearMonth);
			}
			champsSearchData.setMonth(filterData.getMonth());
			champsSearchData.setReadingLevel(filterData.getReadingLevel());
		} else {
			champsSearchData.setReadingLevel("3");
		}
		List<ChampStudentDTO> champList = studentChampService.findChampStudents(champsSearchData, pageNo, pageSize);
		if (champList == null || champList.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setChamps(champList);
		pageData.setFilter(filterData);
		pageDto.setData(pageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}

	private ChampsPageData mapPageData(ChampsPageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), ChampsPageData.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return pageData;
	}

}
