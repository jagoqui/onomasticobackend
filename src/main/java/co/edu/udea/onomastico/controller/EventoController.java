package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.job.EmailScheduling;
import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.payload.CondicionResponse;
import co.edu.udea.onomastico.payload.CorreoEnviadoResponse;
import co.edu.udea.onomastico.payload.EventoRequest;
import co.edu.udea.onomastico.payload.EventoResponse;
import co.edu.udea.onomastico.security.JwtTokenProvider;
import co.edu.udea.onomastico.service.EventoService;
import co.edu.udea.onomastico.service.PlantillaService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class EventoController {

	@Autowired
	EventoService eventoService; 
	
	@Autowired
	PlantillaService plantillaService;
	
	@Autowired
	FeignClientInterceptor interceptor;
	
	@Autowired
	JwtTokenProvider tokenProvider;
	
	//obtener todos los usuarios
	@JsonView(Views.Public.class)
	@GetMapping("/eventos")
	public List<EventoRequest> getAllEventos() {
	    return eventoService.findAllEventosResponse();
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/condiciones")
	public List<CondicionResponse> getCondiciones() {
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.getConditionsForUser(usuarioId);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/pag")
	public List<EventoRequest> getAllEmailsPag(@Valid @RequestParam Integer npage,
			@RequestParam Integer psize,@RequestParam(required = false) String sort){
        return eventoService.getAllEventos(npage, psize, sort);
    }
	
	@JsonView(Views.Public.class)
	@PostMapping("/evento")
	public EventoRequest AddEvento(@Valid @RequestBody EventoRequest evento) {
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.AddEvento(evento, userId);
	}
	@JsonView(Views.Public.class)
	@GetMapping("/evento/{id}")
	public EventoRequest getEventoById(@Valid @PathVariable(value = "id") Integer eventoId) {
	    return eventoService.getEventoById(eventoId);
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/evento/{id}")
	public  EventoRequest updateEvento(@Valid @PathVariable(value = "id") Integer eventoId,
	                             @RequestBody EventoRequest  detallesEvento) {
		Integer usuarioId = tokenProvider.getUserIdFromJWT(FeignClientInterceptor.getBearerTokenHeader());
		return eventoService.updateEvento(eventoId, detallesEvento, usuarioId);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/plantilla/{plantillaId}")
	public  List<EventoRequest> getEventosByPlantilla(@Valid @NotNull @PathVariable(value = "plantillaId") Integer plantillaId) throws ResourceNotFoundException {
		Plantilla plantilla = plantillaService.getPlantillaById(plantillaId);
	    return eventoService.getEventosByPlantilla(plantilla);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/usuario")
	public List<EventoRequest> getAllPlantillasPorAsociacion(@Valid @NotNull @RequestParam Integer id){
		return eventoService.getAllEventosByUsuario(id);
	}
	@JsonView(Views.Public.class)
	@GetMapping("/evento/usuario/pag")
	public List<EventoRequest> getAllPlantillasPorAsociacionPag(@Valid @RequestParam Integer npage,@Valid @RequestParam Integer psize,@RequestParam String sort){
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.getAllEventosByUsuarioPag(userId, npage, psize, sort);
	}
	
	@JsonView(Views.Public.class)
	@DeleteMapping("/evento/{id}")
	public ResponseEntity<?> deleteEvento(@Valid @NotNull @PathVariable(value = "id") Integer eventoId) {
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.deleteEvento(eventoId, userId);
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/evento/desactivar/{id}")
	public EventoRequest deactivateEvento(@Valid @NotNull @PathVariable(value = "id") Integer eventoId) {
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.deactivateEvento(eventoId, userId);
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/evento/activar/{id}")
	public EventoRequest activateEvento(@Valid @NotNull @PathVariable(value = "id") Integer eventoId) {
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.activateEvento(eventoId, userId);
	}
}
