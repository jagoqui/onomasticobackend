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

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "correo_enviado")
public class CorreoEnviado {
	
	@EmbeddedId
	private CorreoEnviadoId id = new CorreoEnviadoId(); 

	@ManyToOne(optional=false)
	@JoinColumns({
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion",insertable = false, updatable = false),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion",insertable = false, updatable = false), })
	private UsuarioCorreo usuarioCorreoEnviado;
	
	@ManyToOne(optional=false)
	@JoinColumns({
		@JoinColumn(name = "evento_idevento", referencedColumnName = "idevento",insertable = false, updatable = false) })
	private Evento eventoEnviado;
	
	@Column(name = "fecha", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private java.util.Date fecha;
	
	@Column(name = "email",nullable = false, length = 250)
	private String email;

	public CorreoEnviado() {
		super();
	}

	public CorreoEnviado(CorreoEnviadoId id, Date fecha, String email) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.email = email;
	}

	public CorreoEnviadoId getId() {
		return id;
	}

	public void setId(CorreoEnviadoId id) {
		this.id = id;
	}

	public java.util.Date getFecha() {
		return fecha;
	}

	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

