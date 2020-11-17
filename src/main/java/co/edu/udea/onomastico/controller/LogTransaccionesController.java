package co.edu.udea.onomastico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udea.onomastico.exceptions.ResourceNotFoundException;
import co.edu.udea.onomastico.model.LogTransacciones;
import co.edu.udea.onomastico.repository.LogTransaccionesRepository;
import co.edu.udea.onomastico.repository.UsuarioRepository;


@RestController
public class LogTransaccionesController {

	@Autowired
	LogTransaccionesRepository  transaccionesRepository;
	
	@Autowired
	UsuarioRepository  usuarioRepository;
	
	@GetMapping("/usuarios/{usuarioId}/transacciones")
    public Page<LogTransacciones> getAllTransaccionesByUsuarioId(@PathVariable (value = "usuarioId") Integer usuarioId,
                                                Pageable pageable) {
        return transaccionesRepository.findByUsuarioId(usuarioId, pageable);
    }

    @PostMapping("/usuarios/{usuarioId}/transacciones")
    public LogTransacciones createTransaccion(@PathVariable (value = "usuariosId") Integer usuarioId,
                                 @RequestBody LogTransacciones transaccion) {
        return usuarioRepository.findById(usuarioId).map(usuario -> {
        	transaccion.setUsuario(usuario);
            return transaccionesRepository.save(transaccion);
        }).orElseThrow(() -> new ResourceNotFoundException("UsuarioId " + usuarioId + " not found"));
    }

    @PutMapping("/usuarios/{usuarioId}/transacciones")
    public LogTransacciones updateTransaccion(@PathVariable (value = "usuarioId") Integer usuarioId,
                                 @PathVariable (value = "transaccionId") Integer transaccionId,
                                 @RequestBody LogTransacciones transaccionRequest) {
        if(!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("UsuariotId " + usuarioId + " not found");
        }

        return transaccionesRepository.findById(transaccionId).map(transaccion -> {
            transaccion.setTransaccion(transaccionRequest.getTransaccion());
            return transaccionesRepository.save(transaccion);
        }).orElseThrow(() -> new ResourceNotFoundException("transaccionId " + transaccionId + "not found"));
    }

    @DeleteMapping("/usuarios/{usuarioId}/transacciones/{transaccionId}")
    public ResponseEntity<?> deleteTransaccion(@PathVariable (value = "usuarioId") Integer usuarioId,
                              @PathVariable (value = "transaccionId") Integer transaccionId) {
        return transaccionesRepository.findByIdAndUsuarioId(transaccionId, usuarioId).map(transaccion -> {
            transaccionesRepository.delete(transaccion);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Transaccion no encontrada con el id " + transaccionId + " and usuarioId " + usuarioId));
    }
	
}
