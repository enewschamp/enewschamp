package com.enewschamp.app.admin.handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
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
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;
	private Validator validator;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createEdition(pageRequest);
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
	private PageDTO createEdition(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		GenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), GenrePageData.class);
		pageData.setName(pageRequest.getData().get("name").asText());
		pageData.setImage(pageRequest.getData().get("image").asText());
		validate(pageData);
		Genre genre = mapGenreData(pageRequest, pageData);
		genre = genreService.create(genre);
		mapHeaderData(pageRequest, pageDto, pageData, genre);
		pageData.setId(genre.getGenreId());
		pageData.setLastUpdate(genre.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto, GenrePageData pageData, Genre edition) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
	}

	private Genre mapGenreData(PageRequestDTO pageRequest, GenrePageData pageData) {
		Genre genre = modelMapper.map(pageData, Genre.class);
		genre.setNameId(pageData.getName());
		genre.setImageName(pageData.getImage());
		genre.setOperatorId(pageRequest.getData().get("operator").asText());
		genre.setRecordInUse(RecordInUseType.Y);
		return genre;
	}

	@SneakyThrows
	private PageDTO updateGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		GenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), GenrePageData.class);
		validate(pageData);
		Genre genre = mapGenreData(pageRequest, pageData);
		genre = genreService.update(genre);
		mapHeaderData(pageRequest, pageDto, pageData, genre);
		pageData.setLastUpdate(genre.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		GenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), GenrePageData.class);
		Genre genre = modelMapper.map(pageData, Genre.class);
		genre.setGenreId(pageRequest.getData().get("id").asLong());
		genre = genreService.read(genre);
		mapHeaderData(pageRequest, pageDto, pageData, genre);
		mapPageData(pageData, genre);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapPageData(GenrePageData pageData, Genre genre) {
		pageData.setId(genre.getGenreId());
		pageData.setName(genre.getNameId());
		pageData.setImage(genre.getImageName());
		pageData.setRecordInUse(genre.getRecordInUse().toString());
		pageData.setOperator(genre.getOperatorId());
		pageData.setLastUpdate(genre.getOperationDateTime());
	}

	@SneakyThrows
	private PageDTO closeGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		GenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), GenrePageData.class);
		Genre genre = modelMapper.map(pageData, Genre.class);
		genre.setGenreId(pageData.getId());
		genre = genreService.close(genre);
		mapHeaderData(pageRequest, pageDto, pageData, genre);
		mapPageData(pageData, genre);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listGenre(PageRequestDTO pageRequest) {
		Page<Genre> editionList = genreService.list(
				pageRequest.getData().get("pagination").get("pageNumber").asInt(),
				pageRequest.getData().get("pagination").get("pageSize").asInt());

		List<GenrePageData> list = mapGenreData(editionList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		dto.setHeader(pageRequest.getHeader());
		if ((editionList.getNumber() + 1) == editionList.getTotalPages()) {
			pageData.getPagination().setLastPage(true);
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
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				GenrePageData genrePageData = modelMapper.map(genre, GenrePageData.class);
				genrePageData.setImage(genre.getImageName());
				genrePageData.setName(genre.getNameId());
				genrePageData.setId(genre.getGenreId());
				genrePageData.setOperator(genre.getOperatorId());
				genrePageData.setLastUpdate(genre.getOperationDateTime());
				genrePageDataList.add(genrePageData);
			}
		}
		return genrePageDataList;
	}

	private void validate(GenrePageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<GenrePageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
