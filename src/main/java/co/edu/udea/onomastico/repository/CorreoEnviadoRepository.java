package co.edu.udea.onomastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.CorreoEnviado;
import co.edu.udea.onomastico.model.CorreoEnviadoId;

import java.util.Date;
import java.util.List;

@Repository
public interface CorreoEnviadoRepository extends JpaRepository<CorreoEnviado, CorreoEnviadoId>{

    @Query(value = "select c from correo_enviado c where fecha BETWEEN :first AND :last", nativeQuery = true)
    List<CorreoEnviado> findCorreosPorFechas(@Param("first")Date first, @Param("last") Date last);
}
