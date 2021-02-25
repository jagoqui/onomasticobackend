package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Plataforma;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.repository.PlataformaRepository;

@RestController
public class PlataformaController {

	@Autowired
	PlataformaRepository plataformaRepository;
	
	@GetMapping("/plataformas")
	@JsonView(Views.Public.class)
	public List<Plataforma> getAllPlataformas() {
	    return plataformaRepository.findAll();
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/plataformas/{id}")
	public Plataforma getPlataformaById(@PathVariable(value = "id") Integer plataformaId) {
	    return plataformaRepository.findById(plataformaId)
	            .orElseThrow(() -> new ResourceNotFoundException("Plataforma"+"id"+plataformaId));
	}
	
	@PostMapping("/plataformas")
	@JsonView(Views.Public.class)
	public Plataforma addPlataforma(@RequestBody Plataforma plataforma) {
		plataformaRepository.save(plataforma);
		return plataforma;
	}
	
	@DeleteMapping("/plataformas/{id}")
	public ResponseEntity<?> deleteVinculacion(@PathVariable(value = "id") Integer plataformaId) {
		Plataforma plataforma = plataformaRepository.findById(plataformaId)
	            .orElseThrow(() -> new ResourceNotFoundException("Plataforma"+"id"+plataformaId));

		plataformaRepository.delete(plataforma);

	    return ResponseEntity.ok().build();
	}
}
