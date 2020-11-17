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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "vinculacion")
public class Vinculacion {

	@Id 
	@Column(name = "idvinculacion", nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name = "nombre", nullable = false)
	private String nombre;

	@ManyToMany(mappedBy = "vinculacionPorUsuarioCorreo")
	@JsonIgnoreProperties("vinculacionPorUsuarioCorreo")
    private Set<UsuarioCorreo> usuariosCorreoVinculacion = new HashSet<>();
	
	public Vinculacion() {
		super();
	}

	public Vinculacion(int id, String nombre, Set<UsuarioCorreo> usuariosCorreo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.usuariosCorreoVinculacion = usuariosCorreo;
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

	public Set<UsuarioCorreo> getUsuariosCorreoVinculacion() {
		return usuariosCorreoVinculacion;
	}

	public void setUsuariosCorreoVinculacion(Set<UsuarioCorreo> usuariosCorreo) {
		this.usuariosCorreoVinculacion = usuariosCorreo;
	}

}
