package co.edu.udea.onomastico.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udea.onomastico.exceptions.BadRequestException;
import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Usuario;
import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.model.UsuarioCorreoId;
import co.edu.udea.onomastico.repository.UsuarioCorreoRepository;


@CrossOrigin(origins = "*")
@RestController
public class UsuarioCorreoController {

	@Autowired
	UsuarioCorreoRepository  usuarioRepository;
	
	@GetMapping("/usuariosemail")
	public List<UsuarioCorreo> getAllUsuarios() {
	    return usuarioRepository.findAll();
	}
	
	//crear usuario
	@PostMapping("/usuariosemail")
	public UsuarioCorreo createUsuario(@RequestBody UsuarioCorreo usuario) {
		if(usuario.getEmail()!=null && usuarioRepository.findById(usuario.getId()).isEmpty()) {
			UsuarioCorreo newUser = usuarioRepository.save(usuario);
			return newUser;
		}else throw new BadRequestException("usuario de correo existente");
	}
	
	@GetMapping("/usuariosemail/{tipo}/{numero}")
	public Optional<UsuarioCorreo> getUsuarioById(@PathVariable(value = "tipo") String tipo, 
			@PathVariable(value = "numero")String numero) {
		UsuarioCorreoId id = new UsuarioCorreoId(tipo,numero);
	    return usuarioRepository.findById(id);
	}
	
	@PutMapping("/usuariosemail/{tipo}/{numero}")
	public  UsuarioCorreo updateUsuario(@PathVariable(value = "tipo") String tipo, 
			@PathVariable(value = "numero")String numero,
			@RequestBody UsuarioCorreo detallesUsuario) {
		UsuarioCorreoId usuarioId = new UsuarioCorreoId(tipo,numero);
		UsuarioCorreo  usuario =  usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("UsuarioCorreo" + "id"+ usuarioId));

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
		UsuarioCorreo updatedUsuario = usuarioRepository.save(usuario);
	    return updatedUsuario;
	}
	
	@DeleteMapping("/usuariosemail/{tipo}/{numero}")
	public ResponseEntity<?> deleteUsuario(@PathVariable(value = "tipo") String tipo, 
			@PathVariable(value = "numero")String numero) {
		UsuarioCorreoId usuarioId = new UsuarioCorreoId(tipo,numero);
		UsuarioCorreo usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("UsuarioCorreo"+"id"+usuarioId));
	    
		usuarioRepository.delete(usuario);
	    return ResponseEntity.ok().build();
	}
}
