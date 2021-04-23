package co.edu.udea.onomastico.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonView;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "log_transacciones")
public class LogTransacciones implements Serializable{
	
	@JsonView(Views.Public.class)
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@JsonView(Views.Public.class)
	@Column(name = "transaccion", length = 200)
	private String transaccion;
	
	@JsonView(Views.Public.class)
	@Column(name = "fecha")
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private java.util.Date fecha;
	
	@JsonView(Views.Public.class)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Usuario usuario;

	public LogTransacciones() {
		super();
	}

	public LogTransacciones(int id, String transaccion, Date fecha, Usuario usuario) {
		super();
		this.id = id;
		this.transaccion = transaccion;
		this.fecha = fecha;
		this.usuario = usuario;
	}
	
	public LogTransacciones(String transaccion) {
		super();
		this.transaccion = transaccion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	public java.util.Date getFecha() {
		return fecha;
	}

	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}
