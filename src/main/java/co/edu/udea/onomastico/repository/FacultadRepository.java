package co.edu.udea.onomastico.repository;

import co.edu.udea.onomastico.model.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.ProgramaAcademico;

import java.util.List;

@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Integer> {

    List<Facultad> findAll();
}
