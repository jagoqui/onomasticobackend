package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Usuario;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.repository.AsociacionRepository;

@RestController
public class AsociacionController {

	@Autowired
	AsociacionRepository asociacionRepository;
	
	@JsonView(Views.Public.class)
	@GetMapping("/asociaciones")
	public List<Asociacion> getAllAsociaciones() {
	    return asociacionRepository.findAll();
	}
	
	@JsonView(Views.Public.class)
	@PostMapping("/asociaciones")
	public Asociacion addAsociacion(@RequestBody Asociacion asociacion) {
		asociacionRepository.save(asociacion);
		return asociacion;
		
	}

}