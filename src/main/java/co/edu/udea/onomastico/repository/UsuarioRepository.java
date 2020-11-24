package co.edu.udea.onomastico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	Optional<Usuario> findByEmail(String email);
	
	Optional<Usuario> findByNombreOrEmail(String nombreUsuario, String email);
	
	List<Usuario> findByIdIn(List<Long> usuariosIds);
	
	Optional<Usuario> findByNombre(String nombre);

	Boolean existsByNombre(String nombre);

	Boolean existsByEmail(String email);
}
