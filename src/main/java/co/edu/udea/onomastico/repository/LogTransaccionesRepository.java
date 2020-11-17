package co.edu.udea.onomastico.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.LogTransacciones;

@Repository
public interface LogTransaccionesRepository extends JpaRepository<LogTransacciones, Integer> {
	 Page<LogTransacciones> findByUsuarioId(Integer usuarioId, Pageable pageable);
	 Optional<LogTransacciones> findByIdAndUsuarioId(Integer id, Integer UsuarioId);
}
