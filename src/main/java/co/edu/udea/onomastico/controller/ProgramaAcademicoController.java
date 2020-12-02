package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.model.Vinculacion;
import co.edu.udea.onomastico.repository.ProgramaAcademicoRepository;

@RestController
public class ProgramaAcademicoController {

	@Autowired
	ProgramaAcademicoRepository programaAcademicoRepository;
	
	
	@GetMapping("/programasacademicos")
	@JsonView(Views.Public.class)
	public List<ProgramaAcademico> getAllUsuarios() {
	    return programaAcademicoRepository.findAll();
	}
	
	@PostMapping("/programasacademicos")
	@JsonView(Views.Public.class)
	public ProgramaAcademico addProgramaAcademico(@RequestBody ProgramaAcademico programaAcademico) {
		programaAcademicoRepository.save(programaAcademico);
		return programaAcademico;
	}
}
