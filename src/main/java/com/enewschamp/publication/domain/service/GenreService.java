package com.enewschamp.publication.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.publication.domain.common.PublicationErrorCodes;
import com.enewschamp.publication.domain.entity.Genre;

@Service
public class GenreService {

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
		String genreId = genre.getGenreId();
		Genre existingGenre = get(genreId);
		modelMapper.map(genre, existingGenre);
		return repository.save(existingGenre);
	}
	
	public Genre patch(Genre genre) {
		String genreId = genre.getGenreId();
		Genre existingEntity = get(genreId);
		modelMapperForPatch.map(genre, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(String genreId) {
		repository.deleteById(genreId);
	}
	
	public Genre get(String genreId) {
		Optional<Genre> existingEntity = repository.findById(genreId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), PublicationErrorCodes.GENRE_NOT_FOUND, "Genre not found!");
		}
	}
	
	public String getAudit(String genreId) {
		Genre genre = new Genre();
		genre.setGenreId(genreId);
		return auditService.getEntityAudit(genre);
	}
	
}
