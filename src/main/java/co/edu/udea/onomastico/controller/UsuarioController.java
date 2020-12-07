package co.edu.udea.onomastico.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
	@GetMapping("/usuarios")
	public List<Usuario> getAllUsuarios() {
	    return usuarioRepository.findAll();
	}
	
	//crear usuario
	@PostMapping("/usuarios")
	public Usuario AddUsuario(@RequestBody Usuario usuario) {
		if(!usuarioRepository.findByEmail(usuario.getEmail()).isEmpty()) throw new BadRequestException("usuario existente");
		String password = usuario.getPassword();
		String encriptedPassword = passwordEncoder.encode(password);
		usuario.setPassword(encriptedPassword);
	    Usuario newuser = usuarioRepository.save(usuario);
	    return newuser;
	}
	@GetMapping("/usuarios/{id}")
	public Usuario getUsuarioById(@PathVariable(value = "id") Integer usuarioId) {
	    return usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));
	}
	
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
