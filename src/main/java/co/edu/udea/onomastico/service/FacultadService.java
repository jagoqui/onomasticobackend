package co.edu.udea.onomastico.service;

import java.util.List;

import co.edu.udea.onomastico.model.Facultad;
import co.edu.udea.onomastico.repository.FacultadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.repository.ProgramaAcademicoRepository;

@Service
public class FacultadService {

    @Autowired
    FacultadRepository facultadRepository;

    public List<Facultad> getAllFacultades() {
        return facultadRepository.findAll();
    }

    public Facultad addFacultad(Facultad facultad) {
        facultadRepository.save(facultad);
        return facultad;
    }

    public ResponseEntity<?> deleteFacultad(Integer facultadId) {
        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new ResourceNotFoundException("facultad"+"id"+facultadId));
        facultadRepository.delete(facultad);
        return ResponseEntity.ok().build();
    }



}
