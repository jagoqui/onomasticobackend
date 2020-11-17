package co.edu.udea.onomastico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.model.UsuarioCorreoId;

@Repository
public interface UsuarioCorreoRepository extends JpaRepository<UsuarioCorreo, UsuarioCorreoId> {
	
}
