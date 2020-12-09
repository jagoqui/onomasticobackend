package co.edu.udea.onomastico.model;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name = "asociacion")
public class Asociacion implements Serializable {
	
	@JsonView(Views.Public.class)
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@JsonView(Views.Public.class)
	@Column(name = "nombre", nullable = false, length=45)
	private String nombre;
	
	@JsonView(Views.Internal.class)
	@ManyToMany(mappedBy = "asociacionPorUsuario")
	@JsonIgnoreProperties("asociacionPorUsuario")
    private Set<Usuario> usuariosAsociacion = new HashSet<>();
	
	@JsonView(Views.Internal.class)
	@ManyToMany(mappedBy = "asociacionPorUsuarioCorreo")
	@JsonIgnoreProperties("asociacionPorUsuarioCorreo")
    private Set<UsuarioCorreo> usuariosCorreoAsociacion = new HashSet<>();

	public Asociacion() {
		super();
	}

	public Asociacion(int id, String nombre, Set<Usuario> usuariosAsociacion,
			Set<UsuarioCorreo> usuariosCorreoAsociacion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.usuariosAsociacion = usuariosAsociacion;
		this.usuariosCorreoAsociacion = usuariosCorreoAsociacion;
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

	public Set<Usuario> getUsuariosAsociacion() {
		return usuariosAsociacion;
	}

	public void setUsuariosAsociacion(Set<Usuario> usuariosAsociacion) {
		this.usuariosAsociacion = usuariosAsociacion;
	}

	public Set<UsuarioCorreo> getUsuariosCorreoAsociacion() {
		return usuariosCorreoAsociacion;
	}

	public void setUsuariosCorreoAsociacion(Set<UsuarioCorreo> usuariosCorreoAsociacion) {
		this.usuariosCorreoAsociacion = usuariosCorreoAsociacion;
	}
}	