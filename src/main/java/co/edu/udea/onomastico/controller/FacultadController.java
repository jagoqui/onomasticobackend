package co.edu.udea.onomastico.controller;

import java.util.List;

import co.edu.udea.onomastico.model.Facultad;
import co.edu.udea.onomastico.service.FacultadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.service.ProgramaAcademicoService;

@RestController
public class FacultadController {

    @Autowired
    FacultadService facultadService;


    @GetMapping("/facultades")
    @JsonView(Views.Public.class)
    public List<Facultad> getAllFacultades() {
        return facultadService.getAllFacultades();
    }


    @PostMapping("/facultad")
    @JsonView(Views.Public.class)
    public Facultad addFacultad(@RequestBody Facultad facultad) {
        return facultadService.addFacultad(facultad);
    }

    @DeleteMapping("/facultad/{id}")
    public ResponseEntity<?> deleteProgramaAcademico(@PathVariable(value = "id") Integer facultadId) {
        return facultadService.deleteFacultad(facultadId);
    }


}
