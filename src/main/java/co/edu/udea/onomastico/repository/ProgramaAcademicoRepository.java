package co.edu.udea.onomastico.repository;

import java.util.List;

import co.edu.udea.onomastico.model.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.ProgramaAcademico;

@Repository
public interface ProgramaAcademicoRepository extends JpaRepository<ProgramaAcademico, Integer>{

	List<ProgramaAcademico> findByFacultad(Facultad facultad);
}
