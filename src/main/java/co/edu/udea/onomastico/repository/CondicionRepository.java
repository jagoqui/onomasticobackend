package co.edu.udea.onomastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.Condicion;

@Repository
public interface CondicionRepository extends JpaRepository<Condicion, Integer> {

}