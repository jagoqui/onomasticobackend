package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udea.onomastico.exceptions.BadRequestException;
import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Evento;
import co.edu.udea.onomastico.model.Usuario;
import co.edu.udea.onomastico.repository.EventoRepository;
import co.edu.udea.onomastico.repository.UsuarioRepository;

@RestController
public class EventoController {


	@Autowired
	EventoRepository  eventoRepository;
	
	//obtener todos los usuarios
	@GetMapping("/")
	public List<Evento> getAllEventos() {
	    return eventoRepository.findAll();
	}
	
	//crear usuario
	@PostMapping("/evento")
	public Evento AddEvento(@RequestBody Evento evento) {
	    Evento newEvento = eventoRepository.save(evento);
	    return newEvento;
	}
	@GetMapping("/evento/{id}")
	public Evento getEventoById(@PathVariable(value = "id") Integer eventoId) {
	    return eventoRepository.findById(eventoId)
	            .orElseThrow(() -> new ResourceNotFoundException("Evento"+"id"+eventoId));
	}
	
	@PutMapping("/evento/{id}")
	public  Evento updateUsuario(@PathVariable(value = "id") Integer eventoId,
	                                         @RequestBody Evento detallesEvento) {
		Evento  evento =  eventoRepository.findById(eventoId)
	            .orElseThrow(() -> new ResourceNotFoundException("Evento" + "id"+eventoId));

		evento.setAsociacion(detallesEvento.getAsociacion());
		evento.setEstado(detallesEvento.getEstado());
		evento.setFecha(detallesEvento.getFecha());
		evento.setNombre(detallesEvento.getNombre());
		evento.setRecurrencia(detallesEvento.getRecurrencia());
		evento.setPlantilla(detallesEvento.getPlantilla());
		evento.setCondicionesEvento(detallesEvento.getCondicionesEvento());
		Evento updatedEvento = eventoRepository.save(evento);
	    return updatedEvento;
	}
}
