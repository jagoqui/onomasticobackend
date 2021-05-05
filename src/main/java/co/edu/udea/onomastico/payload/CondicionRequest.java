package co.edu.udea.onomastico.payload;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

public class CondicionRequest {
	@JsonView(Views.Public.class)
	private String id;
	@JsonView(Views.Public.class)
	private String condicion;
	@JsonView(Views.Public.class)
	private String value;
	
	public CondicionRequest(String id, String condicion, String value) {
		super();
		this.id = id;
		this.condicion = condicion;
		this.value = value;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
