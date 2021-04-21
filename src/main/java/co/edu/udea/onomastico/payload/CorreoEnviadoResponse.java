package co.edu.udea.onomastico.payload;

import java.util.Date;

public class CorreoEnviadoResponse {
	private String email;
	private java.util.Date fecha;
	private String asunto;
	
	public CorreoEnviadoResponse() {
		super();
	}

	public CorreoEnviadoResponse(String email, Date fecha, String asunto) {
		super();
		this.email = email;
		this.fecha = fecha;
		this.asunto = asunto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public java.util.Date getFecha() {
		return fecha;
	}

	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	
	
}
