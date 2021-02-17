package co.edu.udea.onomastico.job;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

@Service
public class EmailScheduling {
	private static final Logger logger = LoggerFactory.getLogger(EmailScheduling.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
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
    		 List<UsuarioCorreo> destinatarios = getRecipers(evento);
    		 destinatarios.forEach(destino ->{
    			 emails.append(getTextoByReciper(evento.getPlantilla(), destino)); 
    			 //emailService.sendEmail(destino.getEmail(),evento.getNombre(), getTextoByReciper(evento.getPlantilla(), destino));
    		 });
    	});
		return emails.toString();
    }

	private String getTextoByReciper(Plantilla plantilla, UsuarioCorreo destino) {
		String text = "<div id=\"editorContent\" style=\"background-image: url('http://arquimedes.udea.edu.co:8096/onomastico/images/"
				+ String.valueOf(plantilla.getId()) + "/background.jpg'); background-repeat: no-repeat; background-position: center center; background-size: cover; height: auto; min-height: 300px; color: black;\">";
		text += plantilla.getTexto();
		text.replace("<nombre>", destino.getNombre());
		text.replace("<fecha>", LocalDateTime.now().toString());
		return text;
	}

	private List<UsuarioCorreo> getRecipers(Evento evento) {
		if(evento.getRecurrencia().contentEquals("DIARIA")) {
			return emailSchedulingRespository.selectUsuariosCorreo(evento.getCondicionesEvento());
        }else if(evento.getFecha().getMonth() == LocalDateTime.now().getMonthValue() && evento.getFecha().getDay() == LocalDateTime.now().getDayOfMonth() ) {
        	return emailSchedulingRespository.selectUsuariosCorreo(evento.getCondicionesEvento());
        	}
		return null;
	}
    
}
