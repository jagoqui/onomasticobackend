package co.edu.udea.onomastico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.Condicion;
import co.edu.udea.onomastico.model.CondicionId;

@Repository
public interface CondicionRepository extends JpaRepository<Condicion, CondicionId> {
	
	@Query(value = "SELECT  e.* FROM condicion_por_evento ce JOIN evento e ON e.idevento = ce.evento_idevento WHERE ce.condicion = 'asociacion' AND ce.parametro = ?1", 
			  nativeQuery = true)
	List<Object> getEventosIdByAsociacion(Integer parametro);
	
}
