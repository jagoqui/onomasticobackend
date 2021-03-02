package co.edu.udea.onomastico.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Evento;
import co.edu.udea.onomastico.model.LogTransacciones;
import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.Usuario;
import co.edu.udea.onomastico.model.Vinculacion;
import co.edu.udea.onomastico.payload.CondicionResponse;
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
	ProgramaAcademicoRepository programaAcademicoRepository;
	
	@Autowired
	VinculacionRepository vinculacionRepository;
	
	@Autowired
	LogTransaccionesService transaccionesService;

	public List<Evento> findAllEventos(){
		return eventoRepository.findAll();
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
				programas = programaAcademicoRepository.findByProgramaAcademicoPorAsociacion(asociacion);
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
		List<Vinculacion> vinculaciones= vinculacionRepository.findAll();
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
	public Evento AddEvento(Evento evento, Integer usuarioId) {
	    Evento newEvento = eventoRepository.save(evento);
	    LogTransacciones transaccion = new LogTransacciones("Añadir evento:"+evento.toString());
		transaccionesService.createTransaccion(usuarioId, transaccion);
	    return newEvento;
	}
	public  Evento updateEvento(Integer eventoId, Evento detallesEvento, Integer usuarioId) {
		Evento  evento =  eventoRepository.findById(eventoId).orElseThrow(() -> new ResourceNotFoundException("Evento" + "id"+eventoId));
		//evento.setAsociacion(detallesEvento.getAsociacion());
		evento.setEstado(detallesEvento.getEstado());
		evento.setFecha(detallesEvento.getFecha());
		evento.setNombre(detallesEvento.getNombre());
		evento.setRecurrencia(detallesEvento.getRecurrencia());
		evento.setPlantilla(detallesEvento.getPlantilla());
		evento.setCondicionesEvento(detallesEvento.getCondicionesEvento());
		Evento updatedEvento = eventoRepository.save(evento);
		LogTransacciones transaccion = new LogTransacciones("Editar evento:"+evento.toString());
		transaccionesService.createTransaccion(usuarioId, transaccion);
		return updatedEvento;
	}
	
	public Evento getEventoById(Integer eventoId) {
	    return eventoRepository.findById(eventoId)
	            .orElseThrow(() -> new ResourceNotFoundException("Evento"+"id"+eventoId));
	}
	
	public List<Evento> getAllEventos(Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Evento> pagedResult =  eventoRepository.findAll(paging);
        if(pagedResult.hasContent()) return pagedResult.getContent();
        else return new ArrayList<Evento>();
    }
}
