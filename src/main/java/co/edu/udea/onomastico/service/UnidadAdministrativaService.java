package co.edu.udea.onomastico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.UnidadAdministrativa;
import co.edu.udea.onomastico.repository.UnidadAdministrativaRepository;

@Service
public class UnidadAdministrativaService {

	@Autowired
	UnidadAdministrativaRepository unidadAdministrativaRepository;

	@Autowired
	ProgramaAcademicoService programaAcademicoService;

	public List<UnidadAdministrativa> getAllUnidadesAdministrativas() {
	    return unidadAdministrativaRepository.findAll();
	}
	
	public UnidadAdministrativa addUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
		unidadAdministrativaRepository.save(unidadAdministrativa);
		return unidadAdministrativa;
		
	}

	public UnidadAdministrativa getUnidadAdministrativaById(Integer unidadAdministrativaId) {
	    return unidadAdministrativaRepository.findById(unidadAdministrativaId)
	            .orElseThrow(() -> new ResourceNotFoundException("UnidadAdministrativa"+"id"+unidadAdministrativaId));
	}
	
	public ResponseEntity<?> deleteUnidadAdministrativa(Integer unidadAdministrativaId) {
		UnidadAdministrativa unidadAdministrativa = unidadAdministrativaRepository.findById(unidadAdministrativaId)
	            .orElseThrow(() -> new ResourceNotFoundException("UnidadAdministrativa"+"id"+unidadAdministrativaId));
		unidadAdministrativaRepository.delete(unidadAdministrativa);
	    return ResponseEntity.ok().build();
	}
	
	/*public Asociacion getAsociacionByProgramaAcademico(ProgramaAcademico programaAcademico){
		return  asociacionRepository.findByprogramasAcademicos(programaAcademico);
	}

	 */
	
	public boolean existsUnidadAdministrativa(Integer id) {
		return unidadAdministrativaRepository.existsById(id);
	}

	public List<UnidadAdministrativa> getUnidadAdministrativaByNombre(String nombreUnidad) {
		return unidadAdministrativaRepository.findByNombre(nombreUnidad);
	}

	/*public Set<ProgramaAcademico> setAsociacionesInProgramasAcademicos(Set<ProgramaAcademico> programas){
		programas.forEach(programa ->{
			Asociacion asociacion = getAsociacionByProgramaAcademico(programa);
			//programa.setAsociacion(Asociacion.toModel(asociacion.getId(),asociacion.getNombre()));
			asociacion.addProgramaAcademico(programa);
			programaAcademicoService.addProgramaAcademico(programa);
		});
		return programas;
	}*/
}
