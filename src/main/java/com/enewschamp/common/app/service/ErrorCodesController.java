package com.enewschamp.common.app.service;

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

import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.common.app.dto.ErrorCodesDTO;
import com.enewschamp.common.domain.entity.ErrorCodes;
import com.enewschamp.common.domain.service.ErrorCodesService;
import com.enewschamp.page.dto.ListOfValuesItem;

@RestController
@RequestMapping("/enewschamp-api/v1")
public class ErrorCodesController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private ErrorCodesService errorCodesService;

	@PostMapping(value = "/admin/errorcodes")
	public ResponseEntity<ErrorCodesDTO> create(@RequestBody @Valid ErrorCodesDTO errorCodeDTO) {
		ErrorCodes errorCode = modelMapper.map(errorCodeDTO, ErrorCodes.class);
		errorCode = errorCodesService.create(errorCode);
		errorCodeDTO = modelMapper.map(errorCode, ErrorCodesDTO.class);
		return new ResponseEntity<ErrorCodesDTO>(errorCodeDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/admin/errorcodes/{errorcodeId}")
	public ResponseEntity<ErrorCodesDTO> update(@RequestBody @Valid ErrorCodesDTO errorCodeDTO,
			@PathVariable Long errorCodeId) {
		errorCodeDTO.setErrorCodeId(errorCodeId);
		ErrorCodes errorCode = modelMapper.map(errorCodeDTO, ErrorCodes.class);
		errorCode = errorCodesService.update(errorCode);
		errorCodeDTO = modelMapper.map(errorCode, ErrorCodesDTO.class);
		return new ResponseEntity<ErrorCodesDTO>(errorCodeDTO, HttpStatus.OK);
	}

	@PatchMapping(value = "/admin/errorcodes/{errorcodeId}")
	public ResponseEntity<ErrorCodesDTO> patch(@RequestBody ErrorCodesDTO errorCodeDTO,
			@PathVariable Long errorCodeId) {
		errorCodeDTO.setErrorCodeId(errorCodeId);
		ErrorCodes errorCode = modelMapper.map(errorCodeDTO, ErrorCodes.class);
		errorCode = errorCodesService.patch(errorCode);
		errorCodeDTO = modelMapper.map(errorCode, ErrorCodesDTO.class);
		return new ResponseEntity<ErrorCodesDTO>(errorCodeDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/admin/errorcodes/{errorCodeId}")
	public ResponseEntity<Void> delete(@PathVariable Long errorCodeId) {
		errorCodesService.delete(errorCodeId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping(value = "/admin/errorcodes/{errorCodeId}")
	public ResponseEntity<ErrorCodesDTO> get(@PathVariable Long errorCodeId) {
		ErrorCodes errorCode = errorCodesService.get(errorCodeId);
		ErrorCodesDTO errorCodeDTO = modelMapper.map(errorCode, ErrorCodesDTO.class);
		errorCodesService.get(errorCodeId);
		return new ResponseEntity<ErrorCodesDTO>(errorCodeDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/admin/errorCodesLOV")
	public ResponseEntity<List<ListOfValuesItem>> getLOV() {
		return new ResponseEntity<List<ListOfValuesItem>>(errorCodesService.getLOV(), HttpStatus.OK);
	}

	@GetMapping(value = "/admin/errorcodes/{errorCodeId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long errorCodeId) {
		String audit = errorCodesService.getAudit(errorCodeId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}

}
