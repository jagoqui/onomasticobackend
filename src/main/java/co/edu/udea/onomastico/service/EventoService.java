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
import co.edu.udea.onomastico.model.Usuario;
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
	
	public List<Object> getConditionsForUser(Integer id){
		List<Object> condiciones = new ArrayList<Object>();
		List<Object> programas = new ArrayList<Object>();
		Usuario usuario = usuarioRepository.findById(id).orElse(null);
		Set<Asociacion> asociaciones = usuario.getAsociacionPorUsuario();
		condiciones.add(usuario.getAsociacionPorUsuario());
		if(asociaciones != null) {
			asociaciones.forEach(asociacion ->{
				programas.add(programaAcademicoRepository.findByProgramaAcademicoPorAsociacion(asociacion));
			});
		}
		condiciones.add(programas);
		condiciones.add(vinculacionRepository.findAll());
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
		evento.setAsociacion(detallesEvento.getAsociacion());
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
