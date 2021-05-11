package co.edu.udea.onomastico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.repository.AsociacionRepository;

@Service
public class AsociacionService {

	@Autowired
	AsociacionRepository asociacionRepository;
	public List<Asociacion> getAllAsociaciones() {
	    return asociacionRepository.findAll();
	}
	
	public Asociacion addAsociacion(Asociacion asociacion) {
		asociacionRepository.save(asociacion);
		return asociacion;
		
	}
	public Asociacion getAsociacionById(Integer asociacionId) {
	    return asociacionRepository.findById(asociacionId)
	            .orElseThrow(() -> new ResourceNotFoundException("Asociacion"+"id"+asociacionId));
	}
	
	public ResponseEntity<?> deleteAsociacion(Integer asociacionId) {
		Asociacion asociacion = asociacionRepository.findById(asociacionId)
	            .orElseThrow(() -> new ResourceNotFoundException("Asociacion"+"id"+asociacionId));
		asociacionRepository.delete(asociacion);
	    return ResponseEntity.ok().build();
	}
	
	public List<Asociacion> getAsociacionByProgramaAcademico(ProgramaAcademico programaAcademico){
		return  asociacionRepository.findByProgramasAsociacion(programaAcademico);
	}
	
	public boolean existsAsociacion(Integer id) {
		return asociacionRepository.existsById(id);
	}
}
