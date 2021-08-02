package co.edu.udea.onomastico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.UnidadAdministrativa;
import co.edu.udea.onomastico.model.Rol;
import co.edu.udea.onomastico.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	Optional<Usuario> findByEmail(String email);
	
	Optional<Usuario> findByResetToken(String resetToken);
	
	List<Usuario> findByIdIn(List<Long> usuariosIds);

	Boolean existsByEmail(String email);
	
	List<Usuario> findByUnidadAdministrativaPorUsuario(UnidadAdministrativa unidadAdministrativa);
	
	List<Usuario> findByRol(Rol rol);
}
