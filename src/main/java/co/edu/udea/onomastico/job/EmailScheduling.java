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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.model.Condicion;
import co.edu.udea.onomastico.model.CorreoEnviado;
import co.edu.udea.onomastico.model.Evento;
import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.model.UsuarioCorreo;
import co.edu.udea.onomastico.repository.EmailSchedulingRepository;
import co.edu.udea.onomastico.service.CorreoEnviadoService;
import co.edu.udea.onomastico.service.EmailService;
import co.edu.udea.onomastico.service.EventoService;
import co.edu.udea.onomastico.util.DateUtil;
import co.edu.udea.onomastico.util.StringUtil;
import java.util.Base64;

@Service
public class EmailScheduling {
	@Value("${app.images}")
	private String IMAGE_SERVER;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final LocalDate date = LocalDate.now();
    
    @Autowired
    EventoService eventoService;
    
    @Autowired
    EmailSchedulingRepository  emailSchedulingRespository;
    
    @Autowired
    EmailService emailService;
    
    @Autowired
    CorreoEnviadoService correoEnviadoService;
    
    //cron everyday at 8:00 am
    //@Scheduled(cron = "0 0 8 * * ?")
    public String scheduleDailyEmails() {
    	StringBuilder emails = new StringBuilder();
    	List<Evento> eventos = eventoService.getAllEventos();
    	eventos.forEach(evento ->{
    		System.out.print(evento.getNombre());
    		 //emailService.sendEmail("apoyodesarrolloingenieria6@udea.edu.co","prueba", getTextoByReciper(evento.getPlantilla(), "Jenny", LocalDateTime.now()));
    		 List<UsuarioCorreo> destinatarios = getRecipers(evento);
    		 if(destinatarios != null) {
    		 destinatarios.forEach(destino ->{
    			 emails.append(getTextoByReciper(evento.getPlantilla(), destino)); 
    			 //emailService.sendEmail(destino.getEmail(),evento.getNombre(), getTextoByReciper(evento.getPlantilla(), destino));
    			 correoEnviadoService.addCorreoEnviado(new CorreoEnviado());
    		 });
    		 }
    	});
		return emails.toString();
    }

	private String getTextoByReciper(Plantilla plantilla, String string, LocalDateTime now) {
		String textoPlantilla = plantilla.getTexto();
		StringBuilder text = new StringBuilder(textoPlantilla);
		String[] targets = {"<font color=\"#e74c3c\">&lt;Nombre&gt;</font>","<font color=\"#16a085\">&lt;Fecha&gt;</font>"};
		String[] replacements = {string, date.toString()};
		for(int i=0; i<targets.length;i++) {
			StringBuilder tempText = StringUtil.replaceText(targets[i],text,replacements[i]);
			text = tempText;
		}
		text.append("</div><div><p> Si quieres dejar de recibir nuestras tarjetas, <a href='http://arquimedes.udea.edu.co/onomastico/mail-users-subscription-status/");
		String email = "jcarolina.escobar@udea.edu.co";
		String encriptedEmail = Base64.getEncoder().encodeToString(email.getBytes());
		text.append(encriptedEmail);
		text.append("'>pulsa aquí</a>  para darte de baja.</p></div></div>");
		return text.toString();
	}

	private String getTextoByReciper(Plantilla plantilla, UsuarioCorreo destino) {
		String textoPlantilla = plantilla.getTexto();
		StringBuilder text = new StringBuilder(textoPlantilla);
		String[] targets = {"&lt;NOMBRE&gt;","&lt;FECHA&gt;","&lt;FALCUTAD/ESCUELA&gt;","&lt;ESTAMENTO&gt;"};
		String nombre = StringUtil.capitalizeFirstLetter(StringUtil.getFirstWord(destino.getNombre()));
		System.out.print(destino.getAsociacionPorUsuarioCorreo());
		String[] replacements = {nombre, date.toString(), destino.getAsociacionPorUsuarioCorreo().toString(), destino.getVinculacionPorUsuarioCorreo().toString()};
		for(int i=0; i<targets.length;i++) {
			StringBuilder tempText = StringUtil.replaceText(targets[i],text,replacements[i]);
			text = tempText;
		}
		text.append("</div><div><p> Si quieres dejar de recibir nuestras tarjetas, <a href='http://arquimedes.udea.edu.co/onomastico/mail-users-subscription-status/");
		String encriptedEmail = Base64.getEncoder().encodeToString(destino.getEmail().getBytes());
		text.append(encriptedEmail);
		text.append("'>pulsa aquí</a>  para darte de baja.</p></div></div>");
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
