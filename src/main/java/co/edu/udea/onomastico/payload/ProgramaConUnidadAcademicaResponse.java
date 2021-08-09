package co.edu.udea.onomastico.payload;

import co.edu.udea.onomastico.model.UnidadAcademica;
import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Views;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramaConUnidadAcademicaResponse {

	@JsonView(Views.Public.class)
	private int codigo; //de programa
	@JsonView(Views.Public.class)
	private String nombre;
	@JsonView(Views.Public.class)
	private UnidadAcademica unidadAcademica;
	public ProgramaConUnidadAcademicaResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProgramaConUnidadAcademicaResponse(int codigo, String nombre, UnidadAcademica unidadAcademica) {
		super();
		this.unidadAcademica = unidadAcademica;
		this.codigo = codigo;
		this.nombre = nombre;
	}
}
