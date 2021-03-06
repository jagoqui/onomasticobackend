package co.edu.udea.onomastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.Plataforma;

@Repository
public interface PlataformaRepository extends JpaRepository<Plataforma, Integer> {

}
