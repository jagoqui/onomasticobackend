package co.edu.udea.onomastico.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;

@Embeddable
public class CorreoEnviadoId implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "usuario_correo_tipo_identificacion")
	private String tipoIdentificacion;
	 
	@Column(name = "usuario_correo_numero_identificacion")
	private String numeroIdentificacion;
	
	@Column(name = "evento_idevento")
	private int eventoId ;
	
	@Column(name = "fecha", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private java.util.Date fecha;

	public CorreoEnviadoId() {
		super();
	}
	

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public int getEventoId() {
		return eventoId;
	}

	public void setEventoId(int eventoId) {
		this.eventoId = eventoId;
	}

	public java.util.Date getFecha() {
		return fecha;
	}

	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eventoId;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((numeroIdentificacion == null) ? 0 : numeroIdentificacion.hashCode());
		result = prime * result + ((tipoIdentificacion == null) ? 0 : tipoIdentificacion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CorreoEnviadoId other = (CorreoEnviadoId) obj;
		if (eventoId != other.eventoId)
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (numeroIdentificacion == null) {
			if (other.numeroIdentificacion != null)
				return false;
		} else if (!numeroIdentificacion.equals(other.numeroIdentificacion))
			return false;
		if (tipoIdentificacion == null) {
			if (other.tipoIdentificacion != null)
				return false;
		} else if (!tipoIdentificacion.equals(other.tipoIdentificacion))
			return false;
		return true;
	}

}
