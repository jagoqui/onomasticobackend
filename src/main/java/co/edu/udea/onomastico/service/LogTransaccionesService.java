package co.edu.udea.onomastico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.LogTransacciones;
import co.edu.udea.onomastico.repository.LogTransaccionesRepository;
import co.edu.udea.onomastico.repository.UsuarioRepository;

@Service
public class LogTransaccionesService {

	@Autowired
	LogTransaccionesRepository transaccionesRepository;
	
	@Autowired
	UsuarioRepository  usuarioRepository;
	
	public LogTransacciones createTransaccion(Integer usuarioId, LogTransacciones transaccion) {
		return usuarioRepository.findById(usuarioId).map(usuario -> {
			transaccion.setUsuario(usuario);
			return transaccionesRepository.save(transaccion);
			}).orElseThrow(() -> new ResourceNotFoundException("UsuarioId " + usuarioId + " not found"));
		}
	
	public Page<LogTransacciones> getAllTransaccionesByUsuarioId(Integer usuarioId, Pageable pageable) {
		return transaccionesRepository.findByUsuarioId(usuarioId, pageable);
		}

}
