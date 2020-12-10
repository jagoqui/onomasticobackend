package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.repository.PlantillaRepository;

@RestController
public class PlantillasController {

	@Autowired
	PlantillaRepository plantillaRepository;
	
	
	@GetMapping("/plantillas")
	public List<Plantilla> getAllPlantillas(){
		return plantillaRepository.findAll();
	}
	
	@PostMapping("plantillas")
	public Plantilla addPlantilla(@RequestBody Plantilla plantilla){
		return plantillaRepository.save(plantilla);
	}
	
	@GetMapping("/plantillas/{id}")
	public Plantilla getPantillaById(@PathVariable(value = "id") Integer plantillaId) {
	    return plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("Plantilla"+"id"+plantillaId));
	}
	
	@PutMapping("/plantillas/{id}")
	public  Plantilla updateUsuario(@PathVariable(value = "id") Integer plantillaId,
	                                         @RequestBody Plantilla detallesPlantilla) {

		 Plantilla  plantilla =  plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("plantilla" + "id"+plantillaId));
	
		plantilla.setCuerpoTexto(detallesPlantilla.getCuerpoTexto());
		plantilla.setImagenArchivo(detallesPlantilla.getImagenArchivo());
	
		Plantilla updatedPlantilla = plantillaRepository.save(plantilla);
	    return updatedPlantilla;
	}
	
	@DeleteMapping("/plantillas/{id}")
	public ResponseEntity<?> deletePlantilla(@PathVariable(value = "id") Integer plantillaId) {
		Plantilla plantilla = plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+plantillaId));

		plantillaRepository.delete(plantilla);
		return ResponseEntity.ok().build();
	}
}