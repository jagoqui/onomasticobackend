package co.edu.udea.onomastico.service;

import java.util.*;
import java.util.stream.Collectors;

import co.edu.udea.onomastico.model.*;
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
import co.edu.udea.onomastico.payload.CondicionRequest;
import co.edu.udea.onomastico.payload.CondicionResponse;
import co.edu.udea.onomastico.payload.EventoRequest;
import co.edu.udea.onomastico.payload.ParametroResponse;
import co.edu.udea.onomastico.repository.CondicionRepository;
import co.edu.udea.onomastico.repository.EventoRepository;
import co.edu.udea.onomastico.repository.UsuarioRepository;

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
	UnidadAdministrativaService unidadAdministrativaService;
	
	@Autowired
	VinculacionService vinculacionService;
	
	@Autowired
	ProgramaAcademicoService programaAcademicoService;

	@Autowired
	UnidadAcademicaService unidadAcademicaService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	PlantillaService plantillaService;

	public List<Evento> getAllEventos() {
	    return eventoRepository.findAll();
	}
	public List<EventoRequest> getAllEventosByUsuario(Integer usuarioId){
		Set<UnidadAdministrativa> asociaciones = usuarioService.getUnidadAdministrativaUsuarioById(usuarioId);
		List<Evento> eventos = getAllEventosByAsociacion(asociaciones);
		return getEventoResponseFormat(eventos);
	}
	
	public List<EventoRequest> getAllEventosByUsuarioPag(Integer usuarioId, Integer pageNo, Integer pageSize, String sortBy){
		Set<UnidadAdministrativa> asociaciones = usuarioService.getUnidadAdministrativaUsuarioById(usuarioId);
		List<Evento> eventosAsociacion = getAllEventosByAsociacion(asociaciones);
		List<EventoRequest> eventos = getEventoResponseFormat(eventosAsociacion);
		Pageable paging;
		if(sortBy!=null)paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        else paging = PageRequest.of(pageNo, pageSize);
		final int start = (int)paging.getOffset();
		final int end = Math.min((start + paging.getPageSize()), eventos.size());
		final Page<EventoRequest> page = new PageImpl<>(eventos.subList(start, end), paging, eventos.size());
		return page.toList();
	}
	
	public List<Evento> getAllEventosByAsociacion(Set<UnidadAdministrativa> asociaciones) {
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
	
	public List<EventoRequest> getAllEventos(Integer pageNo, Integer pageSize, String sortBy){
		Pageable paging;
		if(sortBy!=null)paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        else paging = PageRequest.of(pageNo, pageSize);
        Page<Evento> pagedResult =  eventoRepository.findAll(paging);
        List<Evento> eventos = pagedResult.getContent();
        if(!pagedResult.isEmpty()) return getEventoResponseFormat(eventos);
        else return new ArrayList<EventoRequest>();
    }
	
	public List<EventoRequest> findAllEventosResponse(){
		List<Evento> eventos = eventoRepository.findAll();
		return getEventoResponseFormat(eventos);
	}
	public  List<EventoRequest> getEventosByPlantilla(Plantilla plantilla) {
		List<Evento> eventos = eventoRepository.findEventoByPlantilla(plantilla);
		return getEventoResponseFormat(eventos);
	}
	
	public boolean isValidCondicionesEvento(EventoRequest eventoRequest, Integer userId){
		List<Object> unidades = usuarioService.getUnidadesPorUsuario(userId);
		Set<UnidadAcademica> unidadesAcademicas = usuarioService.getUnidadAcademicaUsuarioById(userId);
		Set<UnidadAdministrativa> unidadesAdministrativas = usuarioService.getUnidadAdministrativaUsuarioById(userId);

		if(!(eventoRequest.getEstado().equals("ACTIVO") || eventoRequest.getEstado().equals("INACTIVO"))) return false;
		if(!(eventoRequest.getRecurrencia().equals("DIARIA") || eventoRequest.getRecurrencia().equals("ANUAL")))return false;
		if(!plantillaService.existsPlantilla(eventoRequest.getPlantilla().getId()))return false;
		List<CondicionRequest> condicionRequest = eventoRequest.getCondicionesEvento();
		for(CondicionRequest condicion: condicionRequest){
			if(condicion.getCondicion().contains("unidad_administrativa")) {
				UnidadAdministrativa unidadAdminConstruida = unidadAdministrativaService.getUnidadAdministrativaById(Integer.parseInt(condicion.getId()));
				if(!unidadesAdministrativas.contains(unidadAdminConstruida)) return false;
			}
			else if(condicion.getCondicion().contains("unidad_academica")){
				UnidadAcademica unidadAcadConstruida = unidadAcademicaService.getUnidadAcademicaById(Integer.parseInt(condicion.getId()));
				if(!unidadesAcademicas.contains(unidadAcadConstruida)) return false;
			}
			else if(condicion.getCondicion().contains("programa_academico")) {
				ProgramaAcademico programaConstruido = programaAcademicoService.getProgramaAcademicoById(Integer.parseInt(condicion.getId()));
				if(!unidadesAcademicas.contains(programaConstruido.getUnidadAcademica())) return false;
			}else if(condicion.getCondicion().contains("vinculacion")) {
				if(!vinculacionService.existsVinculacion(Integer.parseInt(condicion.getId()))) return false;
			}
			else if(!(condicion.getCondicion().contains("vinculacion") ||
					condicion.getCondicion().contains("unidad_administrativa") ||
					condicion.getCondicion().contains("unidad_academica") ||
					condicion.getCondicion().contains("programa_academico") ||
					condicion.getCondicion().contains("genero") ||
					condicion.getCondicion().contains("fecha_nacimiento"))) {
				return false;
			}
		}
		return true;
	}



	public EventoRequest AddEvento(EventoRequest eventoRequest, Integer usuarioId) throws BadRequestException{
	    if(isValidCondicionesEvento(eventoRequest, usuarioId)==false)throw new BadRequestException("Condiciones incorrectas");
		Evento evento = EventoRequest.toModelCreate(eventoRequest);
	    evento = eventoRepository.save(evento);
	    Integer newEventoId = evento.getId();
	    evento.setCondicionesEvento(setCondiciones(eventoRequest.getCondicionesEvento(),newEventoId, usuarioId, evento));
	    Evento newEvento = eventoRepository.save(evento);
		EventoRequest eventoResponse = getEventoResponseFormate(newEvento);
		if(eventoResponse!=null){
			String transaccion = "Añadir evento:"+newEvento.getId()+" "+newEvento.getNombre();
			transaccionesService.createTransaccion(usuarioId, transaccion);
			return eventoResponse;
		}
		else{
			eventoRepository.delete(newEvento);
			throw new BadRequestException("Bad Conditions");
		}
	}

	public  EventoRequest updateEvento(Integer eventoId, EventoRequest detallesEvento, Integer usuarioId) {
		if(isValidCondicionesEvento(detallesEvento, usuarioId)==false)throw new BadRequestException("Condiciones incorrectas");
		Evento  oldEvento =  eventoRepository.findById(eventoId).orElseThrow(() -> new ResourceNotFoundException("Evento" + "id"+eventoId));
		Evento evento = EventoRequest.toModelCreate(detallesEvento);
		evento.setId(oldEvento.getId());
		evento.setCondicionesEvento(setCondiciones(detallesEvento.getCondicionesEvento(),evento.getId(), usuarioId, evento));
		Evento updatedEvento = eventoRepository.save(evento);
		EventoRequest eventoResponse = getEventoResponseFormate(evento);
		if(eventoResponse!=null){
			String transaccion = "Editar evento:"+evento.getId()+" "+evento.getNombre();
			transaccionesService.createTransaccion(usuarioId, transaccion);
			return eventoResponse;
		}
		else{
			eventoRepository.delete(evento);
			throw new BadRequestException("Bad Conditions");
		}
	}

	public List<Condicion> setCondiciones(List<CondicionRequest> condicionRequest, Integer newEventoId, Integer usuarioId, Evento newEvento) {
		List<Condicion> condiciones = new ArrayList<>();
		for(CondicionRequest condicion: condicionRequest){
	    	if(condicion.getCondicion().contains("genero")) {
	    		String parametro = "FEMENINO";
	    		if(condicion.getId().contains("1")) parametro = "MASCULINO";
	    		condiciones.add(new Condicion(new CondicionId(newEventoId,condicion.getCondicion(),parametro)));
	    	}else {
	    		condiciones.add(new Condicion(new CondicionId(newEventoId,condicion.getCondicion(),condicion.getId())));
	    	}
	    }
		Set<Condicion> resultsProgramas = condiciones.stream().filter(item -> item.getId().getCondicion().equals("programa_academico")).collect(Collectors.toSet());
		Set<Condicion> results = condiciones.stream().filter(item -> item.getId().getCondicion().equals("unidad_administrativa")).collect(Collectors.toSet());

		if(!resultsProgramas.isEmpty()) return condiciones;
	    if(results.isEmpty()) {
			Set<UnidadAdministrativa> uad = usuarioService.getUnidadAdministrativaUsuarioById(usuarioId);
	    	uad.forEach(unidadAdministrativa ->{
	    		condiciones.add(new Condicion(new CondicionId(newEventoId,"unidad_administrativa",String.valueOf(unidadAdministrativa.getId()))));
	    	});
		}
		results = condiciones.stream().filter(item -> item.getId().getCondicion().equals("unidad_academica")).collect(Collectors.toSet());

		if(results.isEmpty()) {
			Set<UnidadAcademica> uac = usuarioService.getUnidadAcademicaUsuarioById(usuarioId);
			uac.forEach(unidadAcademica ->{
				condiciones.add(new Condicion(new CondicionId(newEventoId,"unidad_academica",String.valueOf(unidadAcademica.getId()))));
			});
		}

		return condiciones;
	}
	
	public List<CondicionResponse> getConditionsForUser(Integer id){
		List<CondicionResponse> condiciones = new ArrayList<>();
		Usuario usuario = usuarioRepository.findById(id).orElse(null);

		Set<UnidadAdministrativa> unidadesAdministrativas = usuario.getUnidadAdministrativaPorUsuario();
		// lista con la mezcla de unidades
		List<Object> unidades = new ArrayList<Object>();
		unidades.addAll(unidadesAdministrativas);

		List<ParametroResponse> parametrosFecha = new ArrayList<ParametroResponse>();
		parametrosFecha.add(new ParametroResponse(1,"fecha_nacimiento","cumpleaños"));
		condiciones.add(new CondicionResponse("Fecha de nacimiento", parametrosFecha));
		List<ParametroResponse> parametrosGenero = new ArrayList<ParametroResponse>();
		parametrosGenero.add(new ParametroResponse(1,"genero","MASCULINO"));
		parametrosGenero.add(new ParametroResponse(2,"genero","FEMENINO"));
		condiciones.add(new CondicionResponse("Género", parametrosGenero));

		if(unidadesAdministrativas != null) {
			List<ParametroResponse> parametrosAsociacion = new ArrayList<ParametroResponse>();
			unidadesAdministrativas.forEach(asociacion ->{
				parametrosAsociacion.add(new ParametroResponse(asociacion.getId(),"unidad_administrativa",asociacion.getNombre()));
			});
			condiciones.add(new CondicionResponse("Unidad Administrativa", parametrosAsociacion));
		}

		Set<UnidadAcademica> unidadesAcademicas = usuario.getUnidadAcademicaPorUsuario();

		List<ParametroResponse> parametrosUnidadAcademica =  new ArrayList<ParametroResponse>();
		if(unidadesAcademicas != null){
			unidadesAcademicas.forEach(unidadAcademica -> {
				if(unidadAcademicaService.existsUnidadAcademica(unidadAcademica.getId())){
					parametrosUnidadAcademica.add(new ParametroResponse(unidadAcademica.getId(), "unidad_academica", unidadAcademica.getNombre()));
					unidades.add(unidadAcademica);
				}
			});
			condiciones.add(new CondicionResponse("Unidad Académica", parametrosUnidadAcademica));
		}

		List<ProgramaAcademico> programasAcademicos = programaAcademicoService.getAllProgramasAcademicos();
		List<ParametroResponse> parametrosProgramaAcademico = new ArrayList<ParametroResponse>();
		if(programasAcademicos != null) {
			programasAcademicos.forEach(programaAcademico ->{
				parametrosProgramaAcademico.add(new ParametroResponse(programaAcademico.getCodigo(),"programa_academico",programaAcademico.getNombre()));
			});
			condiciones.add(new CondicionResponse("Programa académico", parametrosProgramaAcademico));
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
	
	public EventoRequest getEventoById(Integer eventoId) {
		Evento evento = eventoRepository.findById(eventoId)
	            .orElseThrow(() -> new ResourceNotFoundException("Evento"+"id"+eventoId));
	    return getEventoResponseFormate(evento);
	}
	public EventoRequest getEventoResponseFormate(Evento evento) {
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(evento);
		List<EventoRequest> eventoResponse = getEventoResponseFormat(eventos);
		return eventoResponse.get(0);
	}
	
	public EventoRequest deactivateEvento(Integer eventoId, Integer usuarioId) {
		Evento evento = eventoRepository.findById(eventoId)
	            .orElseThrow(() -> new ResourceNotFoundException("Evento"+"id"+eventoId));
		evento.setEstado("INACTIVO");
		Evento nuevo = eventoRepository.save(evento);
		String transaccion = "Editar evento:"+nuevo.getId()+"desactivar";
		transaccionesService.createTransaccion(usuarioId, transaccion);
		return getEventoResponseFormate(nuevo);
	}
	
	public EventoRequest activateEvento(Integer eventoId, Integer usuarioId) {
		Evento evento = eventoRepository.findById(eventoId)
	            .orElseThrow(() -> new ResourceNotFoundException("Evento"+"id"+eventoId));
		evento.setEstado("ACTIVO");
		Evento nuevo = eventoRepository.save(evento);
		String transaccion = "Editar evento:"+nuevo.getId()+"activar";
		transaccionesService.createTransaccion(usuarioId, transaccion);
		return getEventoResponseFormate(nuevo);
	}
	
	public ResponseEntity<?> deleteEvento(Integer eventoId, Integer usuarioId) {
		Evento evento = eventoRepository.findById(eventoId)
	            .orElseThrow(() -> new ResourceNotFoundException("Evento"+"id"+eventoId));

		eventoRepository.delete(evento);
		return ResponseEntity.ok().build();
	}
	
	
	public List<EventoRequest> getEventoResponseFormat(List<Evento> eventos){
		List<EventoRequest> eventoResponse = new ArrayList<EventoRequest>();
		eventos.forEach(evento ->{
			List<CondicionRequest> condicionesResponse = new ArrayList<>();
			List<UnidadAdministrativa> asociaciones = new ArrayList<UnidadAdministrativa>();
			List<ProgramaAcademico> programas = new ArrayList<ProgramaAcademico>();

			List<Condicion> condicionesEvento = evento.getCondicionesEvento();
			condicionesEvento.forEach(condicion ->{
				if(condicion.getId().getCondicion().contains("asociacion")) {
					UnidadAdministrativa tempUnidadAdministrativa = unidadAdministrativaService.getUnidadAdministrativaById(Integer.parseInt(condicion.getId().getParametro()));
					asociaciones.add(tempUnidadAdministrativa);
					condicionesResponse.add(new  CondicionRequest(String.valueOf(tempUnidadAdministrativa.getId()), "asociacion", tempUnidadAdministrativa.getNombre()));
				}
				else if(condicion.getId().getCondicion().contains("vinculacion")) {
					Vinculacion tempVinculacion = vinculacionService.getVinculacionById(Integer.parseInt(condicion.getId().getParametro()));
					condicionesResponse.add(new  CondicionRequest(String.valueOf(tempVinculacion.getId()), "vinculacion", tempVinculacion.getNombre()));
				}
				else if(condicion.getId().getCondicion().contains("programa_academico")) {
					ProgramaAcademico tempProgramaAcademico = programaAcademicoService.getProgramaAcademicoById(Integer.parseInt(condicion.getId().getParametro()));
					programas.add(tempProgramaAcademico);
				}
				else if(condicion.getId().getCondicion().contains("fecha_nacimiento")) {
					condicionesResponse.add(new  CondicionRequest(String.valueOf(1),"fecha_nacimiento","cumpleaños"));
				}
				else if(condicion.getId().getCondicion().contains("genero")) {
					if(condicion.getId().getParametro().contains("MASCULINO")) {
						condicionesResponse.add(new  CondicionRequest(String.valueOf(1),"genero","MASCULINO"));
					}
					else if(condicion.getId().getParametro().contains("FEMENINO")) {
						condicionesResponse.add(new  CondicionRequest(String.valueOf(2),"genero","FEMENINO"));
					}
				}
			});
			/*if(!asociaciones.isEmpty()) {
				asociaciones.forEach(asociacion ->{
					programas.forEach(programa ->{
						Asociacion asociacionFound = asociacionService.getAsociacionByProgramaAcademico(programa);
						if(asociacion.getId() == asociacionFound.getId()){
							condicionesResponse.add(new  CondicionRequest(String.valueOf(programa.getCodigo()),"programa_academico",
									programa.getNombre().concat(" / ").concat(asociacion.getNombre())));
						}
					});
				});
			}

			 */
			eventoResponse.add(new EventoRequest(evento.getId(), evento.getNombre(), evento.getFecha(), evento.getEstado(), evento.getRecurrencia(), evento.getPlantilla(), 
					condicionesResponse));
		});
		return eventoResponse;
	}
}
