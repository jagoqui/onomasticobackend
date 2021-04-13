package co.edu.udea.onomastico.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Condicion;
import co.edu.udea.onomastico.model.CondicionId;
import co.edu.udea.onomastico.model.Evento;
import co.edu.udea.onomastico.model.LogTransacciones;
import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.Usuario;
import co.edu.udea.onomastico.model.Vinculacion;
import co.edu.udea.onomastico.payload.CondicionRequest;
import co.edu.udea.onomastico.payload.CondicionResponse;
import co.edu.udea.onomastico.payload.EventoRequest;
import co.edu.udea.onomastico.payload.EventoResponse;
import co.edu.udea.onomastico.payload.ParametroResponse;
import co.edu.udea.onomastico.payload.ValorResponse;
import co.edu.udea.onomastico.repository.CondicionRepository;
import co.edu.udea.onomastico.repository.EventoRepository;
import co.edu.udea.onomastico.repository.ProgramaAcademicoRepository;
import co.edu.udea.onomastico.repository.UsuarioRepository;
import co.edu.udea.onomastico.repository.VinculacionRepository;

@Service
public class EventoService {

	@Autowired
	EventoRepository eventoRepository;
	
	@Autowired
	CondicionRepository condicionRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	LogTransaccionesService transaccionesService;
	
	@Autowired
	AsociacionService asociacionService;
	
	@Autowired
	VinculacionService vinculacionService;
	
	@Autowired
	ProgramaAcademicoService programaAcademicoService;
	
	@Autowired
	UsuarioService usuarioService;

	public List<Evento> getAllEventos() {
	    return eventoRepository.findAll();
	}
	public List<EventoResponse> getAllEventosByUsuario(Integer usuarioId){
		Set<Asociacion> asociaciones = usuarioService.getAsociacionUsuarioById(usuarioId);
		List<Evento> eventos = getAllEventosByAsociacion(asociaciones);
		return getEventoResponseFormat(eventos);
	}
	
	public List<EventoResponse> getAllEventosByUsuarioPag(Integer usuarioId, Integer pageNo, Integer pageSize, String sortBy){
		Set<Asociacion> asociaciones = usuarioService.getAsociacionUsuarioById(usuarioId);
		List<Evento> eventosAsociacion = getAllEventosByAsociacion(asociaciones);
		List<EventoResponse> eventos = getEventoResponseFormat(eventosAsociacion);
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		final int start = (int)paging.getOffset();
		final int end = Math.min((start + paging.getPageSize()), eventos.size());
		final Page<EventoResponse> page = new PageImpl<>(eventos.subList(start, end), paging, eventos.size());
		return page.toList();
	}
	
	public List<Evento> getAllEventosByAsociacion(Set<Asociacion> asociaciones) {
		List<Evento> merge = new ArrayList<>();
		asociaciones.forEach(asociacion->{
			List<Integer> indexes = condicionRepository.getEventosIdByAsociacion(asociacion.getId());
			indexes.forEach(index->{
				List<Evento> temp = eventoRepository.findById(index).stream().collect(Collectors.toList());
				merge.addAll(temp);
			});
		});
	    return merge;
	}
	
	public List<EventoResponse> getAllEventos(Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Evento> pagedResult =  eventoRepository.findAll(paging);
        List<Evento> eventos = pagedResult.getContent();
        if(!pagedResult.isEmpty()) return getEventoResponseFormat(eventos);
        else return new ArrayList<EventoResponse>();
    }
	
	public List<EventoResponse> findAllEventosResponse(){
		List<Evento> eventos = eventoRepository.findAll();
		return getEventoResponseFormat(eventos);
	}
	public  List<EventoResponse> getEventosByPlantilla(Plantilla plantilla) {
		List<Evento> eventos = eventoRepository.findEventoByPlantilla(plantilla);
		return getEventoResponseFormat(eventos);
	}
	
	public EventoResponse AddEvento(EventoRequest eventoRequest, Integer usuarioId) {
		Evento evento = new Evento();
		evento.setFecha(eventoRequest.getFecha());
		evento.setEstado(eventoRequest.getEstado());
		evento.setNombre(eventoRequest.getNombre());
		evento.setPlantilla(eventoRequest.getPlantilla());
		evento.setRecurrencia(eventoRequest.getRecurrencia());
		evento.setCondicionesEvento(null);
	    Evento newEvento = eventoRepository.saveAndFlush(evento);
	    Integer newEventoId = newEvento.getId();
	    Set<CondicionRequest> condicionRequest = eventoRequest.getCondicionesEvento();
	    Set<Condicion> condiciones =  new HashSet<Condicion>();
	    condicionRequest.forEach(condicion->{
	    	if(condicion.getCondicion().contains("genero")) {
	    		String parametro = "FEMENINO";
	    		if(condicion.getParametro().contains("1")) parametro = "MASCULINO";
	    		condiciones.add(new Condicion(new CondicionId(newEventoId,condicion.getCondicion(),parametro), newEvento));
	    	}else {
	    	condiciones.add(new Condicion(new CondicionId(newEventoId,condicion.getCondicion(),condicion.getParametro()), newEvento));
	    	}
	    });
	    Set<CondicionRequest> result = condicionRequest.stream()
	    		.filter(item -> item.getCondicion().equals("asociacion")).collect(Collectors.toSet());
	    	     
	    if(result.isEmpty()) {
	    	Set<Asociacion> as = usuarioService.getAsociacionUsuarioById(usuarioId);
	    	as.forEach(asociacion ->{
	    		condiciones.add(new Condicion(new CondicionId(newEventoId,"asociacion",String.valueOf(asociacion.getId())), newEvento));
	    	});
	    }
	    newEvento.setCondicionesEvento(condiciones);
	    eventoRepository.save(newEvento);
	    LogTransacciones transaccion = new LogTransacciones("Añadir evento:"+evento.getId()+" "+evento.getNombre());
		transaccionesService.createTransaccion(usuarioId, transaccion);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(newEvento);
		List<EventoResponse> eventoResponse = getEventoResponseFormat(eventos);
	    return eventoResponse.get(0);
	}
	
	public  EventoResponse updateEvento(Integer eventoId, EventoRequest detallesEvento, Integer usuarioId) {
		Evento  evento =  eventoRepository.findById(eventoId).orElseThrow(() -> new ResourceNotFoundException("Evento" + "id"+eventoId));
		//evento.setAsociacion(detallesEvento.getAsociacion());
		evento.setEstado(detallesEvento.getEstado());
		evento.setFecha(detallesEvento.getFecha());
		evento.setNombre(detallesEvento.getNombre());
		evento.setRecurrencia(detallesEvento.getRecurrencia());
		evento.setPlantilla(detallesEvento.getPlantilla());
		 Set<CondicionRequest> condicionRequest = detallesEvento.getCondicionesEvento();
		    Set<Condicion> condiciones =  new HashSet<Condicion>();
		    condicionRequest.forEach(condicion->{
		    	if(condicion.getCondicion().contains("genero")) {
		    	String parametro = "FEMENINO";
	    		if(condicion.getParametro().equals('1')) parametro = "MASCULINO";
	    		condiciones.add(new Condicion(new CondicionId(evento.getId(),condicion.getCondicion(),parametro), evento));
	    		}else {
	    	condiciones.add(new Condicion(new CondicionId(evento.getId(),condicion.getCondicion(),condicion.getParametro()), evento));
	    	}
	    });
		    Set<CondicionRequest> result = condicionRequest.stream()
		    		.filter(item -> item.getCondicion().equals("asociacion")).collect(Collectors.toSet());
		    if(result.isEmpty()) {
	    	Set<Asociacion> as = usuarioService.getAsociacionUsuarioById(usuarioId);
	    	as.forEach(asociacion ->{
	    		condiciones.add(new Condicion(new CondicionId(evento.getId(),"asociacion",String.valueOf(asociacion.getId())), evento));
	    	});
	    }
		evento.setCondicionesEvento(condiciones);
		Evento updatedEvento = eventoRepository.save(evento);
		LogTransacciones transaccion = new LogTransacciones("Editar evento:"+evento.getId()+" "+evento.getNombre());
		transaccionesService.createTransaccion(usuarioId, transaccion);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(updatedEvento);
		List<EventoResponse> eventoResponse = getEventoResponseFormat(eventos);
	    return eventoResponse.get(0);
	}
	
	public List<CondicionResponse> getConditionsForUser(Integer id){
		List<CondicionResponse> condiciones = new ArrayList<CondicionResponse>();
		Usuario usuario = usuarioRepository.findById(id).orElse(null);
		Set<Asociacion> asociaciones = usuario.getAsociacionPorUsuario();
		List<ValorResponse> valoresFecha = new ArrayList<ValorResponse>();
		valoresFecha.add(new ValorResponse(1,"cumpleaños"));
		List<ParametroResponse> parametrosFecha = new ArrayList<ParametroResponse>();
		parametrosFecha.add(new ParametroResponse(1,valoresFecha));
		condiciones.add(new CondicionResponse("fecha_nacimiento", parametrosFecha));
		List<ValorResponse> valoresGenero = new ArrayList<ValorResponse>();
		valoresGenero.add(new ValorResponse(1,"MASCULINO"));
		valoresGenero.add(new ValorResponse(2,"FEMENINO"));
		List<ParametroResponse> parametrosGenero = new ArrayList<ParametroResponse>();
		parametrosGenero.add(new ParametroResponse(1,valoresGenero));
		condiciones.add(new CondicionResponse("genero", parametrosGenero));
		if(asociaciones != null) {
			List<ValorResponse> valoresAsociacion = new ArrayList<ValorResponse>();
			List<ParametroResponse> parametrosPrograma = new ArrayList<ParametroResponse>();
			List<ParametroResponse> parametrosAsociacion = new ArrayList<ParametroResponse>();
			asociaciones.forEach(asociacion ->{
				List<ProgramaAcademico> programas = new ArrayList<ProgramaAcademico>();
				valoresAsociacion.add(new ValorResponse(asociacion.getId(),asociacion.getNombre()));
				programas = programaAcademicoService.findByProgramaAcademicoPorAsociacion(asociacion);
				List<ValorResponse> valoresPrograma = new ArrayList<ValorResponse>();
				programas.forEach(programa ->{
					valoresPrograma.add(new ValorResponse(programa.getCodigo(),programa.getNombre()));
				});
				parametrosPrograma.add(new ParametroResponse(asociacion.getId(),valoresPrograma));
			});
			condiciones.add(new CondicionResponse("programa_academico", parametrosPrograma));
			parametrosAsociacion.add(new ParametroResponse(1,valoresAsociacion));
			condiciones.add(new CondicionResponse("asociacion", parametrosAsociacion));
		}
		List<Vinculacion> vinculaciones= vinculacionService.getAllVinculaciones();
		List<ParametroResponse> parametrosVincuacion = new ArrayList<ParametroResponse>();
		if(vinculaciones != null) {
			List<ValorResponse> valoresVinculacion = new ArrayList<ValorResponse>();
			vinculaciones.forEach(vinculacion ->{
				valoresVinculacion.add(new ValorResponse(vinculacion.getId(),vinculacion.getNombre()));
			});
			parametrosVincuacion.add(new ParametroResponse(1,valoresVinculacion));
			condiciones.add(new CondicionResponse("vinculacion", parametrosVincuacion));
		}
		return condiciones;
	}
	
	public EventoResponse getEventoById(Integer eventoId) {
		Evento evento = eventoRepository.findById(eventoId)
	            .orElseThrow(() -> new ResourceNotFoundException("Evento"+"id"+eventoId));
	    return getEventoResponseFormate(evento);
	}
	public EventoResponse getEventoResponseFormate(Evento evento) {
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(evento);
		List<EventoResponse> eventoResponse = getEventoResponseFormat(eventos);
		return eventoResponse.get(0);
	}
	
	public EventoResponse deactivateEvento(Integer eventoId, Integer usuarioId) {
		Evento evento = eventoRepository.findById(eventoId)
	            .orElseThrow(() -> new ResourceNotFoundException("Evento"+"id"+eventoId));
		evento.setEstado("INACTIVO");
		Evento nuevo = eventoRepository.save(evento);
		LogTransacciones transaccion = new LogTransacciones("Editar evento:"+nuevo.getId()+"desactivar");
		transaccionesService.createTransaccion(usuarioId, transaccion);
		return getEventoResponseFormate(nuevo);
	}
	
	public EventoResponse activateEvento(Integer eventoId, Integer usuarioId) {
		Evento evento = eventoRepository.findById(eventoId)
	            .orElseThrow(() -> new ResourceNotFoundException("Evento"+"id"+eventoId));
		evento.setEstado("ACTIVO");
		Evento nuevo = eventoRepository.save(evento);
		LogTransacciones transaccion = new LogTransacciones("Editar evento:"+nuevo.getId()+"activar");
		transaccionesService.createTransaccion(usuarioId, transaccion);
		return getEventoResponseFormate(nuevo);
	}
	
	public ResponseEntity<?> deleteEvento(Integer eventoId, Integer usuarioId) {
		Evento evento = eventoRepository.findById(eventoId)
	            .orElseThrow(() -> new ResourceNotFoundException("Evento"+"id"+eventoId));

		eventoRepository.delete(evento);
		return ResponseEntity.ok().build();
	}
	
	
	public List<EventoResponse> getEventoResponseFormat(List<Evento> eventos){
		List<EventoResponse> eventoResponse = new ArrayList<EventoResponse>();
		List<CondicionResponse> condicionesResponse = new ArrayList<CondicionResponse>();
		eventos.forEach(evento ->{
			List<ValorResponse> valoresGenero = new ArrayList<ValorResponse>();
			List<ValorResponse> valoresFecha = new ArrayList<ValorResponse>();
			List<ValorResponse> valoresAsociacion = new ArrayList<ValorResponse>();
			List<ValorResponse> valoresVinculacion = new ArrayList<ValorResponse>();
			List<ParametroResponse> parametrosGenero = new ArrayList<ParametroResponse>();
			List<ParametroResponse> parametrosFecha = new ArrayList<ParametroResponse>();
			List<ParametroResponse> parametrosAsociacion = new ArrayList<ParametroResponse>();
			List<ParametroResponse> parametrosVinculacion = new ArrayList<ParametroResponse>();
			List<ParametroResponse> parametrosPrograma = new ArrayList<ParametroResponse>();
			List<Asociacion> asociaciones = new ArrayList<Asociacion>();
			List<ProgramaAcademico> programas = new ArrayList<ProgramaAcademico>();

			Set<Condicion> condicionesEvento = evento.getCondicionesEvento();
			if(condicionesEvento.isEmpty()) eventoResponse.add(new EventoResponse(evento.getId(), evento.getNombre(), evento.getFecha(), evento.getEstado(), evento.getRecurrencia(), evento.getPlantilla()));
			condicionesEvento.forEach(condicion ->{
				if(condicion.getId().getCondicion().contains("asociacion")) {
					Asociacion tempAsociacion = asociacionService.getAsociacionById(Integer.parseInt(condicion.getId().getParametro()));
					asociaciones.add(tempAsociacion);
					valoresAsociacion.add(new ValorResponse(tempAsociacion.getId(), tempAsociacion.getNombre()));
				}
				else if(condicion.getId().getCondicion().contains("vinculacion")) {
					Vinculacion tempVinculacion = vinculacionService.getVinculacionById(Integer.parseInt(condicion.getId().getParametro()));
					valoresVinculacion.add(new ValorResponse(tempVinculacion.getId(), tempVinculacion.getNombre()));
				}
				else if(condicion.getId().getCondicion().contains("programa_academico")) {
					ProgramaAcademico tempProgramaAcademico = programaAcademicoService.getProgramaAcademicoById(Integer.parseInt(condicion.getId().getParametro()));
					programas.add(tempProgramaAcademico);
				}
				else if(condicion.getId().getCondicion().contains("fecha_nacimiento")) {
					valoresFecha.add(new ValorResponse(1, "cumpleaños"));
				}
				else if(condicion.getId().getCondicion().contains("genero")) {
					if(condicion.getId().getParametro().contains("MASCULINO")) valoresGenero.add(new ValorResponse(1, "MASCULINO"));
					else valoresGenero.add(new ValorResponse(2, "FEMENINO"));
				}
			});
			
			if(!asociaciones.isEmpty()) {
				asociaciones.forEach(asociacion ->{
					List<ValorResponse> valoresPrograma = new ArrayList<ValorResponse>();
					programas.forEach(programa ->{
						List<Asociacion> tempAsociaciones = asociacionService.getAsociacionByProgramaAcademico(programa);
						tempAsociaciones.forEach(tempAsociacion ->{
							if(tempAsociacion.getId()==asociacion.getId()) {
								valoresPrograma.add(new ValorResponse(programa.getCodigo(),programa.getNombre()));
							}
						});
					});
					parametrosPrograma.add(new ParametroResponse(asociacion.getId(),valoresPrograma));
				});
			}
			parametrosGenero.add(new ParametroResponse(1,valoresGenero));
			parametrosFecha.add(new ParametroResponse(1,valoresFecha));
			parametrosAsociacion.add(new ParametroResponse(1,valoresAsociacion));
			parametrosVinculacion.add(new ParametroResponse(1,valoresVinculacion));
			condicionesResponse.add(new CondicionResponse("genero", parametrosGenero));
			condicionesResponse.add(new CondicionResponse("fecha_nacimiento", parametrosFecha));
			condicionesResponse.add(new CondicionResponse("programa_academico", parametrosPrograma));
			condicionesResponse.add(new CondicionResponse("vinculacion", parametrosVinculacion));
			condicionesResponse.add(new CondicionResponse("asociacion", parametrosAsociacion));
			eventoResponse.add(new EventoResponse(evento.getId(), evento.getNombre(), evento.getFecha(), evento.getEstado(), evento.getRecurrencia(), evento.getPlantilla(), 
					condicionesResponse));
		});
		return eventoResponse;
	}
}
