package co.edu.udea.onomastico.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.exceptions.BadRequestException;
import co.edu.udea.onomastico.exceptions.ResourceAlreadyExistsException;
import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.UnidadAdministrativa;
import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.model.UsuarioCorreoId;
import co.edu.udea.onomastico.model.Vinculacion;
import co.edu.udea.onomastico.repository.UsuarioCorreoRepository;

import javax.validation.constraints.NotNull;

@Service
public class UsuarioCorreoService {

	@Autowired
	UsuarioCorreoRepository usuarioCorreoRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	VinculacionService vinculacionService;
	
	@Autowired
	UnidadAdministrativaService unidadAdministrativaService;
	
	@Autowired
	ProgramaAcademicoService programaAcademicoService;
	
	public List<UsuarioCorreo> findByBirhday(){
		return usuarioCorreoRepository.findByBirthday();
	}
	
	public List<UsuarioCorreo> findByGender(String gender){
		return usuarioCorreoRepository.findByGenero(gender);
	}
	
	public List<UsuarioCorreo> findByAsociation(UnidadAdministrativa unidadAdministrativa){
		return usuarioCorreoRepository.findByUnidadAdministrativaPorCorreoUsuario(unidadAdministrativa);
	}
	
	public List<UsuarioCorreo> findByProgram(ProgramaAcademico programaAcademico){
		return usuarioCorreoRepository.findByProgramaAcademicoPorUsuarioCorreo(programaAcademico);
	}
	
	public List<UsuarioCorreo> findByVinculation(Vinculacion vinculacion){
		return usuarioCorreoRepository.findByVinculacionPorUsuarioCorreo(vinculacion);
	}
	public Integer getTotalUsuariosCorreoPorUsuarioPlataforma(@NotNull Integer usuarioId) {
		return getAllUsuarioCorreoByUsuario(usuarioId).size();
	}
	
	public List<UsuarioCorreo> getAllUsuarioCorreoByUsuario(@NotNull Integer usuarioId){
		Set<UnidadAdministrativa> asociaciones = usuarioService.getUnidadAdministrativaUsuarioById(usuarioId);
		List<UsuarioCorreo> usuarioCorreo =  getUsuariosCorreosByAsociacion(asociaciones);
		return usuarioCorreo;
	}

	public UsuarioCorreo addAsociacionToUsuarioCorreo(List<UnidadAdministrativa> asociacionesToAdd, UsuarioCorreoId idUsuario){
		UsuarioCorreo usuario = usuarioCorreoRepository.findById(idUsuario).get();
		asociacionesToAdd.stream().forEach(asociacion -> usuario.addAsociacion(asociacion));
		return usuarioCorreoRepository.save(usuario);
	}
	
	public List<UsuarioCorreo> getAllUsuarioCorreoByUsuarioPag(Integer usuarioId, Integer pageNo, Integer pageSize, String sortBy) throws ResourceNotFoundException{
		Set<UnidadAdministrativa> asociaciones = usuarioService.getUnidadAdministrativaUsuarioById(usuarioId);
		List<UsuarioCorreo> usuarioCorreos = getUsuariosCorreosByAsociacion(asociaciones);
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		final int start = (int)paging.getOffset();
		final int end = Math.min((start + paging.getPageSize()), usuarioCorreos.size());
		final Page<UsuarioCorreo> page = new PageImpl<>(usuarioCorreos.subList(start, end), paging, usuarioCorreos.size());
		return page.toList();
	}
	
	public List<UsuarioCorreo> getUsuariosCorreosByAsociacion(Set<UnidadAdministrativa> asociaciones){
		List<UsuarioCorreo> merge = new ArrayList<UsuarioCorreo>();
		asociaciones.forEach(asociacion ->{
			List<UsuarioCorreo> temp = usuarioCorreoRepository.findByUnidadAdministrativaPorCorreoUsuario(asociacion);
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
	
	public UsuarioCorreo createUsuario(UsuarioCorreo usuario) throws BadRequestException{
		if(!isUserDetailsValid(usuario)) throw new BadRequestException("argumentos invalidos");
		if(!usuarioCorreoRepository.findByEmail(usuario.getEmail()).isEmpty() || !usuarioCorreoRepository.findById(usuario.getId()).isEmpty()) throw new ResourceAlreadyExistsException(" ya se encuentra en uso", usuario.getEmail());

		/*usuario.getProgramaAcademicoPorUsuarioCorreo().stream().forEach(programa -> {
			usuario.addProgramaAcademico(programa);
			Asociacion asociacion = asociacionService.getAsociacionByProgramaAcademico(programa);
			asociacion.addProgramaAcademico(programa);
		});

		 */
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
	public boolean isUserDetailsValid(UsuarioCorreo usuario) {
		if(!(usuario.getEstado().equals("ACTIVO") || usuario.getEstado().equals("INACTIVO")))return false;
		if(!(usuario.getGenero().equals("FEMENINO")|| usuario.getGenero().equals("MASCULINO")))return false;
		return true;
	}
	
	public  UsuarioCorreo updateUsuario(String tipo, String numero, UsuarioCorreo detallesUsuario) throws BadRequestException{
		if(!isUserDetailsValid(detallesUsuario)) throw new BadRequestException("argumentos invalidos");
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
		usuario.setUnidadAdministrativaPorCorreoUsuario(detallesUsuario.getUnidadAdministrativaPorCorreoUsuario());
		usuario.setProgramaAcademicoPorUsuarioCorreo(detallesUsuario.getProgramaAcademicoPorUsuarioCorreo());
		usuario.setVinculacionPorUsuarioCorreo(detallesUsuario.getVinculacionPorUsuarioCorreo());
		UsuarioCorreo updatedUsuario = usuarioCorreoRepository.save(usuario);
	    return updatedUsuario;
	}
	
	public ResponseEntity<?> deleteUsuario(String tipo, String numero) {
		UsuarioCorreoId usuarioId = UsuarioCorreoId.builder().tipoIdentificacion(tipo).numeroIdentificacion(numero).build();
		UsuarioCorreo usuario = usuarioCorreoRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("UsuarioCorreo"+"id"+usuarioId));
	    
		usuarioCorreoRepository.deleteById(usuarioId);
	    return ResponseEntity.ok().build();
	}
}
