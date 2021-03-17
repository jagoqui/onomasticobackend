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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.exceptions.BadRequestException;
import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Usuario;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.repository.UsuarioRepository;

@RestController
public class UsuarioController {

	@Autowired
	UsuarioRepository  usuarioRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//obtener todos los usuarios
	@JsonView(Views.Public.class)
	@GetMapping("/usuarios")
	public List<Usuario> getAllUsuarios() {
	    return usuarioRepository.findAll();
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuarios/pag/{pageNo}/{pageSize}/{sortBy}")
	public List<Usuario> getAllUsuariosCorreo(@PathVariable(value = "pageNo") Integer pageNo, 
			@PathVariable(value = "pageSize") Integer pageSize,@PathVariable(value = "sortBy") String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Usuario> pagedResult =  usuarioRepository.findAll(paging);
        if(pagedResult.hasContent()) return pagedResult.getContent();
        else return new ArrayList<Usuario>();
    }
	
	//crear usuario
	@JsonView(Views.Public.class)
	@PostMapping("/usuarios")
	public Usuario AddUsuario(@RequestBody Usuario usuario) {
		if(!usuarioRepository.findByEmail(usuario.getEmail()).isEmpty()) throw new BadRequestException("usuario existente");
		String password = usuario.getPassword();
		String encriptedPassword = passwordEncoder.encode(password);
		usuario.setPassword(encriptedPassword);
	    Usuario newuser = usuarioRepository.save(usuario);
	    return newuser;
	}
	@JsonView(Views.Public.class)
	@GetMapping("/usuarios/{id}")
	public Usuario getUsuarioById(@PathVariable(value = "id") Integer usuarioId) {
	    return usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuarios/asociacion/{id}")
	public Set<Asociacion> getAsociacionUsuarioById(@PathVariable(value = "id") Integer usuarioId) {
	    Usuario usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));
		return usuario.getAsociacionPorUsuario();
	}
	
	@JsonView(Views.Public.class)
	@PutMapping("/usuarios/{id}")
	public  Usuario updateUsuario(@PathVariable(value = "id") Integer usuarioId,
	                                         @RequestBody Usuario detallesUsuario) {

		 Usuario  usuario =  usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario" + "id"+usuarioId));

		usuario.setNombre(detallesUsuario.getNombre());;
		usuario.setEmail(detallesUsuario.getEmail());
		String encriptedPassword = passwordEncoder.encode(detallesUsuario.getPassword());
		usuario.setPassword(encriptedPassword);

		Usuario updatedUsuario = usuarioRepository.save(usuario);
	    return updatedUsuario;
	}
	
	@DeleteMapping("/usuarios/{id}")
	public ResponseEntity<?> deleteUsuario(@PathVariable(value = "id") Integer usuarioId) {
	    Usuario usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));

	    usuarioRepository.delete(usuario);
	    return ResponseEntity.ok().build();
	}

}
