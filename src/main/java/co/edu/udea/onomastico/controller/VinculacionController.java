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

import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.model.Vinculacion;
import co.edu.udea.onomastico.service.VinculacionService;

@RestController
public class VinculacionController {

	@Autowired
	VinculacionService  vinculacionService;
	
	@GetMapping("/vinculaciones")
	@JsonView(Views.Public.class)
	public List<Vinculacion> getAllVinculaciones() {
	    return vinculacionService.getAllVinculaciones();
	}
	
	@PostMapping("/vinculaciones")
	@JsonView(Views.Public.class)
	public Vinculacion addVinculacion(@RequestBody Vinculacion vinculacion) {
		return vinculacionService.addVinculacion(vinculacion);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/vinculaciones/{id}")
	public Vinculacion getVinculacionById(@PathVariable(value = "id") Integer vinculacionId) {
	    return vinculacionService.getVinculacionById(vinculacionId);
	}
	
	@DeleteMapping("/vinculaciones/{id}")
	public ResponseEntity<?> deleteVinculacion(@PathVariable(value = "id") Integer vinculacionId) {
		return vinculacionService.deleteVinculacion(vinculacionId);
	}
}
