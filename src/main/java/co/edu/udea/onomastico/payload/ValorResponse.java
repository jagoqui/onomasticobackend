package co.edu.udea.onomastico.payload;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

public class ValorResponse {
	@JsonView(Views.Public.class)
	private int id;
	@JsonView(Views.Public.class)
	private String nombre;
	public ValorResponse(int id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
