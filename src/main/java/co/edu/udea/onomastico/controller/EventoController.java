package co.edu.udea.onomastico.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.job.EmailScheduling;
import co.edu.udea.onomastico.model.Evento;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.service.EventoService;

@RestController
public class EventoController {

	@Autowired
	EventoService eventoService; 
	
	@Autowired
	EmailScheduling emailScheduling;
	
	//obtener todos los usuarios
	@JsonView(Views.Public.class)
	@GetMapping("/")
	public List<Evento> getAllEventos() {
	    return eventoService.findAllEventos();
	}
	
	@GetMapping("/evento")
	public String getAllemails() {
	    return emailScheduling.scheduleDailyEmails();
	}
	@JsonView(Views.Public.class)
	@GetMapping("/evento/condicones/{id}")
	public List<Object> getCondiciones(@PathVariable(value = "id") Integer userid) {
	    return eventoService.getConditionsForUser(userid);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/pag/{pageNo}/{pageSize}/{sortBy}")
	public List<Evento> getAllUsuariosCorreo(@PathVariable(value = "pageNo") Integer pageNo, 
			@PathVariable(value = "pageSize") Integer pageSize,@PathVariable(value = "sortBy") String sortBy){
        return eventoService.getAllEventos(pageNo, pageSize, sortBy);
    }
	
	@JsonView(Views.Public.class)
	@PostMapping("/evento")
	public Evento AddEvento(@RequestBody Evento evento, @RequestBody Integer usuarioId) {
		return eventoService.AddEvento(evento, usuarioId);
	}
	
	@GetMapping("/evento/{id}")
	public Evento getEventoById(@PathVariable(value = "id") Integer eventoId) {
	    return eventoService.getEventoById(eventoId);
	}
	
	@PutMapping("/evento/{id}")
	public  Evento updateEvento(@PathVariable(value = "id") Integer eventoId,
	                             @RequestBody Evento detallesEvento, @RequestBody Integer userId) {
	    return eventoService.updateEvento(eventoId, detallesEvento, userId);
	}
}
