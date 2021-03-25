package co.edu.udea.onomastico.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.model.CorreoEnviado;
import co.edu.udea.onomastico.repository.CorreoEnviadoRepository;

@Service
public class CorreoEnviadoService {
	@Autowired
	CorreoEnviadoRepository correoEnviadoRepository;
	
	public List<CorreoEnviado> getAllEmails() {
		return correoEnviadoRepository.findAll();
	}
	
	public List<CorreoEnviado> getAllUsuariosCorreo(Integer pageNo,  Integer pageSize,String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<CorreoEnviado> pagedResult =  correoEnviadoRepository.findAll(paging);
        if(pagedResult.hasContent()) return pagedResult.getContent();
        else return new ArrayList<CorreoEnviado>();
    }
	
	public CorreoEnviado addCorreoEnviado(CorreoEnviado correoEnviado) {
		return correoEnviadoRepository.save(correoEnviado);
	}

}
