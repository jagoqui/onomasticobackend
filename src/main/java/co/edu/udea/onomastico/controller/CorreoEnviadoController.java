package co.edu.udea.onomastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.CorreoEnviado;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.service.CorreoEnviadoService;

@RestController
public class CorreoEnviadoController {

	@Autowired
	CorreoEnviadoService correoEnviadoService;
	
	@JsonView(Views.Public.class)
	@GetMapping("/emails")
	public List<CorreoEnviado> getAllEmails() {
		return correoEnviadoService.getAllEmails();
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/emails/pag")
	public List<CorreoEnviado> getAllUsuariosCorreo(@RequestParam Integer npage, 
			@RequestParam Integer psize,@RequestParam String sort){
        return correoEnviadoService.getAllUsuariosCorreo(npage, psize, sort);
    }
	
	@JsonView(Views.Public.class)
	@PostMapping("/emails")
	public CorreoEnviado addCorreoEnviado(@RequestBody CorreoEnviado correoEnviado) {
		return correoEnviadoService.addCorreoEnviado(correoEnviado);
	}

}
