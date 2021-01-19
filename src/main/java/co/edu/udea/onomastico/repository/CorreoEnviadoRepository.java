package co.edu.udea.onomastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.CorreoEnviado;
import co.edu.udea.onomastico.model.CorreoEnviadoId;

@Repository
public interface CorreoEnviadoRepository extends JpaRepository<CorreoEnviado, CorreoEnviadoId>{

}
