package co.edu.udea.onomastico.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

public class ParametroResponse {
	@JsonView(Views.Public.class)
	private int id;
	@JsonView(Views.Public.class)
	private List<ValorResponse> valores;
	public ParametroResponse(int id, List<ValorResponse> valores) {
		super();
		this.id = id;
		this.valores = valores;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<ValorResponse> getValores() {
		return valores;
	}
	public void setValor(List<ValorResponse> valores) {
		this.valores = valores;
	}
	
}
