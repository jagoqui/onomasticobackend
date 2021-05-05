package co.edu.udea.onomastico.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

public class ParametroResponse {
	@JsonView(Views.Public.class)
	private int id;
	@JsonView(Views.Public.class)
	private String condicion;
	@JsonView(Views.Public.class)
	private String value;
	public ParametroResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ParametroResponse(int id, String condicion, String value) {
		super();
		this.id = id;
		this.condicion = condicion;
		this.value = value;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCondicion() {
		return condicion;
	}
	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
