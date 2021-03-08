package co.edu.udea.onomastico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Vinculacion;
import co.edu.udea.onomastico.repository.VinculacionRepository;

@Service
public class VinculacionService {

	@Autowired
	VinculacionRepository vinculacionRepository;
	
	public List<Vinculacion> getAllVinculaciones() {
	    return vinculacionRepository.findAll();
	}
	
	public Vinculacion addVinculacion(Vinculacion vinculacion) {
		vinculacionRepository.save(vinculacion);
		return vinculacion;
	}
	
	public Vinculacion getVinculacionById(Integer vinculacionId) {
	    return vinculacionRepository.findById(vinculacionId)
	            .orElseThrow(() -> new ResourceNotFoundException("Vinculacion"+"id"+vinculacionId));
	}
	
	public ResponseEntity<?> deleteVinculacion(Integer vinculacionId) {
		Vinculacion vinculacion = vinculacionRepository.findById(vinculacionId)
	            .orElseThrow(() -> new ResourceNotFoundException("Vinculacion"+"id"+vinculacionId));
		vinculacionRepository.delete(vinculacion);
	    return ResponseEntity.ok().build();
	}
}
