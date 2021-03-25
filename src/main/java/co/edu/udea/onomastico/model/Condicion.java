package co.edu.udea.onomastico.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "condicion_por_evento")
public class Condicion {

	@JsonView(Views.Public.class)
	@EmbeddedId
	private CondicionId id;
	
	@ManyToOne 
	@OnDelete(action=OnDeleteAction.CASCADE) 
    @JoinColumn(name = "evento_idevento",referencedColumnName = "idevento",insertable = false, updatable = false) 
    private Evento eventoCondicion;
	
	public Condicion() {
		super();
	}

	public Condicion(CondicionId id, Evento eventoCondicion) {
		super();
		this.id = id;
		this.eventoCondicion = eventoCondicion;
	}

	public CondicionId getId() {
		return id;
	}

	public void setId(CondicionId id) {
		this.id = id;
	}

	public Evento getEventoCondicion() {
		return eventoCondicion;
	}

	public void setEventoCondicion(Evento eventoCondicion) {
		this.eventoCondicion = eventoCondicion;
	}

	
}
