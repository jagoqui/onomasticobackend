package co.edu.udea.onomastico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Plantilla;

@Repository
public interface PlantillaRepository extends JpaRepository<Plantilla, Integer>{

	List<Plantilla> findByAsociacionesPorPlantilla(Asociacion asociacion);
}
