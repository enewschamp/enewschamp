package com.enewschamp.publication.app.service;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.publication.app.dto.GenreDTO;
import com.enewschamp.publication.domain.entity.Genre;
import com.enewschamp.publication.domain.service.GenreService;

@RestController
@RequestMapping("/enewschamp-api/v1")
public class GenreController {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private GenreService genreService;

	@PostMapping(value = "/genres")
	public ResponseEntity<GenreDTO> create(@RequestBody @Valid GenreDTO genreDTO) {
		Genre genre = modelMapper.map(genreDTO, Genre.class);
		genre = genreService.create(genre);
		genreDTO = modelMapper.map(genre, GenreDTO.class);
		return new ResponseEntity<GenreDTO>(genreDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/genres/{genreId}")
	public ResponseEntity<GenreDTO> update(@RequestBody @Valid GenreDTO genreDTO, @PathVariable String genreId) {
		genreDTO.setGenreId(genreId);
		Genre genre = modelMapper.map(genreDTO, Genre.class);
		genre = genreService.update(genre);
		genreDTO = modelMapper.map(genre, GenreDTO.class);
		return new ResponseEntity<GenreDTO>(genreDTO, HttpStatus.OK);
	}
	
	@PatchMapping(value = "/genres/{genreId}")
	public ResponseEntity<GenreDTO> patch(@RequestBody GenreDTO genreDTO, @PathVariable String genreId) {
		genreDTO.setGenreId(genreId);
		Genre genre = modelMapper.map(genreDTO, Genre.class);
		genre = genreService.patch(genre);
		genreDTO = modelMapper.map(genre, GenreDTO.class);
		return new ResponseEntity<GenreDTO>(genreDTO, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/genres/{genreId}")
	public ResponseEntity<Void> delete(@PathVariable String genreId) {
		genreService.delete(genreId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/genres/{genreId}")
	public ResponseEntity<GenreDTO> get(@PathVariable String genreId) {
		Genre genre = genreService.get(genreId);
		GenreDTO genreDTO = modelMapper.map(genre, GenreDTO.class);
		genreService.get(genreId);
		return new ResponseEntity<GenreDTO>(genreDTO, HttpStatus.OK);
	}
	
	@GetMapping(value = "/genres/{genreId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable String genreId) {
		String audit = genreService.getAudit(genreId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}
	
	

}
