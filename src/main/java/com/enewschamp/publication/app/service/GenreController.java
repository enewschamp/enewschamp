package com.enewschamp.publication.app.service;

import java.util.List;

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

import com.enewschamp.app.common.CommonService;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.page.dto.ListOfValuesItem;
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

	@Autowired
	CommonService commonService;

	@Autowired
	private PropertiesService propertiesService;

	@PostMapping(value = "/admin/genres")
	public ResponseEntity<GenreDTO> create(@RequestBody @Valid GenreDTO genreDTO) {
		Genre genre = modelMapper.map(genreDTO, Genre.class);
		genre = genreService.create(genre);
		String newImageName = genre.getGenreId() + "_" + System.currentTimeMillis();
		String imageType = "jpg";
		boolean saveFlag = commonService.saveImages("genre", imageType, genre.getBase64Image(), newImageName,
				genre.getImageName());
		if (saveFlag) {
			genre.setImageName(newImageName + "." + imageType);
			genre = genreService.update(genre);
		}
		genreDTO = modelMapper.map(genre, GenreDTO.class);
		return new ResponseEntity<GenreDTO>(genreDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/admin/genres/{genreId}")
	public ResponseEntity<GenreDTO> update(@RequestBody @Valid GenreDTO genreDTO, @PathVariable Long genreId) {
		genreDTO.setGenreId(genreId);
		Genre genre = modelMapper.map(genreDTO, Genre.class);
		genre = genreService.update(genre);
		genreDTO = modelMapper.map(genre, GenreDTO.class);
		return new ResponseEntity<GenreDTO>(genreDTO, HttpStatus.OK);
	}

	@PatchMapping(value = "/admin/genres/{genreId}")
	public ResponseEntity<GenreDTO> patch(@RequestBody GenreDTO genreDTO, @PathVariable Long genreId) {
		genreDTO.setGenreId(genreId);
		Genre genre = modelMapper.map(genreDTO, Genre.class);
		genre = genreService.patch(genre);
		genreDTO = modelMapper.map(genre, GenreDTO.class);
		return new ResponseEntity<GenreDTO>(genreDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/admin/genres/{genreId}")
	public ResponseEntity<Void> delete(@PathVariable Long genreId) {
		genreService.delete(genreId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping(value = "/admin/genres/{genreId}")
	public ResponseEntity<GenreDTO> get(@PathVariable Long genreId) {
		Genre genre = genreService.get(genreId);
		GenreDTO genreDTO = modelMapper.map(genre, GenreDTO.class);
		genreService.get(genreId);
		return new ResponseEntity<GenreDTO>(genreDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/admin/genreLOV")
	public ResponseEntity<List<ListOfValuesItem>> getLOV() {
		return new ResponseEntity<List<ListOfValuesItem>>(genreService.getLOV(), HttpStatus.OK);
	}

	@GetMapping(value = "/admin/genres/{genreId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long genreId) {
		String audit = genreService.getAudit(genreId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}
}
