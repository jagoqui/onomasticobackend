package co.edu.udea.onomastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.Tipografia;

@Repository
public interface TipografiaRepository extends JpaRepository<Tipografia, Integer> {

}
