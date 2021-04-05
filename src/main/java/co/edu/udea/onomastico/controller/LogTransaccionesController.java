package co.edu.udea.onomastico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udea.onomastico.model.LogTransacciones;
import co.edu.udea.onomastico.security.JwtTokenProvider;
import co.edu.udea.onomastico.service.LogTransaccionesService;


@RestController
public class LogTransaccionesController {
	
	@Autowired
	FeignClientInterceptor interceptor;
	
	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	LogTransaccionesService transaccionesService;
	
	@GetMapping("/usuarios/transacciones")
    public Page<LogTransacciones> getAllTransaccionesByUsuarioId(Pageable pageable) {
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
        return transaccionesService.getAllTransaccionesByUsuarioId(usuarioId, pageable);
    }

}
