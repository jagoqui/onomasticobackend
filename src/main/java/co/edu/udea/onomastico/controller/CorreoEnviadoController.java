package co.edu.udea.onomastico.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import co.edu.udea.onomastico.model.CorreoEnviado;
import co.edu.udea.onomastico.model.Views;
import co.edu.udea.onomastico.payload.CorreoEnviadoResponse;
import co.edu.udea.onomastico.service.CorreoEnviadoService;

@RestController
public class CorreoEnviadoController {

	@Autowired
	CorreoEnviadoService correoEnviadoService;
	
	@GetMapping("/emails")
	public List<CorreoEnviadoResponse> getAllEmails() {
		return correoEnviadoService.getAllEmails();
	}
	
	@GetMapping("/emails/pag")
	public List<CorreoEnviadoResponse> getAllEmailsPag(@RequestParam Integer npage, 
			@RequestParam Integer psize,@RequestParam(required = false) String sort){
        return correoEnviadoService.getAllEmailsPag(npage, psize, sort);
    }
	@GetMapping("/emails/total")
	public Integer getTotalCorreosEnviados(){
        return correoEnviadoService.getTotalCorreosEnviados();
    }
	
	@JsonView(Views.Public.class)
	@PostMapping("/emails")
	public CorreoEnviado addCorreoEnviado(@RequestBody CorreoEnviado correoEnviado) {
		return correoEnviadoService.addCorreoEnviado(correoEnviado);
	}
	
	@GetMapping("/emails/export")
    public void exportCSV(HttpServletResponse response) throws Exception {

        String filename = "emails.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<CorreoEnviadoResponse> writer = new StatefulBeanToCsvBuilder<CorreoEnviadoResponse>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        //write all emails to csv file
        writer.write(correoEnviadoService.getAllEmails());
                
    }

}
