package co.edu.udea.onomastico.payload;

import java.util.Date;
import java.util.Set;

import co.edu.udea.onomastico.model.Plantilla;

public class EventoRequest {
	private int id;
	private String nombre;
	private java.util.Date fecha;
	private String estado;
	private String recurrencia;
	private Plantilla plantilla;
	private Set<CondicionRequest> condicionesEvento;
	public EventoRequest(int id, String nombre, Date fecha, String estado, String recurrencia, Plantilla plantilla,
			Set<CondicionRequest> condicionesEvento) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.estado = estado;
		this.recurrencia = recurrencia;
		this.plantilla = plantilla;
		this.condicionesEvento = condicionesEvento;
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
	public Set<CondicionRequest> getCondicionesEvento() {
		return condicionesEvento;
	}
	public void setCondicionesEvento(Set<CondicionRequest> condicionesEvento) {
		this.condicionesEvento = condicionesEvento;
	}
}
