package co.edu.udea.onomastico.controller;

import java.util.List;

import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.UnidadAcademica;
import co.edu.udea.onomastico.service.UnidadAcademicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    @GetMapping("/unidadacademica/programas/{id}")
    @JsonView(Views.Public.class)
    public List<ProgramaAcademico> findProgramasPorUnidadAcademica(@PathVariable(value = "id") Integer idUnidadAcademica) {
        return unidadAcademicaService.findProgramasByUnidadAcademica(idUnidadAcademica);
    }

    @GetMapping("/unidadacademica/{id}/programas")
    @JsonView(Views.Public.class)
    public List<ProgramaAcademico> findProgramasPorUnidadAcademicaPag(@PathVariable @Valid @NotNull Integer id,
                                                                      @PageableDefault(page = 0, size = 10,
                                                   direction = Sort.Direction.DESC, sort = "id")
                                                                      Pageable pageable) {
        return unidadAcademicaService.findProgramasByUnidadAcademicaPag(id, pageable);
    }



}
