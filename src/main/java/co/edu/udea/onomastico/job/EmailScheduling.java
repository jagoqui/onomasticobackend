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
	
    //cron everyday at 10:30 am
    @Scheduled(cron = "0 30 12 * * ?")
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
    			 correoEnviadoService.addCorreoEnviado(new CorreoEnviado(new CorreoEnviadoId(destino.getEmail()),evento.getNombre()));
    		 });
    		 }
    		}
    	});
		//return emails.toString();
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
		text.append("</td></tr></table></td></tr></table><table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\"><tr style=\"border-collapse:collapse;\"><td align=\"center\" style=\"padding:0;Margin:0;\"><table bgcolor=\"transparent\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\"cellspacing=\"0\" width=\"600\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td align=\"left\"style=\"padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;\">\r\n"
				+ "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td width=\"560\" align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n"
				+ "style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\"><td align=\"center\" style=\"padding:0;Margin:0;\">\r\n"
				+ "<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:'open sans', 'helvetica neue', helvetica, arial, sans-serif;line-height:28px;color:#698391;\">\r\n"
				+ "Si ya no desea recibir estas actualizaciones, puede darse de baja <a target=\"_blank\" href='"+UNSUBSCRIBE_URL+encriptedEmail+"' ");
		return text.toString();
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
    
}
