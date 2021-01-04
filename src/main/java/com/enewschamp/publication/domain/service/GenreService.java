package com.enewschamp.publication.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.common.GenreList;
import com.enewschamp.publication.domain.entity.Genre;

@Service
public class GenreService extends AbstractDomainService {

	@Autowired
	GenreRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public Genre create(Genre genre) {
		return repository.save(genre);
	}

	public Genre update(Genre genre) {
		Long genreId = genre.getGenreId();
		Genre existingGenre = get(genreId);
		modelMapper.map(genre, existingGenre);
		return repository.save(existingGenre);
	}

	public Genre patch(Genre genre) {
		Long genreId = genre.getGenreId();
		Genre existingEntity = get(genreId);
		modelMapperForPatch.map(genre, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long genreId) {
		repository.deleteById(genreId);
	}

	public Genre get(Long genreId) {
		Optional<Genre> existingEntity = repository.findById(genreId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.GENRE_NOT_FOUND, String.valueOf(genreId));
		}
	}

	public List<ListOfValuesItem> getLOV() {
		return toListOfValuesItems(repository.getGenreLOV());
	}

	public List<GenreList> getGenreList() {
		return repository.getGenreList();
	}

	public String getAudit(Long genreId) {
		Genre genre = new Genre();
		genre.setGenreId(genreId);
		return auditService.getEntityAudit(genre);
	}

}
