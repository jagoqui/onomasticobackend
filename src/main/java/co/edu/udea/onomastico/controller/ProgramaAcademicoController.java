package co.edu.udea.onomastico.controller;

import java.util.List;

import co.edu.udea.onomastico.payload.ProgramaConUnidadAcademicaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.service.ProgramaAcademicoService;

@RestController
public class ProgramaAcademicoController {

	@Autowired
	ProgramaAcademicoService programaAcademicoService;
	
	
	@GetMapping("/programasacademicos")
	@JsonView(Views.Public.class)
	public List<ProgramaAcademico> getAllProgramasAcademicos() {
	    return programaAcademicoService.getAllProgramasAcademicos();
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/programasacademicos/{id}")
	public ProgramaAcademico getProgramaAcademicoById(@PathVariable(value = "id") Integer programaId) {
	    return  programaAcademicoService.getProgramaAcademicoById(programaId);
	}
	
	@PostMapping("/programasacademicos")
	@JsonView(Views.Public.class)
	public ProgramaAcademico addProgramaAcademico(@RequestBody ProgramaAcademico programaAcademico) {
		return  programaAcademicoService.addProgramaAcademico(programaAcademico);
	}
	
	@DeleteMapping("/programasacademicos/{id}")
	public ResponseEntity<?> deleteProgramaAcademico(@PathVariable(value = "id") Integer programaAcademicoId) {
		return programaAcademicoService.deleteProgramaAcademico(programaAcademicoId);
	}

	@JsonView(Views.Public.class)
	@GetMapping("/programaconunidad/{id}")
	public ProgramaConUnidadAcademicaResponse getProgramaAcademicoConUnidadAcademica(@PathVariable(value = "id") Integer programaId) {
		return  programaAcademicoService.getProgramaConUnidadAcademica(programaId);
	}

}
