package co.edu.udea.onomastico.payload;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

public class CondicionResponse {
	@JsonView(Views.Public.class)
	private String condicion;
	@JsonView(Views.Public.class)
	private ParametroResponse parametro;
	public CondicionResponse(String condicion, ParametroResponse parametro) {
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
	public ParametroResponse getParametro() {
		return parametro;
	}
	public void setParametro(ParametroResponse parametro) {
		this.parametro = parametro;
	}
	
}
