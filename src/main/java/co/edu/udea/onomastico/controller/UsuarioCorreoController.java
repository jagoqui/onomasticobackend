package co.edu.udea.onomastico.controller;

import java.util.List;
import java.util.Optional;

import co.edu.udea.onomastico.model.UnidadAdministrativa;
import co.edu.udea.onomastico.model.UsuarioCorreoId;
import co.edu.udea.onomastico.payload.UsuarioCorreoResquest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.security.JwtTokenProvider;
import co.edu.udea.onomastico.service.UsuarioCorreoService;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@CrossOrigin(origins = "*")
@RestController
public class UsuarioCorreoController {
	
	@Autowired
	FeignClientInterceptor interceptor;
	
	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	UsuarioCorreoService  usuarioService;
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuariosemail/pag")
	public List<UsuarioCorreo> getAllPlantillasPorAsociacionPag(@RequestParam Integer npage,@RequestParam Integer psize,@RequestParam String sort){
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return usuarioService.getAllUsuarioCorreoByUsuarioPag(usuarioId, npage, psize, sort);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuariosemail/total")
	public Integer getTotalUsuariosCorreoPorUsuarioPlataforma(){
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
		return usuarioService.getTotalUsuariosCorreoPorUsuarioPlataforma(usuarioId);
	}

	@JsonView(Views.Public.class)
	@GetMapping("/usuariosemail")
	public List<UsuarioCorreo> getAllUsuarios() {
	    return usuarioService.getAllUsuarios();
	}
	
	//crear usuario
	@JsonView(Views.Public.class)
	@PostMapping("/usuariosemail")
	public UsuarioCorreo createUsuario(@Valid @RequestBody UsuarioCorreoResquest usuario) {
		return usuarioService.createUsuario(UsuarioCorreoResquest.toModel(usuario));
	}

	// Crear Usuario
	@JsonView(Views.Public.class)
	@PostMapping("/usuariosemail/{tipo}/{numero}")
	public UsuarioCorreo AddAsociacionToUsuarioCorreo(@RequestBody List<UnidadAdministrativa> unidadAdministrativaRequest,
													  @PathVariable String tipo,
													  @PathVariable String numero){
		UsuarioCorreoId idUsuario = UsuarioCorreoId.builder().tipoIdentificacion(tipo).numeroIdentificacion(numero).build();
		return usuarioService.addAsociacionToUsuarioCorreo(unidadAdministrativaRequest, idUsuario);
	}
	
	//ususcribe with ecripted email
	@JsonView(Views.Public.class)
	@PutMapping("/unsubscribe/{email}")
	public UsuarioCorreo unsuscribe(@Valid @NotBlank @PathVariable(value = "email") String encriptedEmail) {
		return usuarioService.unsubscribe(encriptedEmail);
	}
	
	//ususcribe with ecripted email
	@JsonView(Views.Public.class)
	@PutMapping("/usuariosemail/subscribe/{email}")
	public UsuarioCorreo suscribe(@Valid @Email @PathVariable(value = "email") String nonencriptedEmail) {
		return usuarioService.subscribe(nonencriptedEmail);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuariosemail/{tipo}/{numero}")
	public Optional<UsuarioCorreo> getUsuarioById(@NotBlank @PathVariable(value = "tipo") String tipo,
												  @NotBlank @PathVariable(value = "numero")String numero) {
		Optional<UsuarioCorreo> usuario = usuarioService.getUsuarioById(tipo, numero);
		if(!usuario.isPresent()) throw new ResourceNotFoundException();
		else return usuario;
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/usuariosemail/{tipo}/{numero}")
	public  UsuarioCorreo updateUsuario(@NotBlank @PathVariable(value = "tipo") String tipo,
			@NotBlank @PathVariable(value = "numero")String numero, @Valid @RequestBody UsuarioCorreoResquest detallesUsuario) {
		return usuarioService.updateUsuario(tipo, numero, UsuarioCorreoResquest.toModel(detallesUsuario));
	}
	
	@DeleteMapping("/usuariosemail/{tipo}/{numero}")
	public ResponseEntity<?> deleteUsuario(@NotBlank @PathVariable(value = "tipo") String tipo,
			@NotBlank @PathVariable(value = "numero")String numero) {
		return usuarioService.deleteUsuario(tipo, numero);
	}



}
