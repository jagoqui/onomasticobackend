package co.edu.udea.onomastico.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.model.Vinculacion;
import co.edu.udea.onomastico.repository.UsuarioCorreoRepository;

@Service
public class UsuarioCorreoService {

	@Autowired
	UsuarioCorreoRepository usuarioCorreoRepository;
	
	public List<UsuarioCorreo> findByBirhday(){
		return usuarioCorreoRepository.findByBirthday();
	}
	
	public List<UsuarioCorreo> findByGender(String gender){
		return usuarioCorreoRepository.findByGenero(gender);
	}
	
	public List<UsuarioCorreo> findByAsociation(Asociacion asociacion){
		return usuarioCorreoRepository.findByAsociacionPorUsuarioCorreo(asociacion);
	}
	
	public List<UsuarioCorreo> findByProgram(ProgramaAcademico programaAcademico){
		return usuarioCorreoRepository.findByProgramaAcademicoPorUsuarioCorreo(programaAcademico);
	}
	
	public List<UsuarioCorreo> findByVinculation(Vinculacion vinculacion){
		return usuarioCorreoRepository.findByVinculacionPorUsuarioCorreo(vinculacion);
	}
}
