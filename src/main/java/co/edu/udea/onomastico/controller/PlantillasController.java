package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.payload.PlantillaResponse;
import co.edu.udea.onomastico.repository.PlantillaRepository;
import co.edu.udea.onomastico.service.PlantillaService;

@RestController
public class PlantillasController {

	@Autowired
	PlantillaRepository plantillaRepository;
	
	@Autowired
	PlantillaService plantillaService;
	
	@JsonView(Views.Public.class)
	@GetMapping("/plantillas")
	public List<Plantilla> getAllPlantillas(){
		return plantillaRepository.findAll();
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/plantillas/pag/{pageNo}/{pageSize}/{sortBy}")
	public List<Plantilla> getAllUsuariosCorreo(@PathVariable(value = "pageNo") Integer pageNo, 
			@PathVariable(value = "pageSize") Integer pageSize,@PathVariable(value = "sortBy") String sortBy){
        return plantillaService.getAllUsuariosCorreo(pageNo, pageSize, sortBy);
    }
	
	@JsonView(Views.Public.class)
	@PostMapping("/plantillas")
	public ResponseEntity<PlantillaResponse> addPlantilla(@RequestPart("file") MultipartFile file, @RequestPart("plantilla") Plantilla plantilla){
		return  plantillaService.addPlantilla(file, plantilla);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/plantillas/{id}")
	public Plantilla getPantillaById(@PathVariable(value = "id") Integer plantillaId) {
	    return plantillaService.getPantillaById(plantillaId);
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/plantillas/{id}")
	public ResponseEntity<PlantillaResponse> updatePlantilla(@RequestPart("file") MultipartFile file, @RequestPart("plantilla") Plantilla plantilla, @PathVariable(value = "id") Integer plantillaId) {
	    return plantillaService.updatePlantilla(file, plantilla, plantillaId);
	}
	
	@DeleteMapping("/plantillas/{id}")
	public ResponseEntity<?> deletePlantilla(@PathVariable(value = "id") Integer plantillaId) {
		return plantillaService.deletePlantilla(plantillaId);
	}
}
