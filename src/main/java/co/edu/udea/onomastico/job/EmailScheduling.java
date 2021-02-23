package co.edu.udea.onomastico.job;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.model.Condicion;
import co.edu.udea.onomastico.model.Evento;
import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.repository.EmailSchedulingRepository;
import co.edu.udea.onomastico.service.EmailService;
import co.edu.udea.onomastico.service.EventoService;
import co.edu.udea.onomastico.util.DateUtil;

@Service
public class EmailScheduling {
	private static final Logger logger = LoggerFactory.getLogger(EmailScheduling.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final LocalDate date = LocalDate.now();
    
    @Autowired
    EventoService eventoService;
    
    @Autowired
    EmailSchedulingRepository  emailSchedulingRespository;
    
    @Autowired
    EmailService emailService;
    
    //@Scheduled(cron = "0 8 * * * ?")
    public String scheduleDailyEmails() {
    	StringBuilder emails = new StringBuilder();
    	List<Evento> eventos = eventoService.findAllEventos();
    	eventos.forEach(evento ->{
    		System.out.print(evento.getNombre());
    		 emailService.sendEmail("jcarolina.escobar@udea.edu.co","prueba", getTextoByReciper(evento.getPlantilla(), "Jenny", LocalDateTime.now()));
    		 List<UsuarioCorreo> destinatarios = getRecipers(evento);
    		 if(destinatarios != null) {
    		 destinatarios.forEach(destino ->{
    			 emails.append(getTextoByReciper(evento.getPlantilla(), destino)); 
    			 //emailService.sendEmail(destino.getEmail(),evento.getNombre(), getTextoByReciper(evento.getPlantilla(), destino));
    		 });
    		 }
    	});
		return emails.toString();
    }

	private String getTextoByReciper(Plantilla plantilla, String string, LocalDateTime now) {
		String text = "<div id=\"editorContent\" style=\"background-image: url('http://arquimedes.udea.edu.co:8096/onomastico/images/"
				+ String.valueOf(plantilla.getId()) + "/background.jpg'); background-repeat: no-repeat; background-position: center center; background-size: cover; height: auto; min-height: 300px; color: black;\">";
		text += plantilla.getTexto();
		text.replace("&lt;Nombre&gt;", string);
		text.replace("&lt;Fecha&gt;", LocalDateTime.now().toString());
		return text;
	}

	private String getTextoByReciper(Plantilla plantilla, UsuarioCorreo destino) {
		StringBuilder text = new StringBuilder("<div id=\"editorContent\" style=\"background-image: url('http://arquimedes.udea.edu.co:8096/onomastico/images/");
		text.append(String.valueOf(plantilla.getId()) + "/background.jpg'); background-repeat: no-repeat; background-position: center center; background-size: cover; height: auto; min-height: 100%; color: black;\">");
		text.append(plantilla.getTexto());
		String target = "&lt;Nombre&gt;";
		if(text.toString().contains(target)) {
			String replacement = destino.getNombre().toLowerCase();
			int startIndex = text.indexOf(target);
			int stopIndex = startIndex + target.length();
			text.replace(startIndex, stopIndex, replacement);
		}
		text.append("</div><p> <a href='https://www.w3schools.com'>unsuscribe</a> </p>");
		return text.toString();
	}

	private List<UsuarioCorreo> getRecipers(Evento evento) {
		String recurrencia = evento.getRecurrencia();
		Date fechaEvento = evento.getFecha();
		if(recurrencia.contentEquals("DIARIA")) {
			return emailSchedulingRespository.selectUsuariosCorreo(evento.getCondicionesEvento());
        }else if(recurrencia.contentEquals("ANUAL") && DateUtil.getDayFromDate(fechaEvento)== date.getDayOfMonth() && DateUtil.getMonthFromDate(fechaEvento)==date.getMonthValue()) {
        		return emailSchedulingRespository.selectUsuariosCorreo(evento.getCondicionesEvento());
        	}
		return null;
	}
    
}
