package co.edu.udea.onomastico.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.CorreoEnviado;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.repository.CorreoEnviadoRepository;

@RestController
public class CorreoEnviadoController {

	@Autowired
	CorreoEnviadoRepository correoEnviadoRepository;
	
	@JsonView(Views.Public.class)
	@GetMapping("/emails")
	public List<CorreoEnviado> getAllEmails() {
		return correoEnviadoRepository.findAll();
	}
	
	@JsonView(Views.Public.class)
	@GetMapping("/emails/pag/{pageNo}/{pageSize}/{sortBy}")
	public List<CorreoEnviado> getAllUsuariosCorreo(@PathVariable(value = "pageNo") Integer pageNo, 
			@PathVariable(value = "pageSize") Integer pageSize,@PathVariable(value = "sortBy") String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<CorreoEnviado> pagedResult =  correoEnviadoRepository.findAll(paging);
        if(pagedResult.hasContent()) return pagedResult.getContent();
        else return new ArrayList<CorreoEnviado>();
    }
	
	@PostMapping("/emails")
	public CorreoEnviado addCorreoEnviado(@RequestBody CorreoEnviado correoEnviado) {
		return correoEnviadoRepository.save(correoEnviado);
	}
	
//	@GetMapping("/emails/{tipo}/{numero}/{fecha}/{id_evento}")
//	public CorreoEnviado getEmailById(@PathVariable(value = "id") Integer eventoId) {
//	    return correoEnviadoRepository.findById(eventoId)
//	            .orElseThrow(() -> new ResourceNotFoundException("Evento"+"id"+eventoId));
//	}
}
