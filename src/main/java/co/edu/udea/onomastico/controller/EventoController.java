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
import co.edu.udea.onomastico.repository.EventoRepository;

@RestController
public class EventoController {


	@Autowired
	EventoRepository  eventoRepository;
	
	@Autowired
	EmailScheduling emailScheduling;
	
	//obtener todos los usuarios
	@JsonView(Views.Public.class)
	@GetMapping("/")
	public List<Evento> getAllEventos() {
	    return eventoRepository.findAll();
	}
	
	@GetMapping("/evento")
	public String getAllemails() {
	    return emailScheduling.scheduleDailyEmails();
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/pag/{pageNo}/{pageSize}/{sortBy}")
	public List<Evento> getAllUsuariosCorreo(@PathVariable(value = "pageNo") Integer pageNo, 
			@PathVariable(value = "pageSize") Integer pageSize,@PathVariable(value = "sortBy") String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Evento> pagedResult =  eventoRepository.findAll(paging);
        if(pagedResult.hasContent()) return pagedResult.getContent();
        else return new ArrayList<Evento>();
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
