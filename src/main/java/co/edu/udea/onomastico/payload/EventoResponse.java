package co.edu.udea.onomastico.payload;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import co.edu.udea.onomastico.model.Plantilla;
import co.edu.udea.onomastico.model.Views;

public class EventoResponse {
	@JsonView(Views.Public.class)
	private int id;
	@JsonView(Views.Public.class)
	private String nombre;
	@JsonView(Views.Public.class)
	private java.util.Date fecha;
	@JsonView(Views.Public.class)
	private String estado;
	@JsonView(Views.Public.class)
	private String recurrencia;
	@JsonView(Views.Public.class)
	private Plantilla plantilla;
	@JsonView(Views.Public.class)
	private List<CondicionResponse> condicionesEvento;
	public EventoResponse(int id, String nombre, Date fecha, String estado, String recurrencia, Plantilla plantilla,
			List<CondicionResponse> condiciones) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.estado = estado;
		this.recurrencia = recurrencia;
		this.plantilla = plantilla;
		this.condicionesEvento = condiciones;
	}
	
	public EventoResponse(int id, String nombre, Date fecha, String estado, String recurrencia, Plantilla plantilla) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.estado = estado;
		this.recurrencia = recurrencia;
		this.plantilla = plantilla;
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
	public java.util.Date getFecha() {
		return fecha;
	}
	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getRecurrencia() {
		return recurrencia;
	}
	public void setRecurrencia(String recurrencia) {
		this.recurrencia = recurrencia;
	}
	public Plantilla getPlantilla() {
		return plantilla;
	}
	public void setPlantilla(Plantilla plantilla) {
		this.plantilla = plantilla;
	}
	public List<CondicionResponse> getCondiciones() {
		return condicionesEvento;
	}
	public void setCondiciones(List<CondicionResponse> condiciones) {
		this.condicionesEvento = condiciones;
	}  
	
}
