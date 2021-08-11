package co.edu.udea.onomastico.repository;

import co.edu.udea.onomastico.model.UnidadAcademica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadAcademicaRepository extends JpaRepository<UnidadAcademica, Integer> {

    public Optional<UnidadAcademica> findById(Integer id);

}
