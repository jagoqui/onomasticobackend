package co.edu.udea.onomastico.payload;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;

public class ProgramaConUnidadAcademicaResponse {
	@JsonView(Views.Public.class)
	private int idUnidadAcademica; // de unidad academica
	@JsonView(Views.Public.class)
	private int codigo; //de programa
	@JsonView(Views.Public.class)
	private String nombre;
	public ProgramaConUnidadAcademicaResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProgramaConUnidadAcademicaResponse(int idUnidadAcademica, int codigo, String nombre) {
		super();
		this.idUnidadAcademica = idUnidadAcademica;
		this.codigo = codigo;
		this.nombre = nombre;
	}
	public int getIdUnidadAcademica() {
		return idUnidadAcademica;
	}
	public void setIdUnidadAcademica(int idUnidadAcademica) {
		this.idUnidadAcademica = idUnidadAcademica;
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
