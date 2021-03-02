package co.edu.udea.onomastico.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

public class CondicionResponse {
	@JsonView(Views.Public.class)
	private String condicion;
	@JsonView(Views.Public.class)
	private List<ParametroResponse> parametros;
	public CondicionResponse(String condicion, List<ParametroResponse> parametros) {
		super();
		this.condicion = condicion;
		this.parametros = parametros;
	}
	public String getCondicion() {
		return condicion;
	}
	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}
	public List<ParametroResponse> getParametros() {
		return parametros;
	}
	public void setParametros(List<ParametroResponse> parametros) {
		this.parametros = parametros;
	}
}
