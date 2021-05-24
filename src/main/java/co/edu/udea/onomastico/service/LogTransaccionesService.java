package co.edu.udea.onomastico.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	
	public LogTransacciones createTransaccion(Integer usuarioId,String transaccion) {
		return usuarioRepository.findById(usuarioId).map(usuario -> {
			LogTransacciones LogTransaccion = new LogTransacciones();
			LogTransaccion.setUsuario(usuario);
			LogTransaccion.setTransaccion(transaccion);
			return transaccionesRepository.save(LogTransaccion);
			}).orElseThrow(() -> new ResourceNotFoundException("UsuarioId " + usuarioId + " not found"));
		}
	
	public List<LogTransacciones> getAllTransaccionesByUsuarioId(Integer usuarioId, Integer pageNo, Integer pageSize, String sortBy){
		Pageable paging;
		if(sortBy!=null)paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        else paging = PageRequest.of(pageNo, pageSize);
        Page<LogTransacciones> pagedResult =  transaccionesRepository.findByUsuarioId(usuarioId, paging);
        if(!pagedResult.isEmpty()) return pagedResult.getContent();
        else return new ArrayList<LogTransacciones>();
	}
}
