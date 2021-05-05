package co.edu.udea.onomastico.service;

import java.util.ArrayList;
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

import co.edu.udea.onomastico.exceptions.BadRequestException;
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
		Pageable paging;
		if(sortBy!=null)paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        else paging = PageRequest.of(pageNo, pageSize);
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
		Pageable paging;
		if(sortBy!=null)paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        else paging = PageRequest.of(pageNo, pageSize);
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
	
	public EventoResponse AddEvento(EventoRequest eventoRequest, Integer usuarioId) throws BadRequestException{
		if(!(eventoRequest.getEstado().equals("ACTIVO") || eventoRequest.getEstado().equals("INACTIVO"))) throw new BadRequestException("Estado incorrecto");
		if(!(eventoRequest.getRecurrencia().equals("DIARIA") || eventoRequest.getRecurrencia().equals("ANUAL")))throw new BadRequestException("Recurrencia incorrecta");
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
	    Set<Condicion> condiciones =  setCondiciones(condicionRequest,newEventoId, usuarioId, newEvento);
	    newEvento.setCondicionesEvento(condiciones);
	    eventoRepository.save(newEvento);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(newEvento);
		List<EventoResponse> eventoResponse =new ArrayList<EventoResponse>();
		try {
			eventoResponse = getEventoResponseFormat(eventos);
			LogTransacciones transaccion = new LogTransacciones("Añadir evento:"+newEvento.getId()+" "+newEvento.getNombre());
			transaccionesService.createTransaccion(usuarioId, transaccion);
		}catch(Exception e) {
			eventoRepository.delete(newEvento);
		}
		if(!eventoResponse.isEmpty())return eventoResponse.get(0);
		else throw new BadRequestException("Bad Conditions");
	}
	public  EventoResponse updateEvento(Integer eventoId, EventoRequest detallesEvento, Integer usuarioId) {
		if(!(detallesEvento.getEstado().equals("ACTIVO") || detallesEvento.getEstado().equals("INACTIVO"))) throw new BadRequestException("Estado incorrecto");
		if(!(detallesEvento.getRecurrencia().equals("DIARIA") || detallesEvento.getRecurrencia().equals("ANUAL")))throw new BadRequestException("Recurrencia incorrecta");
		Evento  oldEvento =  eventoRepository.findById(eventoId).orElseThrow(() -> new ResourceNotFoundException("Evento" + "id"+eventoId));
		Evento evento = oldEvento;
		evento.setEstado(detallesEvento.getEstado());
		evento.setFecha(detallesEvento.getFecha());
		evento.setNombre(detallesEvento.getNombre());
		evento.setRecurrencia(detallesEvento.getRecurrencia());
		evento.setPlantilla(detallesEvento.getPlantilla());
		Set<CondicionRequest> condicionRequest = detallesEvento.getCondicionesEvento();
		Set<Condicion> condiciones =   setCondiciones(condicionRequest,evento.getId(), usuarioId, evento);
		evento.setCondicionesEvento(condiciones);
		Evento updatedEvento = eventoRepository.save(evento);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(updatedEvento);
		List<EventoResponse> eventoResponse =new ArrayList<EventoResponse>();
		try {
		eventoResponse = getEventoResponseFormat(eventos);
		LogTransacciones transaccion = new LogTransacciones("Editar evento:"+evento.getId()+" "+evento.getNombre());
		transaccionesService.createTransaccion(usuarioId, transaccion);
		}catch(Exception e) {
			eventoRepository.save(oldEvento);
		}if(!eventoResponse.isEmpty())return eventoResponse.get(0);
		else throw new BadRequestException("Bad Conditions");
	}
	
	public Set<Condicion> setCondiciones(Set<CondicionRequest> condicionRequest, Integer newEventoId, Integer usuarioId, Evento newEvento) {
		Set<Condicion> condiciones =  new HashSet<Condicion>();
		condicionRequest.forEach(condicion->{
	    	if(condicion.getCondicion().contains("genero")) {
	    		String parametro = "FEMENINO";
	    		if(condicion.getId().contains("1")) parametro = "MASCULINO";
	    		condiciones.add(new Condicion(new CondicionId(newEventoId,condicion.getCondicion(),parametro), newEvento));
	    	}else {
	    	condiciones.add(new Condicion(new CondicionId(newEventoId,condicion.getCondicion(),condicion.getId()), newEvento));
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
		return condiciones;
	}
	
	public List<CondicionResponse> getConditionsForUser(Integer id){
		List<CondicionResponse> condiciones = new ArrayList<CondicionResponse>();
		Usuario usuario = usuarioRepository.findById(id).orElse(null);
		Set<Asociacion> asociaciones = usuario.getAsociacionPorUsuario();
		List<ParametroResponse> parametrosFecha = new ArrayList<ParametroResponse>();
		parametrosFecha.add(new ParametroResponse(1,"fecha_nacimiento","cumpleaños"));
		condiciones.add(new CondicionResponse("Fecha de nacimiento", parametrosFecha));
		List<ParametroResponse> parametrosGenero = new ArrayList<ParametroResponse>();
		parametrosGenero.add(new ParametroResponse(1,"genero","MASCULINO"));
		parametrosGenero.add(new ParametroResponse(2,"genero","FEMENINO"));
		condiciones.add(new CondicionResponse("Género", parametrosGenero));
		List<ParametroResponse> parametrosPrograma = new ArrayList<ParametroResponse>();
		if(asociaciones != null) {
			List<ParametroResponse> parametrosAsociacion = new ArrayList<ParametroResponse>();
			asociaciones.forEach(asociacion ->{
				parametrosAsociacion.add(new ParametroResponse(asociacion.getId(),"asociacion",asociacion.getNombre()));
				List<ProgramaAcademico> programas = new ArrayList<ProgramaAcademico>();
				programas = programaAcademicoService.findByProgramaAcademicoPorAsociacion(asociacion);
				programas.forEach(programa ->{
					parametrosPrograma.add(new ParametroResponse(programa.getCodigo(),"programa_academico",
					        programa.getNombre().concat(" / ").concat(asociacion.getNombre())));
				});
			});
			condiciones.add(new CondicionResponse("Programa acádemico", parametrosPrograma));
			condiciones.add(new CondicionResponse("Asociación", parametrosAsociacion));
		}
		List<Vinculacion> vinculaciones= vinculacionService.getAllVinculaciones();
		List<ParametroResponse> parametrosVincuacion = new ArrayList<ParametroResponse>();
		if(vinculaciones != null) {
			vinculaciones.forEach(vinculacion ->{
				parametrosVincuacion.add(new ParametroResponse(vinculacion.getId(),"vinculacion",vinculacion.getNombre()));
			});
			condiciones.add(new CondicionResponse("Vinculación", parametrosVincuacion));
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
					parametrosAsociacion.add(new ParametroResponse
							(tempAsociacion.getId(), "asociacion",tempAsociacion.getNombre()));
				}
				else if(condicion.getId().getCondicion().contains("vinculacion")) {
					Vinculacion tempVinculacion = vinculacionService.getVinculacionById(Integer.parseInt(condicion.getId().getParametro()));
					parametrosVinculacion.add(new ParametroResponse(tempVinculacion.getId(), "vinculacion", tempVinculacion.getNombre()));
				}
				else if(condicion.getId().getCondicion().contains("programa_academico")) {
					ProgramaAcademico tempProgramaAcademico = programaAcademicoService.getProgramaAcademicoById(Integer.parseInt(condicion.getId().getParametro()));
					programas.add(tempProgramaAcademico);
				}
				else if(condicion.getId().getCondicion().contains("fecha_nacimiento")) {
					parametrosFecha.add(new ParametroResponse(1,"fecha_nacimiento","cumpleaños"));
				}
				else if(condicion.getId().getCondicion().contains("genero")) {
					if(condicion.getId().getParametro().contains("MASCULINO")) {
						parametrosGenero.add(new ParametroResponse(1,"genero","MASCULINO"));
					}
					else if(condicion.getId().getParametro().contains("FEMENINO")) {
						parametrosGenero.add(new ParametroResponse(2,"genero","FEMENINO"));
					}
				}
			});
			if(!asociaciones.isEmpty()) {
				asociaciones.forEach(asociacion ->{
					programas.forEach(programa ->{
						List<Asociacion> tempAsociaciones = asociacionService.getAsociacionByProgramaAcademico(programa);
						tempAsociaciones.forEach(tempAsociacion ->{
							if(tempAsociacion.getId()==asociacion.getId()) {
								parametrosPrograma.add(new ParametroResponse(programa.getCodigo(),"programa_academico",
								        programa.getNombre().concat(" / ").concat(asociacion.getNombre())));
							}
						});
					});
				});
			}
			if(!parametrosGenero.isEmpty())condicionesResponse.add(new CondicionResponse("Género", parametrosGenero));
			if(!parametrosFecha.isEmpty())condicionesResponse.add(new CondicionResponse("Fecha de nacimiento", parametrosFecha));
			if(!parametrosPrograma.isEmpty())condicionesResponse.add(new CondicionResponse("Programa acádemico", parametrosPrograma));
			if(!parametrosVinculacion.isEmpty())condicionesResponse.add(new CondicionResponse("Vinculación", parametrosVinculacion));
			if(!parametrosAsociacion.isEmpty())condicionesResponse.add(new CondicionResponse("Asociación", parametrosAsociacion));
			eventoResponse.add(new EventoResponse(evento.getId(), evento.getNombre(), evento.getFecha(), evento.getEstado(), evento.getRecurrencia(), evento.getPlantilla(), 
					condicionesResponse));
		});
		return eventoResponse;
	}
}
