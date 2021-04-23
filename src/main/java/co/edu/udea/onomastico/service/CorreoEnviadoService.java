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
import co.edu.udea.onomastico.payload.CorreoEnviadoResponse;
import co.edu.udea.onomastico.repository.CorreoEnviadoRepository;

@Service
public class CorreoEnviadoService {
	@Autowired
	CorreoEnviadoRepository correoEnviadoRepository;
	
	public List<CorreoEnviadoResponse> getAllEmails() {
		return getAllEmailsFormatted(correoEnviadoRepository.findAll());
	}
	
	public List<CorreoEnviadoResponse> getAllEmailsPag(Integer pageNo,  Integer pageSize,String sortBy){
		Pageable paging;
		if(sortBy!=null)paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        else paging = PageRequest.of(pageNo, pageSize);
        Page<CorreoEnviado> pagedResult =  correoEnviadoRepository.findAll(paging);
        if(pagedResult.hasContent()) return getAllEmailsFormatted(pagedResult.getContent());
        else return new ArrayList<CorreoEnviadoResponse>();
    }
	
	public CorreoEnviado addCorreoEnviado(CorreoEnviado correoEnviado) {
		return correoEnviadoRepository.save(correoEnviado);
	}
	
	public List<CorreoEnviadoResponse> getAllEmailsFormatted(List<CorreoEnviado> emails) {
		List<CorreoEnviadoResponse> emailFormatted = new ArrayList<CorreoEnviadoResponse>();
		emails.forEach(email ->{
			emailFormatted.add(new CorreoEnviadoResponse(email.getId().getEmail(),email.getId().getFecha(),email.getAsunto()));
		});
		return emailFormatted;
	}
	

}
