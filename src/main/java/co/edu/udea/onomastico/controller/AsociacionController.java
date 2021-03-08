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

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.service.AsociacionService;

@RestController
public class AsociacionController {

	@Autowired
	AsociacionService asociacionService;
	
	@JsonView(Views.Public.class)
	@GetMapping("/asociaciones")
	public List<Asociacion> getAllAsociaciones() {
	    return asociacionService.getAllAsociaciones();
	}
	
	@JsonView(Views.Public.class)
	@PostMapping("/asociaciones")
	public Asociacion addAsociacion(@RequestBody Asociacion asociacion) {
		return asociacionService.addAsociacion(asociacion);
		
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/asociaciones/{id}")
	public Asociacion getAsociacionById(@PathVariable(value = "id") Integer asociacionId) {
	    return asociacionService.getAsociacionById(asociacionId);
	}
	
	@DeleteMapping("/asociaciones/{id}")
	public ResponseEntity<?> deleteAsociacion(@PathVariable(value = "id") Integer asociacionId) {
		return asociacionService.deleteAsociacion(asociacionId);
	}

}