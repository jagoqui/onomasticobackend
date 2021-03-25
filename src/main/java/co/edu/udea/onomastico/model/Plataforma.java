package co.edu.udea.onomastico.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "plataforma")
public class Plataforma {

	@Id 
	@Column(name = "idplataforma", nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonView(Views.Public.class)	
	private int id;
	
	@Column(name = "nombre", nullable = false, length = 45)
	@JsonView(Views.Public.class)
	private String nombre;
	
	@ManyToMany(mappedBy = "plataformaPorUsuarioCorreo")
	@OnDelete(action=OnDeleteAction.CASCADE) 
	@JsonView(Views.Internal.class)
    private Set<UsuarioCorreo> usuariosCorreoPlataforma = new HashSet<>();

	public Plataforma() {
		super();
	}

	public Plataforma(int id, String nombre, Set<UsuarioCorreo> usuariosCorreoPlataforma) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.usuariosCorreoPlataforma = usuariosCorreoPlataforma;
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

	public Set<UsuarioCorreo> getUsuariosCorreoPlataforma() {
		return usuariosCorreoPlataforma;
	}

	public void setUsuariosCorreoPlataforma(Set<UsuarioCorreo> usuariosCorreoPlataforma) {
		this.usuariosCorreoPlataforma = usuariosCorreoPlataforma;
	}

	
}
