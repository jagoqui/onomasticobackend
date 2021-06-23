package co.edu.udea.onomastico.job;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import co.edu.udea.onomastico.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import co.edu.udea.onomastico.model.Condicion;
import co.edu.udea.onomastico.model.CorreoEnviado;
import co.edu.udea.onomastico.model.CorreoEnviadoId;
import co.edu.udea.onomastico.model.Evento;
import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.payload.EmailQueryResponse;
import co.edu.udea.onomastico.repository.EmailSchedulingRepository;
import co.edu.udea.onomastico.service.AsociacionService;
import co.edu.udea.onomastico.service.CorreoEnviadoService;
import co.edu.udea.onomastico.service.EmailService;
import co.edu.udea.onomastico.service.EventoService;
import co.edu.udea.onomastico.service.ProgramaAcademicoService;
import co.edu.udea.onomastico.service.UsuarioCorreoService;
import co.edu.udea.onomastico.service.VinculacionService;
import co.edu.udea.onomastico.util.DateUtil;
import co.edu.udea.onomastico.util.StringUtil;
import java.util.Base64;

@Component
public class EmailScheduling {
	@Value("${app.images}")
	private String IMAGE_SERVER;
	@Value("${app.unsubscribe}")
	private String UNSUBSCRIBE_URL;
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
    
    @Autowired
	UsuarioCorreoService  usuarioService;
    
    @Autowired
	AsociacionService asociacionService;
	
	@Autowired
	VinculacionService vinculacionService;
	
	@Autowired
	ProgramaAcademicoService programaAcademicoService;
	
    //cron everyday at 7:00 am
    @Scheduled(cron = "0 0 14 * * ?")
    public void scheduleDailyEmails() {
    	StringBuilder emails = new StringBuilder();
    	List<Evento> eventos = eventoService.getAllEventos();
    	eventos.forEach(evento ->{
    		if(!evento.getEstado().contains("INACTIVO")) {
    		List<EmailQueryResponse> destinatarios = getRecipers(evento);
    		 if(destinatarios != null) {
    		 destinatarios.forEach(destino ->{
    			 emails.append(getTextoByReciper(evento.getPlantilla(), destino)); 
    			 emailService.sendEmail(destino.getEmail(),evento.getNombre(), getTextoByReciper(evento.getPlantilla(), destino));
				 correoEnviadoService.addCorreoEnviado(destino.getEmail(),evento.getNombre());
				 System.out.println("Email: " + destino.getEmail());
    		 });
    		 }
    		}
    	});
    }
	private String getTextoByReciper(Plantilla plantilla, EmailQueryResponse destino) {
		String textoPlantilla = plantilla.getTexto();
		String encriptedEmail = Base64.getEncoder().encodeToString(destino.getEmail().getBytes());
		String asociacion = getAsociacionName(destino.getAsociacionId());
		String vinculacion = getVinculacionName(destino.getVinculacionId());
		String programa = getProgramaAcademicoName(destino.getProgramaAcademicoId());
		StringBuilder text = new StringBuilder(textoPlantilla);
		String[] targets = {"&lt;NOMBRE&gt;","&lt;FECHA&gt;","&lt;FALCUTAD/ESCUELA&gt;","&lt;ESTAMENTO&gt;","&lt;PROGRAMA&gt;"};
		String nombre = StringUtil.capitalizeFirstLetter(StringUtil.getFirstWord(destino.getNombre()));
		String[] replacements = {nombre, date.toString(), asociacion, vinculacion, programa};
		for(int i=0; i<targets.length;i++) {
			StringBuilder tempText = StringUtil.replaceText(targets[i],text,replacements[i]);
			text = tempText;
		}
			String body = EmailUtil.emailContent(text.toString(), UNSUBSCRIBE_URL+encriptedEmail, destino.getEmail(), IMAGE_SERVER);
		//if(!asociacion.equalsIgnoreCase("Facultad De ingeniería")) {
		//	body = emailGenericContent(text.toString(), UNSUBSCRIBE_URL+encriptedEmail, destino.getEmail());
		//}
		return body;
	}
	
	private List<EmailQueryResponse> getRecipers(Evento evento) {
		String recurrencia = evento.getRecurrencia();
		Date fechaEvento = evento.getFecha();
		if(recurrencia.contentEquals("DIARIA")) {
			return emailSchedulingRespository.selectUsuariosCorreo(evento.getCondicionesEvento());
        }else if(recurrencia.contentEquals("ANUAL") && DateUtil.getDayFromDate(fechaEvento)== date.getDayOfMonth() && DateUtil.getMonthFromDate(fechaEvento)==date.getMonthValue()) {
        		return emailSchedulingRespository.selectUsuariosCorreo(evento.getCondicionesEvento());
        	}
		return null;
	}
	private String getAsociacionName(int id) {
		String name;
		try{
			name = asociacionService.getAsociacionById(id).getNombre();
		}catch(Exception e) {
			name = "";
		};
		return name;
	}
	private String getProgramaAcademicoName(int id) {
		String name;
		try{
			name = programaAcademicoService.getProgramaAcademicoById(id).getNombre();
		}catch(Exception e) {
			name = "";
		};
		return name;
	}
	private String getVinculacionName(int id) {
		String name;
		try{
			name = vinculacionService.getVinculacionById(id).getNombre();
		}catch(Exception e) {
			name = "";
		};
		return name;
	}
}
