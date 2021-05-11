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
	
    //cron everyday at 7:00 am
    @Scheduled(cron = "0 0 7 * * ?")
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
		String body = emailGenericContent(text.toString(), UNSUBSCRIBE_URL+encriptedEmail);
		if(asociacion.contains("Ingeniería")||asociacion.contains("ingenieria")||asociacion.contains("Ingenieria")||asociacion.contains("ingeniería")) {
			body = emailContent(text.toString(), UNSUBSCRIBE_URL+encriptedEmail);
		}
		return body;
	}
	private String emailContent(String plantilla, String unsubscribeEmail) {
		String body = "<html style=\"width:100%;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0;\"><head><meta charset=\"UTF-8\"><meta content=\"width=device-width, initial-scale=1\" name=\"viewport\"><meta name=\"x-apple-disable-message-reformatting\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n"
				+ "<meta content=\"telephone=no\" name=\"format-detection\"><link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,400i,700,700i\" rel=\"stylesheet\">\r\n"
				+ "<style type=\"text/css\" >img{max-width: 600px !important;} @media only screen and (max-width:600px) {p, ul li, ol li, a { font-size:14px!important; line-height:150%!important } h1 { font-size:28px!important; text-align:center; line-height:120%!important } h2 { font-size:26px!important; text-align:center; line-height:120%!important } h3 { font-size:20px!important; text-align:center; line-height:120%!important } h1 a { font-size:28px!important } h2 a { font-size:26px!important } h3 a { font-size:20px!important } .es-menu td a { font-size:12px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:12px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:14px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:11px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { \r\n"
				+ "text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button { font-size:14px!important; display:block!important; border-left-width:0px!important; border-right-width:0px!important } .es-btn-fw { border-width:10px 0px!important; text-align:center!important } .es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r \r\n"
				+ "{padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } .es-desk-hidden { display:table-row!important; width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } .es-desk-menu-hidden { display:table-cell!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } }#outlook a {	padding:0;}.ExternalClass {	width:100%;}.ExternalClass,.ExternalClass p,.ExternalClass span,.ExternalClass font,.ExternalClass td,.ExternalClass div {	line-height:100%;}.es-button \r\n"
				+ "{mso-style-priority:100!important;	text-decoration:none!important;}a[x-apple-data-detectors] {	color:inherit!important;	text-decoration:none!important;	font-size:inherit!important;	font-family:inherit!important;	font-weight:inherit!important;	line-height:inherit!important;}.es-desk-hidden {	display:none;	float:left;	overflow:hidden;	width:0;	max-height:0;	line-height:0;	mso-hide:all;}</style>\r\n"
				+ "</head><body style=\"width:100%;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0;\"><div class=\"es-wrapper-color\" style=\"background-color:#F6F6F6;\">\r\n"
				+ "<table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td valign=\"top\" style=\"padding:0;Margin:0;\"><table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"> <td align=\"center\" bgcolor=\"#EFF3F5\" style=\"padding:0;Margin:0;background-color:#EFF3F5;\">\r\n"
				+ "<table bgcolor=\"#ffffff\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;\"> <tr style=\"border-collapse:collapse;\">\r\n"
				+ "<td align=\"center\" style=\"padding:4%;Margin:0\">\r\n"
				+ "    <img src="+IMAGE_SERVER.concat("logo-udea-ingenieria.png")+" width=\"312\">\r\n"
				+ "    </td></tr></table></td></tr></table>\r\n"
				+ "<table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\"style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td align=\"center\" bgcolor=\"#EFF3F5\" style=\"padding:0;Margin:0;background-color:#EFF3F5;\">\r\n"
				+ "<table bgcolor=\"#ffffff\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\"cellspacing=\"0\" width=\"600\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td align=\"center\" style=\"padding:0;Margin:0\">"
				+ plantilla + "</td></tr></table></td></tr></table>"
				+ "<table class=\"es-content-body\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"transparent\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\"><tr style=\"border-collapse:collapse;\"><td align=\"left\" style=\"padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\">\r\n"
				+ "<td width=\"560\" align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;\">\r\n"
				+ "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\"><td class=\"es-m-txt-c\" align=\"center\" style=\"padding:0;Margin:0;\"><table class=\"es-table-not-adapt es-social\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\"><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:40px;\">\r\n"
				+ "<a href=\" https://www.facebook.com/FacultadIngenieriaUdeA/\"><img title=\"Facebook\" src="+IMAGE_SERVER.concat("facebook.png")+" alt=\"Fb\" width=\"32\" height=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;border-radius:50%\"></a></td>\r\n"
				+ "<td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:40px;\">\r\n"
				+ " <a href=\"https://twitter.com/FIngenieriaUdeA\"><img title=\"Twitter\" src= "+IMAGE_SERVER.concat("twitter.png")+" alt=\"Tw\" width=\"32\" height=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;border-radius:50%\"></a></td><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:40px;\">\r\n"
				+ " <a href=\"https://www.youtube.com/channel/UCD12pFrjIyluFIKehoRmwXw\"><img title=\"Youtube\" src="+IMAGE_SERVER.concat("youtube.png")+" alt=\"Yt\" width=\"32\" height=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;border-radius:50%\"></a></td><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;\">\r\n"
				+ " <a href=\"https://www.instagram.com/fingenieriaudea/\"><img title=\"Instagram\" src="+IMAGE_SERVER.concat("instagram.png")+" alt=\"Tt\" width=\"32\" height=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;border-radius:50%\"></a></td></tr></table></td></tr></table>\r\n"
				+ "<table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\"><tr style=\"border-collapse:collapse;\"><td align=\"center\" style=\"padding:0;Margin:0;\"><table bgcolor=\"transparent\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\"cellspacing=\"0\" width=\"600\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td align=\"left\"style=\"padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;\">\r\n"
				+ "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td width=\"560\" align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n"
				+ "style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\"><td align=\"center\" style=\"padding:0;Margin:0;\">\r\n"
				+ "<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:'open sans', 'helvetica neue', helvetica, arial, sans-serif;line-height:28px;color:#698391;\">\r\n"
				+ "Facultad de Ingeniería Universidad de Antioquia | Vigilada Mineducación | Acreditación institucional hasta el 2022 NIT 890980040-8 | <a href=\"http://ingenieria.udea.edu.co\">ingenieria.udea.edu.co</a>| Líneas de Atención al Ciudadano | 01 8000 416384 [+57-4] 2198332 | Dirección: calle 67 No. 53 - 108 | <a href=\"mailto:atencionciudadano@udea.edu.co\">atencionciudadano@udea.edu.co</a></p>\r\n"
				+ "<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:'open sans', 'helvetica neue', helvetica, arial, sans-serif;line-height:28px;color:#698391;\">\r\n"
				+ " Este mensaje ha sido enviado a través de su suscripción en Onomástico. Para anular su suscripción, haga clic  <a target=\"_blank\" href=\'"+unsubscribeEmail+"'"
				+ " \" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:'open sans', 'helvetica neue', helvetica, arial, sans-serif;font-size:14px;text-decoration:underline;color:#698391;\">aquí</a>.</p>\r\n"
				+ "</td></tr></table></td></tr></table></td></tr></table></td></tr></table></td></tr></table></div></body></html>";
		return body;
	}
	
	private String emailGenericContent(String plantilla, String unsubscribeEmail) {
		String body = "<html style=\"width:100%;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0;\"><head><meta charset=\"UTF-8\"><meta content=\"width=device-width, initial-scale=1\" name=\"viewport\"><meta name=\"x-apple-disable-message-reformatting\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n"
				+ "<meta content=\"telephone=no\" name=\"format-detection\"><link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,400i,700,700i\" rel=\"stylesheet\">\r\n"
				+ "<style type=\"text/css\" >img{max-width: 600px !important;} @media only screen and (max-width:600px) {p, ul li, ol li, a { font-size:14px!important; line-height:150%!important } h1 { font-size:28px!important; text-align:center; line-height:120%!important } h2 { font-size:26px!important; text-align:center; line-height:120%!important } h3 { font-size:20px!important; text-align:center; line-height:120%!important } h1 a { font-size:28px!important } h2 a { font-size:26px!important } h3 a { font-size:20px!important } .es-menu td a { font-size:12px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:12px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:14px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:11px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { \r\n"
				+ "text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button { font-size:14px!important; display:block!important; border-left-width:0px!important; border-right-width:0px!important } .es-btn-fw { border-width:10px 0px!important; text-align:center!important } .es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r \r\n"
				+ "{padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } .es-desk-hidden { display:table-row!important; width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } .es-desk-menu-hidden { display:table-cell!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } }#outlook a {	padding:0;}.ExternalClass {	width:100%;}.ExternalClass,.ExternalClass p,.ExternalClass span,.ExternalClass font,.ExternalClass td,.ExternalClass div {	line-height:100%;}.es-button \r\n"
				+ "{mso-style-priority:100!important;	text-decoration:none!important;}a[x-apple-data-detectors] {	color:inherit!important;	text-decoration:none!important;	font-size:inherit!important;	font-family:inherit!important;	font-weight:inherit!important;	line-height:inherit!important;}.es-desk-hidden {	display:none;	float:left;	overflow:hidden;	width:0;	max-height:0;	line-height:0;	mso-hide:all;}</style>\r\n"
				+ "</head><body style=\"width:100%;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0;\"><div class=\"es-wrapper-color\" style=\"background-color:#F6F6F6;\">\r\n"
				+ "<table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td valign=\"top\" style=\"padding:0;Margin:0;\"><table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"> <td align=\"center\" bgcolor=\"#EFF3F5\" style=\"padding:0;Margin:0;background-color:#EFF3F5;\">\r\n"
				+ "<table bgcolor=\"#ffffff\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;\"> <tr style=\"border-collapse:collapse;\">\r\n"
				+ "<td align=\"center\" style=\"padding:4%;Margin:0\">\r\n"
				+ "    <img src="+IMAGE_SERVER.concat("logo-udea.png")+" width=\"312\">\r\n"
				+ "    </td></tr></table></td></tr></table>\r\n"
				+ "<table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\"style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td align=\"center\" bgcolor=\"#EFF3F5\" style=\"padding:0;Margin:0;background-color:#EFF3F5;\">\r\n"
				+ "<table bgcolor=\"#ffffff\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\"cellspacing=\"0\" width=\"600\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td align=\"center\" style=\"padding:0;Margin:0\">"
				+ plantilla + "</td></tr></table></td></tr></table>"
				+ "<table class=\"es-content-body\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"transparent\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\"><tr style=\"border-collapse:collapse;\"><td align=\"left\" style=\"padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\">\r\n"
				+ "<td width=\"560\" align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;\">\r\n"
				+ "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\"><td class=\"es-m-txt-c\" align=\"center\" style=\"padding:0;Margin:0;\"><table class=\"es-table-not-adapt es-social\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\"><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:40px;\">\r\n"
				+ "<a href=\"https://www.facebook.com/universidaddeantioquia/\"><img title=\"Facebook\" src="+IMAGE_SERVER.concat("facebook.png")+" alt=\"Fb\" width=\"32\" height=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;border-radius:50%\"></a></td>\r\n"
				+ "<td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:40px;\">\r\n"
				+ " <a href=\"https://twitter.com/UdeA\"><img title=\"Twitter\" src= "+IMAGE_SERVER.concat("twitter.png")+" alt=\"Tw\" width=\"32\" height=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;border-radius:50%\"></a></td><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:40px;\">\r\n"
				+ " <a href=\"https://www.youtube.com/channel/UCszBPa0AyP2aL-81wdvs_gg\"><img title=\"Youtube\" src="+IMAGE_SERVER.concat("youtube.png")+" alt=\"Yt\" width=\"32\" height=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;border-radius:50%\"></a></td><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;\">\r\n"
				+ " <a href=\"https://www.instagram.com/UdeA/\"><img title=\"Instagram\" src="+IMAGE_SERVER.concat("instagram.png")+" alt=\"Tt\" width=\"32\" height=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;border-radius:50%\"></a></td></tr></table></td></tr></table>\r\n"
				+ "<table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;\"><tr style=\"border-collapse:collapse;\"><td align=\"center\" style=\"padding:0;Margin:0;\"><table bgcolor=\"transparent\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\"cellspacing=\"0\" width=\"600\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td align=\"left\"style=\"padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;\">\r\n"
				+ "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\">\r\n"
				+ "<tr style=\"border-collapse:collapse;\"><td width=\"560\" align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n"
				+ "style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;\"><tr style=\"border-collapse:collapse;\"><td align=\"center\" style=\"padding:0;Margin:0;\">\r\n"
				+ "<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:'open sans', 'helvetica neue', helvetica, arial, sans-serif;line-height:28px;color:#698391;\">\r\n"
				+ "Universidad de Antioquia | Vigilada Mineducación | Acreditación institucional hasta el 2022 NIT 890980040-8 | <a href=\"http://www.udea.edu.co\">www.udea.edu.co</a>| Líneas de Atención al Ciudadano | 01 8000 416384 [+57-4] 2198332 <a href=\"mailto:atencionciudadano@udea.edu.co\">atencionciudadano@udea.edu.co</a></p>\r\n"
				+ "<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:'open sans', 'helvetica neue', helvetica, arial, sans-serif;line-height:28px;color:#698391;\">\r\n"
				+ " Este mensaje ha sido enviado a través de su suscripción en Onomástico. Para anular su suscripción, haga clic  <a target=\"_blank\" href=\'"+unsubscribeEmail+"'"
				+ " \" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:'open sans', 'helvetica neue', helvetica, arial, sans-serif;font-size:14px;text-decoration:underline;color:#698391;\">aquí</a>.</p>\r\n"
				+ "</td></tr></table></td></tr></table></td></tr></table></td></tr></table></td></tr></table></div></body></html>";
		
		return body;
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
