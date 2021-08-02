package co.edu.udea.onomastico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.UnidadAdministrativa;
import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.model.UsuarioCorreoId;
import co.edu.udea.onomastico.model.Vinculacion;

@Repository
public interface UsuarioCorreoRepository extends JpaRepository<UsuarioCorreo, UsuarioCorreoId> {
	
	Optional<UsuarioCorreo> findByEmail(String email);
	
	Optional<UsuarioCorreo> findById(UsuarioCorreoId usuarioCorreoId);
	
	List<UsuarioCorreo> findByGenero(String genero);
	
	List<UsuarioCorreo> findByUnidadAdministrativaPorCorreoUsuario(UnidadAdministrativa unidadAdministrativa);
	
	List<UsuarioCorreo> findByProgramaAcademicoPorUsuarioCorreo(ProgramaAcademico programaAcademico);
	
	List<UsuarioCorreo> findByVinculacionPorUsuarioCorreo(Vinculacion vinculacion);
	
	@Query(value = "SELECT * FROM usuario_correo Where DAY(fecha_nacimiento)= DAY(CURRENT_DATE) AND Month(fecha_nacimiento)=Month(CURRENT_DATE) AND estado='ACTIVO'", nativeQuery = true)
	List<UsuarioCorreo> findByBirthday();
}
