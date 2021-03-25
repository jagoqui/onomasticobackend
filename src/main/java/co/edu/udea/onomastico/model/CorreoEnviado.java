package co.edu.udea.onomastico.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "correo_enviado")
public class CorreoEnviado {
	
	@EmbeddedId
	private CorreoEnviadoId id = new CorreoEnviadoId(); 
	
	@JsonView(Views.Public.class)
	@Column(name = "asunto",nullable = false, length = 250)
	private String asunto;

	public CorreoEnviado() {
		super();
	}
	
	public CorreoEnviado(CorreoEnviadoId id, String asunto) {
		super();
		this.id = id;
		this.asunto = asunto;
	}

	public CorreoEnviadoId getId() {
		return id;
	}

	public void setId(CorreoEnviadoId id) {
		this.id = id;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	
}

