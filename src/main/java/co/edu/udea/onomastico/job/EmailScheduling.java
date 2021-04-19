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
	
    //cron everyday at 6:00 am
    @Scheduled(cron = "0 0 6 * * ?")
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
		text.append("</td></tr></table></td></tr></table>"
				+ "<table class=\"es-content-body\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"transparent\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\"><tr style=\"border-collapse:collapse;\"><td align=\"left\" style=\"padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\">\r\n"
				+ " <td width=\"560\" align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;\">\r\n"
				+ " <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\"><td class=\"es-m-txt-c\" align=\"center\" style=\"padding:0;Margin:0;\"><table class=\"es-table-not-adapt es-social\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\"><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:40px;\">\r\n"
				+ " <a href=\"https://www.facebook.com/universidaddeantioquia/\"></a><img title=\"Facebook\" src=\"https://www.instagram.com/static/images/ico/apple-touch-icon-76x76-precomposed.png/666282be8229.png\" alt=\"Fb\" width=\"32\" height=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;border-radius:50%\"></a></td>\r\n"
				+ " <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:40px;\">\r\n"
				+ " <a href=\"https://twitter.com/UdeA\"></a><img title=\"Twitter\" src=\"https://www.instagram.com/static/images/ico/apple-touch-icon-76x76-precomposed.png/666282be8229.png\" alt=\"Tw\" width=\"32\" height=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;border-radius:50%\"></a></td><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:40px;\">\r\n"
				+ " <a href=\"https://www.youtube.com/channel/UCszBPa0AyP2aL-81wdvs_gg\"><img title=\"Youtube\" src=\"https://www.instagram.com/static/images/ico/apple-touch-icon-76x76-precomposed.png/666282be8229.png\" alt=\"Yt\" width=\"32\" height=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;border-radius:50%\"></a></td><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;\">\r\n"
				+ " <a href=\"https://www.instagram.com/UdeA/\"><img title=\"Instagram\" src=\"https://www.instagram.com/static/images/ico/apple-touch-icon-76x76-precomposed.png/666282be8229.png\" alt=\"Tt\" width=\"32\" height=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;border-radius:50%\"></a></td></tr></table></td></tr></table>"
				+ "<table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\"><tr style=\"border-collapse:collapse;\"><td align=\"center\" style=\"padding:0;Margin:0;\"><table bgcolor=\"transparent\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\"cellspacing=\"0\" width=\"600\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td align=\"left\"style=\"padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;\">\r\n"
				+ "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td width=\"560\" align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n"
				+ "style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\"><td align=\"center\" style=\"padding:0;Margin:0;\">\r\n"
				+"<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:'open sans', 'helvetica neue', helvetica, arial, sans-serif;line-height:28px;color:#698391;\">\r\n"
				+ "Facultad de Ingeniería Universidad de Antioquia | Vigilada Mineducación | Acreditación institucional hasta el 2022 NIT 890980040-8 | <a href=\"ingenieria.udea.edu.co\"></a>ingenieria.udea.edu.co | Líneas de Atención al Ciudadano | 01 8000 416384 [+57-4] 2198332 | Dirección: calle 67 No. 53 - 108 | <a href=\"atencionciudadano@udea.edu.co\"></a>atencionciudadano@udea.edu.co</p>"
				+ "<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:'open sans', 'helvetica neue', helvetica, arial, sans-serif;line-height:28px;color:#698391;\">\r\n"
				+ "Este mensaje ha sido enviado a través de su suscripción en Onomástico.\r\n"
				+ "Para anular su suscripción, haga clic <a target=\"_blank\" href='"+UNSUBSCRIBE_URL+encriptedEmail+"' ");
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
