package co.edu.udea.onomastico.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.job.EmailScheduling;
import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Evento;
import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.payload.CondicionResponse;
import co.edu.udea.onomastico.payload.EventoRequest;
import co.edu.udea.onomastico.payload.EventoResponse;
import co.edu.udea.onomastico.service.EventoService;
import co.edu.udea.onomastico.service.PlantillaService;

@RestController
public class EventoController {

	@Autowired
	EventoService eventoService; 
	
	@Autowired
	EmailScheduling emailScheduling;
	
	@Autowired
	PlantillaService plantillaService;
	
	//obtener todos los usuarios
	@JsonView(Views.Public.class)
	@GetMapping("/")
	public List<EventoResponse> getAllEventos() {
	    return eventoService.findAllEventosResponse();
	}
	
	@GetMapping("/evento")
	public String getAllemails() {
	    return emailScheduling.scheduleDailyEmails();
	}
	@JsonView(Views.Public.class)
	@GetMapping("/evento/condiciones/{id}")
	public List<CondicionResponse> getCondiciones(@PathVariable(value = "id") Integer userid) {
	    return eventoService.getConditionsForUser(userid);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/pag/{pageNo}/{pageSize}/{sortBy}")
	public List<EventoResponse> getAllUsuariosCorreo(@PathVariable(value = "pageNo") Integer pageNo, 
			@PathVariable(value = "pageSize") Integer pageSize,@PathVariable(value = "sortBy") String sortBy){
        return eventoService.getAllEventos(pageNo, pageSize, sortBy);
    }
	
	@JsonView(Views.Public.class)
	@PostMapping("/evento/{usuarioId}")
	public EventoResponse AddEvento(@RequestBody EventoRequest evento,  @PathVariable(value = "usuarioId") Integer usuarioId) {
		return eventoService.AddEvento(evento, usuarioId);
	}
	@JsonView(Views.Public.class)
	@GetMapping("/evento/{id}")
	public EventoResponse getEventoById(@PathVariable(value = "id") Integer eventoId) {
	    return eventoService.getEventoById(eventoId);
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/evento/{id}/{usuarioId}")
	public  EventoResponse updateEvento(@PathVariable(value = "id") Integer eventoId,
	                             @RequestBody EventoRequest  detallesEvento, @PathVariable(value = "usuarioId")  Integer userId) {
	    return eventoService.updateEvento(eventoId, detallesEvento, userId);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/plantilla/{plantillaId}")
	public  List<EventoResponse> getEventosByPlantilla(@PathVariable(value = "plantillaId") Integer plantillaId) throws ResourceNotFoundException {
		Plantilla plantilla = plantillaService.getPlantillaById(plantillaId);
	    return eventoService.getEventosByPlantilla(plantilla);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/asociacion")
	public  List<Object> getEventosByAsociacion(@RequestBody Set<Asociacion> asociaciones) throws ResourceNotFoundException {
	    return eventoService.getAllEventosByAsociacion(asociaciones);
	}
	
	@DeleteMapping("/evento/{id}/{usuarioId}")
	public ResponseEntity<?> deleteEvento(@PathVariable(value = "id") Integer eventoId,@PathVariable(value = "usuarioId") Integer usuarioId) {
		return eventoService.deleteEvento(eventoId, usuarioId);
	}
	
	@PutMapping("/evento/desactivar/{id}/{usuarioId}")
	public ResponseEntity<?> deactivateEvento(@PathVariable(value = "id") Integer eventoId,@PathVariable(value = "usuarioId") Integer usuarioId) {
		return eventoService.deactivateEvento(eventoId, usuarioId);
	}
	@PutMapping("/evento/activar/{id}/{usuarioId}")
	public ResponseEntity<?> activateEvento(@PathVariable(value = "id") Integer eventoId,@PathVariable(value = "usuarioId") Integer usuarioId) {
		return eventoService.activateEvento(eventoId, usuarioId);
	}
}
