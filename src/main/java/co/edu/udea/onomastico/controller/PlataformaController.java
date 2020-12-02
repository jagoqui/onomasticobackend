package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Plataforma;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.model.Vinculacion;
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
	
	@PostMapping("/plataformas")
	@JsonView(Views.Public.class)
	public Plataforma addPlataforma(@RequestBody Plataforma plataforma) {
		plataformaRepository.save(plataforma);
		return plataforma;
	}
}
