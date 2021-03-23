package co.edu.udea.onomastico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.Evento;
import co.edu.udea.onomastico.model.Plantilla;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer>{
	
	List<Evento> findEventoByPlantilla(Plantilla plantilla);

}
