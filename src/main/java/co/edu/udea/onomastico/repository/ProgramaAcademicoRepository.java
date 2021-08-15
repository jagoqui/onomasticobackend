package co.edu.udea.onomastico.repository;

import java.util.List;

import co.edu.udea.onomastico.model.UnidadAcademica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.ProgramaAcademico;

@Repository
public interface ProgramaAcademicoRepository extends JpaRepository<ProgramaAcademico, Integer>{

	List<ProgramaAcademico> findByUnidadAcademica(UnidadAcademica unidadAcademica);


	@Query("select p from ProgramaAcademico p where p.unidadAcademica = :unidadAcademica")
	public List<ProgramaAcademico> findByUnidadAcademica(@Param("unidadAcademica") UnidadAcademica unidadAcademica, Pageable pageable);
}
