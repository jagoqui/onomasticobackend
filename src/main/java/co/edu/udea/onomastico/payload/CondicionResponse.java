package co.edu.udea.onomastico.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

public class CondicionResponse {
	@JsonView(Views.Public.class)
	private String condicion;
	@JsonView(Views.Public.class)
	private List<ParametroResponse> parametro;
	public CondicionResponse(String condicion, List<ParametroResponse> parametro) {
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
	public List<ParametroResponse> getParametro() {
		return parametro;
	}
	public void setParametro(List<ParametroResponse> parametro) {
		this.parametro = parametro;
	}
}
