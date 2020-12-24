package com.enewschamp.publication.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Edition;
import com.enewschamp.publication.domain.entity.Genre;
import com.enewschamp.publication.domain.repository.GenreRepositoryCustom;

@Service
public class GenreService extends AbstractDomainService {

	@Autowired
	GenreRepository repository;
	
	@Autowired
	GenreRepositoryCustom repositoryCustom;

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

	public String getAudit(Long genreId) {
		Genre genre = new Genre();
		genre.setGenreId(genreId);
		return auditService.getEntityAudit(genre);
	}

	public Genre read(Genre genreEntity) {
		Long genreId = genreEntity.getGenreId();
		Genre existingGenre = get(genreId);
		existingGenre.setRecordInUse(RecordInUseType.Y);
		return repository.save(existingGenre);
	}

	public Genre close(Genre genreEntity) {
		Long genreId = genreEntity.getGenreId();
		Genre existingEntity = get(genreId);
		existingEntity.setRecordInUse(RecordInUseType.N);
		return repository.save(existingEntity);
	}

	public Page<Genre> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Genre> genreList = repositoryCustom.findGenres(pageable);
		return genreList;
	}

}
