package co.edu.udea.onomastico.controller;

import java.util.List;

import co.edu.udea.onomastico.model.UnidadAcademica;
import co.edu.udea.onomastico.service.UnidadAcademicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

@RestController
public class UnidadAcademicaController {

    @Autowired
    UnidadAcademicaService unidadAcademicaService;


    @GetMapping("/unidadesacademicas")
    @JsonView(Views.Public.class)
    public List<UnidadAcademica> getAllUnidadesAcademicas() {
        return unidadAcademicaService.getAllUnidadesAcademicas();
    }


    @PostMapping("/unidadacademica")
    @JsonView(Views.Public.class)
    public UnidadAcademica addUnidadAcademica(@RequestBody UnidadAcademica unidadAcademica) {
        return unidadAcademicaService.addUnidadAcademica(unidadAcademica);
    }

    @DeleteMapping("/unidadacademica/{id}")
    public ResponseEntity<?> deleteProgramaAcademico(@PathVariable(value = "id") Integer unidadAcademicaId) {
        return unidadAcademicaService.deleteUnidadAcademica(unidadAcademicaId);
    }


}
