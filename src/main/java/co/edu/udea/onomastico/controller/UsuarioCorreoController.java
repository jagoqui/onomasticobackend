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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;


import co.edu.udea.onomastico.exceptions.ResourceAlreadyExitsException;
import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.model.UsuarioCorreoId;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.repository.UsuarioCorreoRepository;



@CrossOrigin(origins = "*")
@RestController
public class UsuarioCorreoController {

	@Autowired
	UsuarioCorreoRepository  usuarioRepository;
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuariosemail/pag/{pageNo}/{pageSize}/{sortBy}")
	public List<UsuarioCorreo> getAllUsuariosCorreo(@PathVariable(value = "pageNo") Integer pageNo, 
			@PathVariable(value = "pageSize") Integer pageSize,@PathVariable(value = "sortBy") String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<UsuarioCorreo> pagedResult =  usuarioRepository.findAll(paging);
        if(pagedResult.hasContent()) return pagedResult.getContent();
        else return new ArrayList<UsuarioCorreo>();
    }
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuariosemail")
	public List<UsuarioCorreo> getAllUsuarios() {
	    return usuarioRepository.findAll();
	}
	
	//crear usuario
	@PostMapping("/usuariosemail")
	public UsuarioCorreo createUsuario(@RequestBody UsuarioCorreo usuario) {
		if(usuario.getEmail()!=null && usuarioRepository.findById(usuario.getId()).isEmpty()) {
			if(usuarioRepository.findByEmail(usuario.getEmail())!=null) throw new ResourceAlreadyExitsException(usuario.getEmail()+" ya se encuentra en uso");
			UsuarioCorreo newUser = usuarioRepository.save(usuario);
			return newUser;
		}else throw new ResourceAlreadyExitsException("usuario de correo existente");
	}
	
	//ususcribe with ecriptedemail
	@JsonView(Views.Public.class)
	@PutMapping("/unsuscribe/{email}")
		public UsuarioCorreo unsuscribe(@PathVariable(value = "email") String encriptedEmail) {
			String email = new String(Base64.getDecoder().decode(encriptedEmail));
			UsuarioCorreo user = usuarioRepository.findByEmail(email)
					.orElseThrow(() -> new ResourceNotFoundException("UsuarioCorreo"+"email"+email));
			user.setEstado("INACTIVO");
			usuarioRepository.save(user);
			return user;
		}
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuariosemail/{tipo}/{numero}")
	public Optional<UsuarioCorreo> getUsuarioById(@PathVariable(value = "tipo") String tipo, 
			@PathVariable(value = "numero")String numero) {
		UsuarioCorreoId id = new UsuarioCorreoId(tipo,numero);
	    return usuarioRepository.findById(id);
	}
	
	@JsonView(Views.Public.class)
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
