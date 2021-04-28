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
	public List<EventoResponse> getAllEventos() {
	    return eventoService.findAllEventosResponse();
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/condiciones")
	public List<CondicionResponse> getCondiciones() {
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.getConditionsForUser(usuarioId);
	}
	
//	@JsonView(Views.Public.class)
//	@GetMapping("/evento/pag/{pageNo}/{pageSize}/{sortBy}")
//	public List<EventoResponse> getAllEventos(@PathVariable(value = "pageNo") Integer pageNo, 
//			@PathVariable(value = "pageSize") Integer pageSize,@PathVariable(value = "sortBy") String sortBy){
//        return eventoService.getAllEventos(pageNo, pageSize, sortBy);
//    }
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/pag")
	public List<EventoResponse> getAllEmailsPag(@RequestParam Integer npage, 
			@RequestParam Integer psize,@RequestParam(required = false) String sort){
        return eventoService.getAllEventos(npage, psize, sort);
    }
	
	@JsonView(Views.Public.class)
	@PostMapping("/evento")
	public EventoResponse AddEvento(@RequestBody EventoRequest evento) {
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.AddEvento(evento, userId);
	}
	@JsonView(Views.Public.class)
	@GetMapping("/evento/{id}")
	public EventoResponse getEventoById(@PathVariable(value = "id") Integer eventoId) {
	    return eventoService.getEventoById(eventoId);
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/evento/{id}")
	public  EventoResponse updateEvento(@PathVariable(value = "id") Integer eventoId,
	                             @RequestBody EventoRequest  detallesEvento) {
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.updateEvento(eventoId, detallesEvento, usuarioId);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/plantilla/{plantillaId}")
	public  List<EventoResponse> getEventosByPlantilla(@PathVariable(value = "plantillaId") Integer plantillaId) throws ResourceNotFoundException {
		Plantilla plantilla = plantillaService.getPlantillaById(plantillaId);
	    return eventoService.getEventosByPlantilla(plantilla);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/evento/usuario")
	public List<EventoResponse> getAllPlantillasPorAsociacion(@RequestParam Integer id){
		return eventoService.getAllEventosByUsuario(id);
	}
	@JsonView(Views.Public.class)
	@GetMapping("/evento/usuario/pag")
	public List<EventoResponse> getAllPlantillasPorAsociacionPag(@RequestParam Integer npage,@RequestParam Integer psize,@RequestParam String sort){
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.getAllEventosByUsuarioPag(userId, npage, psize, sort);
	}
	
	@JsonView(Views.Public.class)
	@DeleteMapping("/evento/{id}")
	public ResponseEntity<?> deleteEvento(@PathVariable(value = "id") Integer eventoId) {
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.deleteEvento(eventoId, userId);
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/evento/desactivar/{id}")
	public EventoResponse deactivateEvento(@PathVariable(value = "id") Integer eventoId) {
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.deactivateEvento(eventoId, userId);
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/evento/activar/{id}")
	public EventoResponse activateEvento(@PathVariable(value = "id") Integer eventoId) {
		Integer userId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return eventoService.activateEvento(eventoId, userId);
	}
}
