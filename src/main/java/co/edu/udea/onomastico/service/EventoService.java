package co.edu.udea.onomastico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.model.Evento;
import co.edu.udea.onomastico.repository.EventoRepository;

@Service
public class EventoService {

	@Autowired
	EventoRepository eventoRepository;
	
	public List<Evento> findAllEventos(){
		return eventoRepository.findAll();
	}
}
