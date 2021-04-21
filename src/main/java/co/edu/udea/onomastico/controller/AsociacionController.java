package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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
	public @ResponseBody ResponseEntity<List<Asociacion>> getAllAsociaciones() {
		List<Asociacion> asociaciones =  asociacionService.getAllAsociaciones();
	    return new ResponseEntity<List<Asociacion>>(asociaciones, HttpStatus.OK);
	}
	
	@JsonView(Views.Public.class)
	@PostMapping("/asociaciones")
	public @ResponseBody ResponseEntity<Asociacion> addAsociacion(@RequestBody Asociacion asociacion) {
		Asociacion newAsociacion = asociacionService.addAsociacion(asociacion);
		return new ResponseEntity<Asociacion>(newAsociacion, HttpStatus.OK);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/asociaciones/{id}")
	public @ResponseBody ResponseEntity<Asociacion> getAsociacionById(@PathVariable(value = "id") Integer asociacionId) {
		Asociacion asociacion = asociacionService.getAsociacionById(asociacionId);
		return new ResponseEntity<Asociacion>(asociacion, HttpStatus.OK);
	}
	
	@DeleteMapping("/asociaciones/{id}")
	public ResponseEntity<?> deleteAsociacion(@PathVariable(value = "id") Integer asociacionId) {
		return asociacionService.deleteAsociacion(asociacionId);
	}

}