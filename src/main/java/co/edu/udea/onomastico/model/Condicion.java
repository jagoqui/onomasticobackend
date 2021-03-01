package co.edu.udea.onomastico.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "condicion")
public class Condicion {

	@JsonView(Views.Public.class)
	@EmbeddedId
	private CondicionId id;
	
	@ManyToMany(mappedBy = "condicionesEvento")
    private Set<Evento> usuariosCorreoCondicion = new HashSet<Evento>();
	
	public Condicion() {
		super();
	}

	public Condicion(CondicionId id, Set<Evento> usuariosCorreoCondicion) {
		super();
		this.id = id;
		this.usuariosCorreoCondicion = usuariosCorreoCondicion;
	}

	public CondicionId getId() {
		return id;
	}

	public void setId(CondicionId id) {
		this.id = id;
	}

	public Set<Evento> getUsuariosCorreoCondicion() {
		return usuariosCorreoCondicion;
	}

	public void setUsuariosCorreoCondicion(Set<Evento> usuariosCorreoCondicion) {
		this.usuariosCorreoCondicion = usuariosCorreoCondicion;
	}

}
