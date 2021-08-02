package co.edu.udea.onomastico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.UnidadAdministrativa;

@Repository
public interface UnidadAdministrativaRepository extends JpaRepository<UnidadAdministrativa, Integer>{

	//Asociacion findByprogramasAcademicos(ProgramaAcademico programaAcademico);

    @Query(value = "SELECT * FROM unidad_administrativa a WHERE nombre = :nombreUnidadAdministrativa",
            nativeQuery = true)
    List<UnidadAdministrativa> findByNombre(@Param("nombreUnidadAdministrativa") String nombreUnidadAdministrativa);



}
