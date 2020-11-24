package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Usuario;
import co.edu.udea.onomastico.repository.AsociacionRepository;

@RestController
public class AsociacionController {

	@Autowired
	AsociacionRepository asociacionRepository;
	
	@GetMapping("/asociaciones")
	public List<Asociacion> getAllAsociaciones() {
	    return asociacionRepository.findAll();
	}

}