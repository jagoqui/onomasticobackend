package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.CorreoEnviado;
import co.edu.udea.onomastico.model.Evento;
import co.edu.udea.onomastico.repository.CorreoEnviadoRepository;

@RestController
public class CorreoEnviadoController {

	@Autowired
	CorreoEnviadoRepository correoEnviadoRepository;
	
	@GetMapping("/emails")
	public List<CorreoEnviado> getAllEmails() {
		return correoEnviadoRepository.findAll();
	}
	
	@PostMapping("/emails")
	public CorreoEnviado addCorreoEnviado(@RequestBody CorreoEnviado correoEnviado) {
		return correoEnviadoRepository.save(correoEnviado);
	}
	
	@GetMapping("/emails/{id}")
	public CorreoEnviado getEventoById(@PathVariable(value = "id") Integer eventoId) {
	    return correoEnviadoRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Evento"+"id"+eventoId));
	}
}
