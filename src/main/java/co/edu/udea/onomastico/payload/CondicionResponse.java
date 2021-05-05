package co.edu.udea.onomastico.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

public class CondicionResponse {
	@JsonView(Views.Public.class)
	private String name;
	@JsonView(Views.Public.class)
	private List<ParametroResponse> parametros;
	public CondicionResponse(String condicion, List<ParametroResponse> parametros) {
		super();
		this.name = condicion;
		this.parametros = parametros;
	}
	public String getCondicion() {
		return name;
	}
	public void setCondicion(String condicion) {
		this.name = condicion;
	}
	public List<ParametroResponse> getParametros() {
		return parametros;
	}
	public void setParametros(List<ParametroResponse> parametros) {
		this.parametros = parametros;
	}
}
