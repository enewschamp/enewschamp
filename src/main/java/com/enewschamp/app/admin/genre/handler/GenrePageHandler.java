package com.enewschamp.app.admin.genre.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

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
import com.enewschamp.publication.domain.entity.Genre;
import com.enewschamp.publication.domain.service.GenreService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("GenrePageHandler")
@Slf4j
public class GenrePageHandler implements IPageHandler {
	@Autowired
	private GenreService genreService;
	@Autowired
	private CommonService commonService;
	
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
			pageDto = createGenre(pageRequest);
			break;
		case "Update":
			pageDto = updateGenre(pageRequest);
			break;
		case "Read":
			pageDto = readGenre(pageRequest);
			break;
		case "Close":
			pageDto = closeGenre(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateGenre(pageRequest);
			break;
		case "List":
			pageDto = listGenre(pageRequest);
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
	private PageDTO createGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		GenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), GenrePageData.class);
		validate(pageData);
		Genre genre = saveImageAndAudio(pageData);
		mapGenre(pageRequest, pageDto, genre);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		GenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), GenrePageData.class);
		validate(pageData);
	    Genre genre  = updateImage(pageData, pageData.getGenreId());
		mapGenre(pageRequest, pageDto, genre);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		GenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), GenrePageData.class);
		Genre genre = modelMapper.map(pageData, Genre.class);
		genre = genreService.read(genre);
		mapGenre(pageRequest, pageDto, genre);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		GenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), GenrePageData.class);
		Genre genre = modelMapper.map(pageData, Genre.class);
		genre = genreService.close(genre);
		mapGenre(pageRequest, pageDto, genre);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		GenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), GenrePageData.class);
		Genre genre = modelMapper.map(pageData, Genre.class);
		genre = genreService.reinstate(genre);
		mapGenre(pageRequest, pageDto, genre);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listGenre(PageRequestDTO pageRequest) {
		Page<Genre> editionList = genreService.list(
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<GenrePageData> list = mapGenreData(editionList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((editionList.getNumber() + 1) == editionList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<GenrePageData> mapGenreData(Page<Genre> page) {
		List<GenrePageData> genrePageDataList = new ArrayList<GenrePageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<Genre> pageDataList = page.getContent();
			for (Genre genre : pageDataList) {
				GenrePageData genrePageData = modelMapper.map(genre, GenrePageData.class);
				genrePageData.setLastUpdate(genre.getOperationDateTime());
				genrePageDataList.add(genrePageData);
			}
		}
		return genrePageDataList;
	}

	private void mapGenre(PageRequestDTO pageRequest, PageDTO pageDto, Genre genre) {
		GenrePageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(genre);
		pageDto.setData(pageData);
	}

	private GenrePageData mapPageData(Genre genre) {
		GenrePageData pageData = modelMapper.map(genre, GenrePageData.class);
		pageData.setLastUpdate(genre.getOperationDateTime());
		pageData.setImageBase64(null);
		return pageData;
	}

	private void validate(GenrePageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<GenrePageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error("Validation failed: " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
	
	private Genre updateImage(GenrePageData genreDTO, Long genreId) {
		genreDTO.setGenreId(genreId);
		Genre genre = genreService.get(genreId);
		if (genre.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		String currentImageName = genre.getImageName();
		genreDTO.setImageName(currentImageName);
		genre = modelMapper.map(genreDTO, Genre.class);
		genre.setRecordInUse(RecordInUseType.Y);
		genre = genreService.update(genre);
		boolean updateFlag = false;
		if ("Y".equalsIgnoreCase(genreDTO.getImageUpdate())) {
			String newImageName = genre.getGenreId() + "_IMG_" + System.currentTimeMillis();
			String imageType = genreDTO.getImageTypeExt();
			boolean saveImageFlag = commonService.saveImages("Admin", "genre", imageType, genreDTO.getImageBase64(),
					newImageName);
			if (saveImageFlag) {
				genre.setImageName(newImageName + "." + imageType);
				updateFlag = true;
			} else {
				genre.setImageName(null);
				updateFlag = true;
			}
			if (currentImageName != null && !"".equals(currentImageName)) {
				commonService.deleteImages("Admin", "genre", currentImageName);
				updateFlag = true;
			}
		}
		
		if (updateFlag) {
			genre = genreService.update(genre);
		}
		return genre;
	}
	
	private Genre saveImageAndAudio(GenrePageData genreDTO) {
		Genre genre = modelMapper.map(genreDTO, Genre.class);
		try {
			genre.setRecordInUse(RecordInUseType.Y);
			genre = genreService.create(genre);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getMessage());
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		boolean updateFlag = false;
		if ("Y".equalsIgnoreCase(genreDTO.getImageUpdate())) {
			String newImageName = genre.getGenreId() + "_IMG_" + System.currentTimeMillis();
			String imageType = genreDTO.getImageTypeExt();
			String currentImageName = genre.getImageName();
			boolean saveImageFlag = commonService.saveImages("Admin", "genre", imageType, genreDTO.getImageBase64(),
					newImageName);
			if (saveImageFlag) {
				genre.setImageName(newImageName + "." + imageType);
				updateFlag = true;
			} else {
				genre.setImageName(null);
				updateFlag = true;
			}
			if (currentImageName != null && !"".equals(currentImageName)) {
				commonService.deleteImages("Admin", "genre", currentImageName);
				updateFlag = true;
			}
		}
	
		if (updateFlag) {
			genre = genreService.update(genre);
		}
		genre.setRecordInUse(RecordInUseType.Y);
		return genre;
	}
}
