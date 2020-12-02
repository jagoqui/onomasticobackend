package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.model.Vinculacion;
import co.edu.udea.onomastico.repository.VinculacionRepository;

@RestController
public class VinculacionController {

	@Autowired
	VinculacionRepository vinculacionRepository;
	
	@GetMapping("/vinculaciones")
	@JsonView(Views.Public.class)
	public List<Vinculacion> getAllVinculaciones() {
	    return vinculacionRepository.findAll();
	}
	
	@PostMapping("/vinculaciones")
	@JsonView(Views.Public.class)
	public Vinculacion addVinculacion(@RequestBody Vinculacion vinculacion) {
		vinculacionRepository.save(vinculacion);
		return vinculacion;
	}
}
