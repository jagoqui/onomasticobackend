package co.edu.udea.onomastico.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.udea.onomastico.exceptions.BadRequestException;
import co.edu.udea.onomastico.exceptions.ResourceAlreadyExistsException;
import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.Usuario;
import co.edu.udea.onomastico.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	UsuarioRepository  usuarioRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
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
		Set<Asociacion> asociaciones = getAsociacionUsuarioById(usuarioId);
		List<Usuario> usuarios =  getUsuariosByAsociacion(asociaciones);
		return usuarios;
	}
	
	public List<Usuario> getAllUsuariosByUsuarioPag(Integer usuarioId, Integer pageNo, Integer pageSize, String sortBy) throws ResourceNotFoundException{
		if(isAdmin(usuarioId)) {
		Set<Asociacion> asociaciones = getAsociacionUsuarioById(usuarioId);
		List<Usuario> usuarios = getUsuariosByAsociacion(asociaciones);
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		final int start = (int)paging.getOffset();
		final int end = Math.min((start + paging.getPageSize()), usuarios.size());
		final Page<Usuario> page = new PageImpl<>(usuarios.subList(start, end), paging, usuarios.size());
		return page.toList();
		} throw new BadRequestException("To complite this action the user must be an Admin");
	}
	
	public List<Usuario> getUsuariosByAsociacion(Set<Asociacion> asociaciones){
		List<Usuario> merge = new ArrayList<Usuario>();
		asociaciones.forEach(asociacion ->{
			List<Usuario> temp = usuarioRepository.findByAsociacionPorUsuario(asociacion);
			if(temp!= null && !temp.isEmpty())merge.addAll(temp);
		});
		return merge;
	}
	

	public Usuario AddUsuario(@RequestBody Usuario usuario) {
		if(!usuarioRepository.findByEmail(usuario.getEmail()).isEmpty()) throw new ResourceAlreadyExistsException("usuario existente",usuario.getEmail());
		String password = usuario.getPassword();
		String encriptedPassword = passwordEncoder.encode(password);
		usuario.setPassword(encriptedPassword);
	    Usuario newuser = usuarioRepository.save(usuario);
	    return newuser;
	}

	public Usuario getUsuarioById(Integer usuarioId) {
	    return usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));
	}
	
	public Set<Asociacion> getAsociacionUsuarioById(Integer usuarioId) {
	    Usuario usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));
		return usuario.getAsociacionPorUsuario();
	}
	
	public boolean isAdmin(Integer usuarioId) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));
		if(usuario.getRol().getNombre().contains("ADMIN")) return true;
		return false;
	}
	
	public  Usuario updateUsuario(Integer usuarioId, Usuario detallesUsuario) {

		 Usuario  usuario =  usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario" + "id"+usuarioId));

		usuario.setNombre(detallesUsuario.getNombre());;
		usuario.setEmail(detallesUsuario.getEmail());
		String encriptedPassword = passwordEncoder.encode(detallesUsuario.getPassword());
		usuario.setPassword(encriptedPassword);

		Usuario updatedUsuario = usuarioRepository.save(usuario);
	    return updatedUsuario;
	}
	
	public ResponseEntity<?> deleteUsuario(Integer usuarioId) {
	    Usuario usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario"+"id"+usuarioId));

	    usuarioRepository.delete(usuario);
	    return ResponseEntity.ok().build();
	}
}
