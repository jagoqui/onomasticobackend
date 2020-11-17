package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Usuario;
import co.edu.udea.onomastico.repository.UsuarioRepository;

@RestController
public class UsuarioController {

	@Autowired
	UsuarioRepository  usuarioRepository;
	
	//obtener todos los usuarios
	@GetMapping("/usuarios")
	public List<Usuario> getAllUsuarios() {
	    return usuarioRepository.findAll();
	}
	
	//crear usuario
	@PostMapping("/usuarios")
	public Usuario createUsuario(@RequestBody Usuario usuario) {
	    return usuarioRepository.save(usuario);
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
		usuario.setPassword(detallesUsuario.getPassword());

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
