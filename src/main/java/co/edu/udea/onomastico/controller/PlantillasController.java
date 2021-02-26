package co.edu.udea.onomastico.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.payload.PlantillaResponse;
import co.edu.udea.onomastico.payload.UploadFileResponse;
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
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Plantilla> pagedResult =  plantillaRepository.findAll(paging);
        if(pagedResult.hasContent()) return pagedResult.getContent();
        else return new ArrayList<Plantilla>();
    }
	
	@JsonView(Views.Public.class)
	@PostMapping("/plantillas")
	public ResponseEntity<PlantillaResponse> addPlantilla(@RequestPart("file") MultipartFile file, @RequestPart("plantilla") Plantilla plantilla){
		Plantilla newPlantilla = plantillaRepository.save(plantilla);
		StringBuilder text = new StringBuilder("<div id=\"editorContent\" style=\"background-image: url('http://arquimedes.udea.edu.co:8096/onomastico/images/");
		text.append(String.valueOf(newPlantilla.getId()) + "background.jpg'); background-repeat: no-repeat; background-position: center center; background-size: cover; height: auto; min-height: 100%; color: black;\">");
		text.append(plantilla.getTexto());
		plantilla.setTexto(text.toString());
		newPlantilla = plantillaRepository.save(plantilla);
		UploadFileResponse up = plantillaService.uploadPlantillaImage(file, String.valueOf(newPlantilla.getId()));
		PlantillaResponse pr = new PlantillaResponse(up, newPlantilla);
		return  new ResponseEntity<>(pr, HttpStatus.OK);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/plantillas/{id}")
	public Plantilla getPantillaById(@PathVariable(value = "id") Integer plantillaId) {
	    return plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("Plantilla"+"id"+plantillaId));
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/plantillas/{id}")
	public ResponseEntity<PlantillaResponse> updateUsuario(@RequestPart("file") MultipartFile file, @RequestPart("plantilla") Plantilla plantilla, @PathVariable(value = "id") Integer plantillaId) {
		
		Plantilla  plantillaToUpdate =  plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("plantilla" + "id"+plantillaId));
	
		StringBuilder text = new StringBuilder("<div id=\"editorContent\" style=\"background-image: url('http://arquimedes.udea.edu.co:8096/onomastico/images/");
		text.append(String.valueOf(plantillaId) + "background.jpg'); background-repeat: no-repeat; background-position: center center; background-size: cover; height: auto; min-height: 100%; color: black;\">");
		text.append(plantilla.getTexto());
		plantillaToUpdate.setTexto(text.toString());
		plantillaToUpdate.setAsociacionesPorPlantilla(plantilla.getAsociacionesPorPlantilla());;
		UploadFileResponse up = plantillaService.uploadPlantillaImage(file, String.valueOf(plantillaId));
		
		Plantilla updatedPlantilla = plantillaRepository.save(plantillaToUpdate);
		PlantillaResponse pr = new PlantillaResponse(up, updatedPlantilla);
	    return new ResponseEntity<>(pr, HttpStatus.OK);
	}
	
	@DeleteMapping("/plantillas/{id}")
	public ResponseEntity<?> deletePlantilla(@PathVariable(value = "id") Integer plantillaId) {
		Plantilla plantilla = plantillaRepository.findById(plantillaId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+plantillaId));

		plantillaRepository.delete(plantilla);
		return ResponseEntity.ok().build();
	}
}
