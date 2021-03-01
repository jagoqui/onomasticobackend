package co.edu.udea.onomastico.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonView;

@Embeddable
public class CondicionId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonView(Views.Public.class)
	@Column(name = "condicion")
	private String condicion;

	@JsonView(Views.Public.class)
	@Column(name = "parametro")
	private String parametro;

	public CondicionId() {
		super();
	}

	public CondicionId(String condicion, String parametro) {
		super();
		this.condicion = condicion;
		this.parametro = parametro;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((condicion == null) ? 0 : condicion.hashCode());
		result = prime * result + ((parametro == null) ? 0 : parametro.hashCode());
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
		CondicionId other = (CondicionId) obj;
		if (condicion == null) {
			if (other.condicion != null)
				return false;
		} else if (!condicion.equals(other.condicion))
			return false;
		if (parametro == null) {
			if (other.parametro != null)
				return false;
		} else if (!parametro.equals(other.parametro))
			return false;
		return true;
	}
	
}