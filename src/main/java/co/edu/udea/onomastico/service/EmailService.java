package co.edu.udea.onomastico.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	public JavaMailSender emailSender;
	
	Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	public void sendEmail(String to, String subject, String text) {
		MimeMessageHelper helper;
		MimeMessage message = emailSender.createMimeMessage();
		helper = new MimeMessageHelper(message);
		try {
			helper.setFrom("aplicaciononomastico@udea.edu.co");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText("<html><body><div style=\"max-width: 800px; margin: auto;\">" + text + "</div></body></html>", true);
			emailSender.send(message);
		} catch (MessagingException e) {
			logger.error("Error enviando Email a: " + to + ", error: " + e.getMessage(), e);
		}
	}
}
