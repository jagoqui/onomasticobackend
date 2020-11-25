package co.edu.udea.onomastico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udea.onomastico.model.ImagenArchivo;

@Repository
public interface ImagenArchivoRepository extends JpaRepository<ImagenArchivo, Integer>{

}
