package co.edu.udea.onomastico.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Asociacion;
import co.edu.udea.onomastico.model.ProgramaAcademico;
import co.edu.udea.onomastico.model.Views;

public class ProgramasPorAsociacionResponse {
	@JsonView(Views.Public.class)
	private Asociacion asociacion;
	@JsonView(Views.Public.class)
	private List<ProgramaAcademico> programas;
	
	public ProgramasPorAsociacionResponse() {
		super();
	}

	public ProgramasPorAsociacionResponse(Asociacion asociacion, List<ProgramaAcademico> programas) {
		super();
		this.asociacion = asociacion;
		this.programas = programas;
	}

	public Asociacion getAsociacion() {
		return asociacion;
	}

	public void setAsociacion(Asociacion asociacion) {
		this.asociacion = asociacion;
	}

	public List<ProgramaAcademico> getProgramas() {
		return programas;
	}

	public void setProgramas(List<ProgramaAcademico> programas) {
		this.programas = programas;
	}
}
