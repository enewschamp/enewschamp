package com.enewschamp.publication.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.dashboard.handler.GenreView;
import com.enewschamp.app.admin.genre.repository.GenreRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
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
	GenreRepositoryCustomImpl repositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public Genre create(Genre genreEntity) {
		Genre genre = null;
		try {
			genre = repository.save(genreEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return genre;
	}

	public Genre update(Genre genre) {
		Long genreId = genre.getGenreId();
		Genre existingGenre = get(genreId);
		if (existingGenre.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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

	public Genre read(Genre genre) {
		Long genreId = genre.getGenreId();
		Genre genreEntity = get(genreId);
		return genreEntity;
	}

	public Genre close(Genre genreEntity) {
		Long genreId = genreEntity.getGenreId();
		Genre existingEntity = get(genreId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public Genre reinstate(Genre genreEntity) {
		Long genreId = genreEntity.getGenreId();
		Genre existingGenre = get(genreId);
		if (existingGenre.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingGenre.setRecordInUse(RecordInUseType.Y);
		existingGenre.setOperationDateTime(null);
		return repository.save(existingGenre);
	}

	public Page<Genre> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Genre> genreList = repositoryCustom.findAll(pageable, null);
		if (genreList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return genreList;
	}

	public List<GenreView> getAllGenreView() {
		return repository.findAllProjectedBy();
	}

}