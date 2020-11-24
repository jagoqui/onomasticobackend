package co.edu.udea.onomastico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.model.UsuarioCorreoId;

@Repository
public interface UsuarioCorreoRepository extends JpaRepository<UsuarioCorreo, UsuarioCorreoId> {
	Optional<UsuarioCorreo> findByEmail(String email);
	
	Optional<UsuarioCorreo> findById(UsuarioCorreoId usuarioCorreoId);
}
