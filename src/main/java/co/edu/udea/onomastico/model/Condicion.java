package co.edu.udea.onomastico.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
	@Id
	@Column(name ="idcondicion")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@JsonView(Views.Public.class)
	@Column(name = "condicion")
	private String condicion;
	
	@JsonView(Views.Public.class)
	@Column(name = "parametro")
	private String parametro;
	

	@ManyToMany(mappedBy = "condicionesEvento")
    private Set<Evento> usuariosCorreoCondicion = new HashSet<Evento>();
	
	public Condicion() {
		super();
	}

	
	public Condicion(int id, String condicion, String parametro, Set<Evento> usuariosCorreoCondicion) {
		super();
		this.id = id;
		this.condicion = condicion;
		this.parametro = parametro;
		this.usuariosCorreoCondicion = usuariosCorreoCondicion;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public Set<Evento> getUsuariosCorreoCondicion() {
		return usuariosCorreoCondicion;
	}

	public void setUsuariosCorreoCondicion(Set<Evento> usuariosCorreoCondicion) {
		this.usuariosCorreoCondicion = usuariosCorreoCondicion;
	}
	
	
}
