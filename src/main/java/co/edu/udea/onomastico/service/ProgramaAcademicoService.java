package co.edu.udea.onomastico.service;

import java.util.List;

import co.edu.udea.onomastico.model.UnidadAcademica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.repository.ProgramaAcademicoRepository;

@Service
public class ProgramaAcademicoService {

	@Autowired
	ProgramaAcademicoRepository programaAcademicoRepository;
	
	public List<ProgramaAcademico> getAllProgramasAcademicos() {
	    return programaAcademicoRepository.findAll();
	}
	
	public List<ProgramaAcademico> findByProgramaAcademicoPorFacultad(UnidadAcademica unidadAcademica){
		return programaAcademicoRepository.findByUnidadAcademica(unidadAcademica);
	}
	
	public ProgramaAcademico getProgramaAcademicoById(Integer programaId) {
	    return programaAcademicoRepository.findById(programaId)
	            .orElseThrow(() -> new ResourceNotFoundException("Programa"+"id"+programaId));
	}
	
	public ProgramaAcademico addProgramaAcademico(ProgramaAcademico programaAcademico) {
		programaAcademicoRepository.save(programaAcademico);
		return programaAcademico;
	}
	
	public ResponseEntity<?> deleteProgramaAcademico(Integer programaAcademicoId) {
		ProgramaAcademico programaAcademico = programaAcademicoRepository.findById(programaAcademicoId)
	            .orElseThrow(() -> new ResourceNotFoundException("programaAcademico"+"id"+programaAcademicoId));
		programaAcademicoRepository.delete(programaAcademico);
	    return ResponseEntity.ok().build();
	}
	
	public boolean existsProgramaAcademico(Integer id) {
		return programaAcademicoRepository.existsById(id);
		// Buscar asociaciones del publicador
		//
	}

	public UnidadAcademica getUnidadAcademicaByPrograma(Integer idPrograma){
		return getProgramaAcademicoById(idPrograma).getUnidadAcademica();
	}

}
