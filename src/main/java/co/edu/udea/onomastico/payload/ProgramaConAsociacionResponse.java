package co.edu.udea.onomastico.payload;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

public class ProgramaConAsociacionResponse {
	@JsonView(Views.Public.class)
	private int id; // de asociación
	@JsonView(Views.Public.class)
	private int codigo; //de programa
	@JsonView(Views.Public.class)
	private String nombre;
	public ProgramaConAsociacionResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProgramaConAsociacionResponse(int id, int codigo, String nombre) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nombre = nombre;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	} 
}
