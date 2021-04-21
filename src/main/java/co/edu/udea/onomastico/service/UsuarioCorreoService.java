package co.edu.udea.onomastico.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.exceptions.ResourceAlreadyExistsException;
import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.model.UsuarioCorreoId;
import co.edu.udea.onomastico.model.Vinculacion;
import co.edu.udea.onomastico.repository.UsuarioCorreoRepository;

@Service
public class UsuarioCorreoService {

	@Autowired
	UsuarioCorreoRepository usuarioCorreoRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
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
	
	
	public List<UsuarioCorreo> getAllUsuarioCorreoByUsuario(Integer usuarioId){
		Set<Asociacion> asociaciones = usuarioService.getAsociacionUsuarioById(usuarioId);
		List<UsuarioCorreo> usuarioCorreo =  getUsuariosCorreosByAsociacion(asociaciones);
		return usuarioCorreo;
	}
	
	public List<UsuarioCorreo> getAllUsuarioCorreoByUsuarioPag(Integer usuarioId, Integer pageNo, Integer pageSize, String sortBy) throws ResourceNotFoundException{
		Set<Asociacion> asociaciones = usuarioService.getAsociacionUsuarioById(usuarioId);
		List<UsuarioCorreo> usuarioCorreos = getUsuariosCorreosByAsociacion(asociaciones);
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		final int start = (int)paging.getOffset();
		final int end = Math.min((start + paging.getPageSize()), usuarioCorreos.size());
		final Page<UsuarioCorreo> page = new PageImpl<>(usuarioCorreos.subList(start, end), paging, usuarioCorreos.size());
		return page.toList();
	}
	
	public List<UsuarioCorreo> getUsuariosCorreosByAsociacion(Set<Asociacion> asociaciones){
		List<UsuarioCorreo> merge = new ArrayList<UsuarioCorreo>();
		asociaciones.forEach(asociacion ->{
			List<UsuarioCorreo> temp = usuarioCorreoRepository.findByAsociacionPorUsuarioCorreo(asociacion);
			if(temp!= null && !temp.isEmpty())merge.addAll(temp);
		});
		return merge;
	}
	
	public List<UsuarioCorreo> getAllUsuariosCorreo(Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<UsuarioCorreo> pagedResult =  usuarioCorreoRepository.findAll(paging);
        if(pagedResult.hasContent()) return pagedResult.getContent();
        else return new ArrayList<UsuarioCorreo>();
    }
	
	public List<UsuarioCorreo> getAllUsuarios() {
	    return usuarioCorreoRepository.findAll();
	}
	
	public UsuarioCorreo createUsuario(UsuarioCorreo usuario) {
			if(!usuarioCorreoRepository.findByEmail(usuario.getEmail()).isEmpty() && !usuarioCorreoRepository.findById(usuario.getId()).isEmpty()) throw new ResourceAlreadyExistsException(" ya se encuentra en uso", usuario.getEmail());
			UsuarioCorreo newUser = usuarioCorreoRepository.save(usuario);
			return newUser;
	}
	
	//unsuscribe with encripted email
	
	public UsuarioCorreo unsubscribe(String encriptedEmail) {
			String email = new String(Base64.getDecoder().decode(encriptedEmail));
			UsuarioCorreo user = usuarioCorreoRepository.findByEmail(email)
					.orElseThrow(() -> new ResourceNotFoundException("UsuarioCorreo"+"email"+email));
			user.setEstado("INACTIVO");
			usuarioCorreoRepository.save(user);
			return user;
		}
	//suscribe with email not encripted
	public UsuarioCorreo subscribe(String nonencriptedEmail) {
		UsuarioCorreo user = usuarioCorreoRepository.findByEmail(nonencriptedEmail)
				.orElseThrow(() -> new ResourceNotFoundException("UsuarioCorreo"+"email"+nonencriptedEmail));
		user.setEstado("ACTIVO");
		usuarioCorreoRepository.save(user);
		return user;
	}
	
	public Optional<UsuarioCorreo> getUsuarioById(String tipo, String numero) {
		UsuarioCorreoId id = new UsuarioCorreoId(tipo,numero);
	    return usuarioCorreoRepository.findById(id);
	}
	
	public  UsuarioCorreo updateUsuario(String tipo, String numero, UsuarioCorreo detallesUsuario) {
		UsuarioCorreoId usuarioId = new UsuarioCorreoId(tipo,numero);
		UsuarioCorreo  usuario =  usuarioCorreoRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("UsuarioCorreo" + "id"+ usuarioId));

		usuario.setId(detallesUsuario.getId());
		usuario.setNombre(detallesUsuario.getNombre());;
		usuario.setEmail(detallesUsuario.getEmail());
		usuario.setApellido(detallesUsuario.getApellido());
		usuario.setGenero(detallesUsuario.getGenero());
		usuario.setEstado(detallesUsuario.getEstado());
		usuario.setFechaNacimiento(detallesUsuario.getFechaNacimiento());
		usuario.setPlataformaPorUsuarioCorreo(detallesUsuario.getPlataformaPorUsuarioCorreo());
		usuario.setAsociacionPorUsuarioCorreo(detallesUsuario.getAsociacionPorUsuarioCorreo());
		usuario.setProgramaAcademicoPorUsuarioCorreo(detallesUsuario.getProgramaAcademicoPorUsuarioCorreo());
		usuario.setVinculacionPorUsuarioCorreo(detallesUsuario.getVinculacionPorUsuarioCorreo());
		UsuarioCorreo updatedUsuario = usuarioCorreoRepository.save(usuario);
	    return updatedUsuario;
	}
	
	public ResponseEntity<?> deleteUsuario(String tipo, String numero) {
		UsuarioCorreoId usuarioId = new UsuarioCorreoId(tipo,numero);
		UsuarioCorreo usuario = usuarioCorreoRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("UsuarioCorreo"+"id"+usuarioId));
	    
		usuarioCorreoRepository.delete(usuario);
	    return ResponseEntity.ok().build();
	}
}
