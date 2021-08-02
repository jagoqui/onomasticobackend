package co.edu.udea.onomastico.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import co.edu.udea.onomastico.model.UnidadAcademica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.udea.onomastico.exceptions.AuthException;
import co.edu.udea.onomastico.exceptions.BadRequestException;
import co.edu.udea.onomastico.exceptions.ResourceAlreadyExistsException;
import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.UnidadAdministrativa;
import co.edu.udea.onomastico.model.Rol;
import co.edu.udea.onomastico.model.Usuario;
import co.edu.udea.onomastico.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	UsuarioRepository  usuarioRepository;

	@Autowired
	UnidadAcademicaService unidadAcademicaService;

	@Autowired
	UnidadAdministrativaService unidadAdministrativaService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	ProgramaAcademicoService  programaAcademicoService;
	
	@Autowired
	private EmailService emailService;
	
	@Value("${app.resetpwd}")
	private String RESET_SERVER;
	
	public List<Usuario> getAllUsuarios() {
	    return usuarioRepository.findAll();
	}
	
	public List<Usuario> getAllUsuariosPag(Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Usuario> pagedResult =  usuarioRepository.findAll(paging);
        if(pagedResult.hasContent()) return pagedResult.getContent();
        else return new ArrayList<Usuario>();
    }
	
	public List<Usuario> getAllUsuariosByUsuario(Integer usuarioId){
		Set<UnidadAdministrativa> unidadesAdministrativas = getUnidadAdministrativaUsuarioById(usuarioId);
		List<Usuario> usuarios =  getUsuariosByUnidadAdministrativa(unidadesAdministrativas);
		return usuarios;
	}
	
	public List<Usuario> getAllUsuariosByUsuarioPag(Integer usuarioId, Integer pageNo, Integer pageSize, String sortBy) throws ResourceNotFoundException{
		if(isAdmin(usuarioId)) {
		Set<UnidadAdministrativa> asociaciones = getUnidadAdministrativaUsuarioById(usuarioId);
		List<Usuario> usuarios = getUsuariosByUnidadAdministrativa(asociaciones);
		Pageable paging;
		if(sortBy!=null)paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        else paging = PageRequest.of(pageNo, pageSize);
		final int start = (int)paging.getOffset();
		final int end = Math.min((start + paging.getPageSize()), usuarios.size());
		final Page<Usuario> page = new PageImpl<>(usuarios.subList(start, end), paging, usuarios.size());
		return page.toList();
		} throw new AuthException("To complite this action the user must be an Admin");
	}
	
	public Optional findUserByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	public Optional findUserByResetToken(String resetToken) {
		return usuarioRepository.findByResetToken(resetToken);
	}

	public void save(Usuario user) {
		usuarioRepository.save(user);
	}
	
	public List<Usuario> getUsuariosByUnidadAdministrativa(Set<UnidadAdministrativa> unidadesAdministrativas){
		List<Usuario> merge = new ArrayList<Usuario>();
		unidadesAdministrativas.forEach(unidadAdministrativa ->{
			List<Usuario> temp = usuarioRepository.findByUnidadAdministrativaPorUsuario(unidadAdministrativa);
			if(temp!= null && !temp.isEmpty())merge.addAll(temp);
		});
		return merge;
	}
	

	public Usuario AddUsuario(@RequestBody Usuario usuario) {
		if(!(usuario.getEstado().equals("ACTIVO") || usuario.getEstado().equals("INACTIVO"))) throw new BadRequestException("Estado Incorrecto");
		if(!usuarioRepository.findByEmail(usuario.getEmail()).isEmpty()) throw new ResourceAlreadyExistsException("usuario existente",usuario.getEmail());
		String password = UUID.randomUUID().toString();
		String encriptedPassword = passwordEncoder.encode(password);
		usuario.setPassword(encriptedPassword);
		usuario.setResetToken(UUID.randomUUID().toString());
	    Usuario newuser = usuarioRepository.save(usuario);
	    sendEmailToNewUser(usuario);
	    return newuser;
	}
	public void sendEmailToNewUser(Usuario user) {
		String asunto = "Bienvenido a onomastico";
		String message = "Onomastico es el sistema para enviar felicitaciones a todos los integrantes de " +
				"la universidad de Antioquía. Para ingresar por primera vez, diríjase a:\n" + RESET_SERVER
				+ "/" + user.getResetToken();
		
		emailService.sendEmail(user.getEmail(),asunto, message);
	}

	public List<Usuario> getUsuariosByRol(Rol rol){
		return usuarioRepository.findByRol(rol);
	}
	public Usuario getUsuarioById(Integer usuarioId) {
	    return usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));
	}
	
	public Set<UnidadAdministrativa> getUnidadAdministrativaUsuarioById(Integer usuarioId) {
	    Usuario usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));
		return usuario.getUnidadAdministrativaPorUsuario();
	}
	
	/*public List<ProgramaConAsociacionResponse> getProgramasPorAsociacionResponseUsuarioById(Integer usuarioId) {
		List<ProgramaConAsociacionResponse> programasPorAsociacion = new ArrayList<ProgramaConAsociacionResponse>();
		Set<Asociacion> asociaciones = getAsociacionUsuarioById(usuarioId);
		if(asociaciones != null) {
			asociaciones.forEach(asociacion ->{
				List<ProgramaAcademico> programas = programaAcademicoService.findByProgramaAcademicoPorAsociacion(asociacion);
				if(programas != null) {
					programas.forEach(programa ->{
						programasPorAsociacion.add(new ProgramaConAsociacionResponse(asociacion.getId(), programa.getCodigo(), programa.getNombre()));
					});
				}
			});
		}
		return programasPorAsociacion;
	}

	 */
	
	public boolean isAdmin(Integer usuarioId) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));
		if(usuario.getRol().getNombre().contains("ADMIN")) return true;
		return false;
	}
	
	public  Usuario updateUsuario(Integer usuarioId, Usuario detallesUsuario) {

		 Usuario  usuario =  usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario" + "id"+usuarioId));
		if(detallesUsuario.getNombre()!=null)usuario.setNombre(detallesUsuario.getNombre());
		if(detallesUsuario.getEmail()!=null)usuario.setEmail(detallesUsuario.getEmail());
		if(detallesUsuario.getPassword()!=null) {
			String encriptedPassword = passwordEncoder.encode(detallesUsuario.getPassword());
			usuario.setPassword(encriptedPassword);
		}
		if(!(detallesUsuario.getEstado().equals("ACTIVO")|| detallesUsuario.getEstado().equals("INACTIVO")))throw new BadRequestException("Estado incorrecto");
		usuario.setEstado(detallesUsuario.getEstado());
		if(detallesUsuario.getRol()!=null)usuario.setRol(detallesUsuario.getRol());
		if(detallesUsuario.getUnidadAdministrativaPorUsuario()!=null)usuario.setUnidadAdministrativaPorUsuario(detallesUsuario.getUnidadAdministrativaPorUsuario());
		Usuario updatedUsuario = usuarioRepository.save(usuario);
	    return updatedUsuario;
	}
	
	public ResponseEntity<?> deleteUsuario(Integer usuarioId) {
	    Usuario usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));

	    usuarioRepository.delete(usuario);
	    return ResponseEntity.ok().build();
	}
	
	public Usuario desactivar(String email) {
		Usuario user = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("email"+email));
		user.setEstado("INACTIVO");
		usuarioRepository.save(user);
		return user;
	}
	
	public Usuario activar(String email) {
		Usuario user = usuarioRepository.findByEmail(email)
			.orElseThrow(() -> new ResourceNotFoundException("UsuarioCorreo"+"email"+email));
		user.setEstado("ACTIVO");
		usuarioRepository.save(user);
		return user;
	}

	public List<Object> getUnidadesPorUsuario(Integer usuarioId){
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));
		Set<UnidadAdministrativa> unidadesAdministrativas = usuario.getUnidadAdministrativaPorUsuario();
		List<UnidadAcademica> unidadesAcademicas = getUnidadAcademicaPorUsuario(usuarioId);

		List<Object> unidadesMerge = new ArrayList<>();
		unidadesMerge.addAll(unidadesAdministrativas);
		unidadesMerge.addAll(unidadesAcademicas);

		return unidadesMerge;

	}

	public List<UnidadAcademica> getUnidadAcademicaPorUsuario(Integer usuarioId){
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));
		List<UnidadAcademica> unidadAcademicaSet = unidadAcademicaService.getAllUnidadesAcademicas();
		List<UnidadAcademica> unidadesAcademicas = new ArrayList<>();
		if(unidadAcademicaSet != null){
			unidadAcademicaSet.forEach(unidadAcademica -> {
				if(unidadAdministrativaService.existsUnidadAdministrativa(unidadAcademica.getId())){
					unidadesAcademicas.add(unidadAcademica);
				}
			});
		}
		return unidadesAcademicas;
	}



}
