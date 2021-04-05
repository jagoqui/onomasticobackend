package co.edu.udea.onomastico.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.payload.EventoResponse;
import co.edu.udea.onomastico.security.JwtTokenProvider;
import co.edu.udea.onomastico.service.UsuarioCorreoService;



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
	@GetMapping("/usuariosemail/pag/{pageNo}/{pageSize}/{sortBy}")
	public List<UsuarioCorreo> getAllUsuariosCorreo(@PathVariable(value = "pageNo") Integer pageNo, 
			@PathVariable(value = "pageSize") Integer pageSize,@PathVariable(value = "sortBy") String sortBy){
        return usuarioService.getAllUsuariosCorreo(pageNo, pageSize, sortBy);
    }
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuariosemail")
	public List<UsuarioCorreo> getAllUsuarios() {
	    return usuarioService.getAllUsuarios();
	}
	
	//crear usuario
	@PostMapping("/usuariosemail")
	public UsuarioCorreo createUsuario(@RequestBody UsuarioCorreo usuario) {
		return usuarioService.createUsuario(usuario);
	}
	
	//ususcribe with ecripted email
	@JsonView(Views.Public.class)
	@PutMapping("/unsuscribe/{email}")
	public UsuarioCorreo unsuscribe(@PathVariable(value = "email") String encriptedEmail) {
		return usuarioService.unsuscribe(encriptedEmail);
	}
	
	//ususcribe with ecripted email
	@JsonView(Views.Public.class)
	@PutMapping("/usuariosemail/suscribe/{email}")
	public UsuarioCorreo suscribe(@PathVariable(value = "email") String nonencriptedEmail) {
		return usuarioService.suscribe(nonencriptedEmail);
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuariosemail/{tipo}/{numero}")
	public Optional<UsuarioCorreo> getUsuarioById(@PathVariable(value = "tipo") String tipo, 
			@PathVariable(value = "numero")String numero) {
		return usuarioService.getUsuarioById(tipo, numero);
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/usuariosemail/{tipo}/{numero}")
	public  UsuarioCorreo updateUsuario(@PathVariable(value = "tipo") String tipo, 
			@PathVariable(value = "numero")String numero, @RequestBody UsuarioCorreo detallesUsuario) {
		return usuarioService.updateUsuario(tipo, numero, detallesUsuario);
	}
	
	@DeleteMapping("/usuariosemail/{tipo}/{numero}")
	public ResponseEntity<?> deleteUsuario(@PathVariable(value = "tipo") String tipo, 
			@PathVariable(value = "numero")String numero) {
		return usuarioService.deleteUsuario(tipo, numero);
	}
}
