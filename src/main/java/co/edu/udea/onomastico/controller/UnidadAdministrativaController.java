package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import co.edu.udea.onomastico.model.UnidadAdministrativa;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.service.UnidadAdministrativaService;

@RestController
public class UnidadAdministrativaController {

	@Autowired
	UnidadAdministrativaService unidadAdministrativaService;
	
	@JsonView(Views.Public.class)
	@GetMapping("/unidadesadministrativas")
	public @ResponseBody ResponseEntity<List<UnidadAdministrativa>> getAllUnidadesAdministrativas() {
		List<UnidadAdministrativa> unidadAdministrativa =  unidadAdministrativaService.getAllUnidadesAdministrativas();
	    return new ResponseEntity<List<UnidadAdministrativa>>(unidadAdministrativa, HttpStatus.OK);
	}
	
	@JsonView(Views.Public.class)
	@PostMapping("/unidadesadministrativas")
	public @ResponseBody ResponseEntity<UnidadAdministrativa> addUnidadAdministrativa(@Valid @NotBlank @RequestBody UnidadAdministrativa unidadAdministrativa) {
		UnidadAdministrativa newUnidadAdministrativa = unidadAdministrativaService.addUnidadAdministrativa(unidadAdministrativa);
		return new ResponseEntity<UnidadAdministrativa>(newUnidadAdministrativa, HttpStatus.OK);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/unidadesadministrativas/{id}")
	public @ResponseBody ResponseEntity<UnidadAdministrativa> getUnidadAdministrativaById(@Valid @NotNull @PathVariable(value = "id") Integer unidadAdministrativaId) {
		UnidadAdministrativa unidadAdministrativa = unidadAdministrativaService.getUnidadAdministrativaById(unidadAdministrativaId);
		return new ResponseEntity<UnidadAdministrativa>(unidadAdministrativa, HttpStatus.OK);
	}
	
	@DeleteMapping("/unidadesadministrativas/{id}")
	public ResponseEntity<?> deleteUnidadAdministrativa(@Valid @NotNull @PathVariable(value = "id") Integer unidadAdministrativaId) {
		return unidadAdministrativaService.deleteUnidadAdministrativa(unidadAdministrativaId);
	}

}