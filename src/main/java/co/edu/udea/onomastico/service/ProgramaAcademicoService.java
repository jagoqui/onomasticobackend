package co.edu.udea.onomastico.service;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.onomastico.model.Evento;
import co.edu.udea.onomastico.model.UnidadAcademica;
import co.edu.udea.onomastico.payload.EventoRequest;
import co.edu.udea.onomastico.payload.ProgramaConUnidadAcademicaResponse;
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

	public List<ProgramaConUnidadAcademicaResponse> findAllProgramasAcademicosResponse(){
		List<ProgramaAcademico> programas = programaAcademicoRepository.findAll();
		return getProgramasAcademicosResponseFormat(programas);
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

	public ProgramaConUnidadAcademicaResponse getProgramaConUnidadAcademica(Integer idPrograma){
		ProgramaAcademico programa = programaAcademicoRepository.findById(idPrograma)
				.orElseThrow(() -> new ResourceNotFoundException("ProgramaAcademico" + "id"+ idPrograma));

		ProgramaConUnidadAcademicaResponse programaConUnidadAcademica = new ProgramaConUnidadAcademicaResponse();
		programaConUnidadAcademica.setCodigo(programa.getCodigo());
		programaConUnidadAcademica.setNombre(programa.getNombre());
		programaConUnidadAcademica.setUnidadAcademica(programa.getUnidadAcademica());

		return programaConUnidadAcademica;
	}

	public List<ProgramaConUnidadAcademicaResponse> getProgramasAcademicosResponseFormat(List<ProgramaAcademico> programas){
		List<ProgramaConUnidadAcademicaResponse> programasConUnidadesAcademicas = new ArrayList<>();
		programas.forEach(programaAcademico -> {
			programasConUnidadesAcademicas.add(new ProgramaConUnidadAcademicaResponse(programaAcademico.getCodigo(), programaAcademico.getNombre(), programaAcademico.getUnidadAcademica()));
		});
		return programasConUnidadesAcademicas;
	}

}
