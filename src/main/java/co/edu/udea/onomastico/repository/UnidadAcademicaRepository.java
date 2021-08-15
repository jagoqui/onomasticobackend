package co.edu.udea.onomastico.repository;

import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.UnidadAcademica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UnidadAcademicaRepository extends JpaRepository<UnidadAcademica, Integer> {

    public Optional<UnidadAcademica> findById(Integer id);

    @Query("select p from ProgramaAcademico p where p.unidadAcademica.id = ?1")
    public List<ProgramaAcademico> findProgramasAcademicosByUnidadAcademica(Integer idUnidadAcademica);

    //@Query("select p from ProgramaAcademico p where p.unidadAcademica.id = :unidad")
    //public List<ProgramaAcademico> findProgramasAcademicosByUnidadAcademicaPag(@Param("unidad") Integer idUnidadAcademica, Pageable pageable);


}
