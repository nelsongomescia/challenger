package com.dws.challenge.common.validations;

import static org.springframework.http.ResponseEntity.badRequest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.dws.challenge.domain.ResponseDTO;


public class BindingResultValidation {

	private BindingResultValidation() {
		throw new IllegalStateException("Utility class");
	} //SonarLint Alert

	/**
	 * Validate Spring Binding Result Process
	 * If there is error(s) return a BadRequest with message(s) error(s) in the body
	 */
	public static ResponseEntity<ResponseDTO> validateBindingResult(BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return badRequest().body(getBindingResultErrors(bindingResult));
		}

		return null;
	}


	private static ResponseDTO getBindingResultErrors(BindingResult result) {

		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, String> map = new HashMap<>();
		for (ObjectError error : result.getAllErrors()) {
			if (error instanceof FieldError) {
				FieldError fieldError = (FieldError) error;
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			} else {
				map.put(error.getCode(), error.getDefaultMessage());
			}
		}
		responseDTO.setDetails(map);
		return responseDTO;
	}

}