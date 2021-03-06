package co.edu.udea.onomastico.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Rol;
import co.edu.udea.onomastico.model.Usuario;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.payload.ProgramaConAsociacionResponse;
import co.edu.udea.onomastico.security.JwtTokenProvider;
import co.edu.udea.onomastico.service.UsuarioService;

@RestController
public class UsuarioController {

	@Autowired
	UsuarioService  usuarioService;
	
	@Autowired
	FeignClientInterceptor interceptor;
	
	@Autowired
	JwtTokenProvider tokenProvider;
	
	//obtener todos los usuarios
	@JsonView(Views.Public.class)
	@GetMapping("/usuarios")
	public List<Usuario> getAllUsuarios() {
	    return usuarioService.getAllUsuarios();
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuarios/pag")
	public List<Usuario> getAllUsuariosPorAsociacionPag(@RequestParam Integer npage,@RequestParam Integer psize,@RequestParam String sort){
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return usuarioService.getAllUsuariosByUsuarioPag(usuarioId, npage, psize, sort);
	}
	
//	@JsonView(Views.Public.class)
//	@GetMapping("/usuarios/pag/{pageNo}/{pageSize}/{sortBy}")
//	public List<Usuario> getAllUsuarios(@PathVariable(value = "pageNo") Integer pageNo, 
//			@PathVariable(value = "pageSize") Integer pageSize,@PathVariable(value = "sortBy") String sortBy){
//         return usuarioService.getAllUsuariosPag(pageNo, pageSize, sortBy);
//    }
//	
	//crear usuario
	@JsonView(Views.Public.class)
	@PostMapping("/usuarios")
	public Usuario AddUsuario(@RequestBody Usuario usuario) {
	    return usuarioService.AddUsuario(usuario);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuarios/{id}")
	public Usuario getUsuarioById(@PathVariable(value = "id") Integer usuarioId) {
	    return usuarioService.getUsuarioById(usuarioId);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuarios/asociacion")
	public Set<Asociacion> getAsociacionUsuarioById() {
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return usuarioService.getAsociacionUsuarioById(usuarioId);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuarios/rol")
	public List<Usuario> getUsuariosByIRol(Rol rol) {
		return usuarioService.getUsuariosByRol(rol);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuarios/programasporasociacion")
	public List<ProgramaConAsociacionResponse> getProgramasPorAsociacionUsuarioById() {
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return usuarioService.getProgramasPorAsociacionResponseUsuarioById(usuarioId);
	}
	@JsonView(Views.Public.class)
	@GetMapping("/usuarios/update")
	public Usuario updateOwnInfo(@RequestBody Usuario detallesUsuario) {
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return usuarioService.updateUsuario(usuarioId, detallesUsuario);
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/usuarios/{id}")
	public  Usuario updateUsuario(@PathVariable(value = "id") Integer usuarioId,
	                                         @RequestBody Usuario detallesUsuario) {
	    return usuarioService.updateUsuario(usuarioId, detallesUsuario);
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/usuarios/activar/{email}")
	public  Usuario activarUsuario(@PathVariable(value = "email") String email) {
	    return usuarioService.activar(email);
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/usuarios/desactivar/{email}")
	public  Usuario desactivarUsuario(@PathVariable(value = "email") String email) {
	    return usuarioService.desactivar(email);
	}
	
	@DeleteMapping("/usuarios/{id}")
	public ResponseEntity<?> deleteUsuario(@PathVariable(value = "id") Integer usuarioId) {
	    return usuarioService.deleteUsuario(usuarioId);
	}

}
