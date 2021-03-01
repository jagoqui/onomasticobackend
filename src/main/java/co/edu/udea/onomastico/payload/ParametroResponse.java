package co.edu.udea.onomastico.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

public class ParametroResponse {
	@JsonView(Views.Public.class)
	private int id;
	@JsonView(Views.Public.class)
	private List<ValorResponse> valor;
	public ParametroResponse(int id, List<ValorResponse> valor) {
		super();
		this.id = id;
		this.valor = valor;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<ValorResponse> getValor() {
		return valor;
	}
	public void setValor(List<ValorResponse> valor) {
		this.valor = valor;
	}
	
}
