package co.edu.udea.onomastico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udea.onomastico.model.LogTransacciones;
import co.edu.udea.onomastico.service.LogTransaccionesService;


@RestController
public class LogTransaccionesController {

	@Autowired
	LogTransaccionesService transaccionesService;
	
	@GetMapping("/usuarios/{usuarioId}/transacciones")
    public Page<LogTransacciones> getAllTransaccionesByUsuarioId(@PathVariable (value = "usuarioId") Integer usuarioId,
                                                Pageable pageable) {
        return transaccionesService.getAllTransaccionesByUsuarioId(usuarioId, pageable);
    }

}
