package co.edu.udea.onomastico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.UsuarioCorreo;

@Repository
public interface AsociacionRepository extends JpaRepository<Asociacion, Integer>{

	//Asociacion findByprogramasAcademicos(ProgramaAcademico programaAcademico);
}
