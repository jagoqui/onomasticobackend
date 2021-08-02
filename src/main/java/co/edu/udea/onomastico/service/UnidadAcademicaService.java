package co.edu.udea.onomastico.service;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.onomastico.model.*;
import co.edu.udea.onomastico.repository.UnidadAcademicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;

@Service
public class UnidadAcademicaService {

    @Autowired
    UnidadAcademicaRepository unidadAcademicaRepository;

    @Autowired
    UnidadAdministrativaService unidadAdministrativaService;

    @Autowired
    ProgramaAcademicoService programaAcademicoService;

    public List<UnidadAcademica> getAllUnidadesAcademicas() {
        return unidadAcademicaRepository.findAll();
    }

    public UnidadAcademica addUnidadAcademica(UnidadAcademica unidadAcademica) {
        unidadAcademicaRepository.save(unidadAcademica);
        return unidadAcademica;
    }

    public ResponseEntity<?> deleteUnidadAcademica(Integer unidadAcademicaId) {
        UnidadAcademica unidadAcademica = unidadAcademicaRepository.findById(unidadAcademicaId)
                .orElseThrow(() -> new ResourceNotFoundException("facultad"+"id"+unidadAcademicaId));
        unidadAcademicaRepository.delete(unidadAcademica);
        return ResponseEntity.ok().build();
    }

    public List<UnidadAcademica> getUnidadesAcademicasByPrograms(List<ProgramaAcademico> programaAcademicos){
        List<UnidadAcademica> unidadAcademicas = new ArrayList<UnidadAcademica>();
        programaAcademicos.forEach(programaAcademico -> {
            if(!unidadAcademicas.contains(programaAcademico.getUnidadAcademica())){
                unidadAcademicas.add(programaAcademico.getUnidadAcademica());
            }
        });
        return unidadAcademicas;
    }

    public void addUnidadesAcademicasToAsociaciones(List<UnidadAcademica> unidadesAcademicasToAdd){
        unidadesAcademicasToAdd.stream().forEach(unidadAcademica -> {
            UnidadAdministrativa unidadAdministrativaToAdd = UnidadAdministrativa.builder().id(unidadAcademica.getId()).nombre(unidadAcademica.getNombre()).build();
            unidadAdministrativaService.addUnidadAdministrativa(unidadAdministrativaToAdd);
        });
        //asociacionesToAdd.stream().forEach(asociacion -> usuario.addAsociacion(asociacion));
        //return usuarioCorreoRepository.save(usuario);
    }

    public boolean existsUnidadAcademica(Integer id) {
        return unidadAcademicaRepository.existsById(id);
    }


}
