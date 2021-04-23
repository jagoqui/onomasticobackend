package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.LogTransacciones;
import co.edu.udea.onomastico.model.Views;
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
	
	@JsonView(Views.Public.class)
	@GetMapping("/usuarios/transacciones")
    public List<LogTransacciones> getAllTransaccionesByUsuarioId(@RequestParam Integer npage, 
			@RequestParam Integer psize,@RequestParam(required = false) String sort){
		Integer usuarioId = tokenProvider.getUserIdFromJWT(interceptor.getBearerTokenHeader());
        return transaccionesService.getAllTransaccionesByUsuarioId(usuarioId, npage, psize, sort);
    }

}
