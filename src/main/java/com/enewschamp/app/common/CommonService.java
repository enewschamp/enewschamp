package com.enewschamp.app.common;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.enewschamp.app.article.page.dto.PublicationData;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.utils.SaveFileUtils;

@Service
public class CommonService {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private PropertiesBackendService propertiesService;

	@Autowired
	private StudentControlBusiness studentControlBusiness;

	public LocalDate getLimitDate(String module, String constantName, String emailId) {
		String unitType = "";
		int unitValue = 0;
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		String subscriptionType = "F";
		if (studentControlDTO != null) {
			subscriptionType = studentControlDTO.getSubscriptionType();
			if ("F".equals(subscriptionType) && (!"Y".equals(studentControlDTO.getEvalAvailed()))) {
				subscriptionType = "E";
			}
		}
		String viewLimit = propertiesService.getValue(module, constantName);
		String[] limitArr = viewLimit.split(",");
		for (int i = 0; i < limitArr.length; i++) {
			if (limitArr[i].startsWith(subscriptionType)) {
				String unitDetails = limitArr[i].split("\\|")[1];
				unitType = unitDetails.substring(unitDetails.length() - 1, unitDetails.length());
				unitValue = Integer.valueOf(unitDetails.substring(0, (unitDetails.length() - 1)));
				break;
			}
		}
		LocalDate date = LocalDate.now();
		if ("D".equalsIgnoreCase(unitType)) {
			date = date.minusDays(unitValue);
		} else if ("M".equalsIgnoreCase(unitType)) {
			date = date.minusMonths(unitValue);
		} else if ("Y".equalsIgnoreCase(unitType)) {
			date = date.minusYears(unitValue);
		}
		return date;
	}

	public CommonFilterData mapPageData(CommonFilterData pageData, PageRequestDTO pageRequest) {
		try {
			if (pageRequest.getData() != null) {
				pageData = modelMapper.map(pageRequest.getData(), CommonFilterData.class);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageData;
	}

	public List<PublicationData> mapStudentData(Page<NewsArticleSummaryDTO> page, Long studentId) {
		List<PublicationData> publicationPageDataList = new ArrayList<PublicationData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<NewsArticleSummaryDTO> pageDataList = page.getContent();
			for (NewsArticleSummaryDTO article : pageDataList) {
				PublicationData publicationData = new PublicationData();
				publicationData = modelMapper.map(article, PublicationData.class);
				publicationPageDataList.add(publicationData);
			}
		}
		return publicationPageDataList;
	}

	public boolean saveImages(String module, String entityType, String imageExtType, String base64Image,
			String newImageName) {
		if (base64Image == null || "".equalsIgnoreCase(base64Image)) {
			return false;
		}
		String imagesFolderPath = propertiesService.getValue(module, "imageRootFolderPath." + entityType);
		String size1FileNameWithoutExtension = imagesFolderPath + "size1/" + newImageName;
		String size2FileNameWithoutExtension = imagesFolderPath + "size2/" + newImageName;
		String size3FileNameWithoutExtension = imagesFolderPath + "size3/" + newImageName;
		String size4FileNameWithoutExtension = imagesFolderPath + "size4/" + newImageName;
		String outputFileName = imagesFolderPath + "temp-" + newImageName;
		File imageFile = null;
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
			imageFile = new File(outputFileName);
			FileUtils.writeByteArrayToFile(imageFile, decodedBytes);
			// Size 1
			List<String> size1Dimension = Arrays
					.asList(propertiesService.getValue(module, PropertyConstants.IMAGE_DIMENSION_SIZE1).split(","));
			SaveFileUtils.resizeImage(imageFile,
					new Dimension(Integer.valueOf(size1Dimension.get(0)), Integer.valueOf(size1Dimension.get(1))),
					imageExtType, size1FileNameWithoutExtension);
			// Size 2
			List<String> size2Dimension = Arrays
					.asList(propertiesService.getValue(module, PropertyConstants.IMAGE_DIMENSION_SIZE2).split(","));
			SaveFileUtils.resizeImage(imageFile,
					new Dimension(Integer.valueOf(size2Dimension.get(0)), Integer.valueOf(size2Dimension.get(1))),
					imageExtType, size2FileNameWithoutExtension);
			// Size 3
			List<String> size3Dimension = Arrays
					.asList(propertiesService.getValue(module, PropertyConstants.IMAGE_DIMENSION_SIZE3).split(","));
			SaveFileUtils.resizeImage(imageFile,
					new Dimension(Integer.valueOf(size3Dimension.get(0)), Integer.valueOf(size3Dimension.get(1))),
					imageExtType, size3FileNameWithoutExtension);
			// Size 4
			List<String> size4Dimension = Arrays
					.asList(propertiesService.getValue(module, PropertyConstants.IMAGE_DIMENSION_SIZE4).split(","));
			SaveFileUtils.resizeImage(imageFile,
					new Dimension(Integer.valueOf(size4Dimension.get(0)), Integer.valueOf(size4Dimension.get(1))),
					imageExtType, size4FileNameWithoutExtension);
			FileUtils.forceDelete(imageFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(ErrorCodeConstants.IMAGE_SAVE_ERROR);
		} finally {
			base64Image = null;
			imageFile = null;
		}
	}

	public boolean deleteImages(String module, String entityType, String currentImageName) {
		String imagesFolderPath = propertiesService.getValue(module, "imageRootFolderPath." + entityType);
		try {
			if (currentImageName != null && !"".equals(currentImageName)) {
				String size1OldFileName = imagesFolderPath + "size1/" + currentImageName;
				String size2OldFileName = imagesFolderPath + "size2/" + currentImageName;
				String size3OldFileName = imagesFolderPath + "size3/" + currentImageName;
				String size4OldFileName = imagesFolderPath + "size4/" + currentImageName;
				File oldFileJPG1 = new File(size1OldFileName);
				oldFileJPG1.delete();
				File oldFileJPG2 = new File(size2OldFileName);
				oldFileJPG2.delete();
				File oldFileJPG3 = new File(size3OldFileName);
				oldFileJPG3.delete();
				File oldFileJPG4 = new File(size4OldFileName);
				oldFileJPG4.delete();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(ErrorCodeConstants.IMAGE_SAVE_ERROR);
		} finally {
		}
	}

	public boolean deleteAudios(String module, String entityType, String currentImageName) {
		String imagesFolderPath = propertiesService.getValue(module, "audioRootFolderPath." + entityType);
		try {
			if (currentImageName != null && !"".equals(currentImageName)) {
				String oldFileName = imagesFolderPath + currentImageName;
				File oldAudioFile = new File(oldFileName);
				oldAudioFile.delete();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(ErrorCodeConstants.IMAGE_SAVE_ERROR);
		} finally {
		}
	}

	public boolean saveAudioFile(String module, String entityType, String fileExtType, String base64Content,
			String newFileName) {
		if (base64Content == null || "".equalsIgnoreCase(base64Content)) {
			return false;
		}
		String fileFolderPath = propertiesService.getValue(module, "audioRootFolderPath." + entityType);
		String fileNameWithoutExtension = fileFolderPath + newFileName;
		String outputFileName = fileFolderPath + "temp-" + newFileName;
		File file = null;
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
			file = new File(outputFileName);
			FileUtils.writeByteArrayToFile(file, decodedBytes);
			SaveFileUtils.saveAudioFile(file, fileExtType, fileNameWithoutExtension);
			FileUtils.forceDelete(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(ErrorCodeConstants.FILE_SAVE_ERROR);
		} finally {
			base64Content = null;
			file = null;
		}
	}
}