package co.edu.udea.onomastico.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "programa_academico")
public class ProgramaAcademico {
	
	@JsonView(Views.Public.class)
	@Id 
	private int codigo;
	
	@Column(name = "nombre", length = 45, nullable = false)
	@JsonView(Views.Public.class)
	private String nombre;
	
	@ManyToMany(mappedBy = "programaAcademicoPorUsuarioCorreo")
	@JsonView(Views.Internal.class)
    private Set<UsuarioCorreo> usuariosCorreoProgramaAcademico = new HashSet<>();

	public ProgramaAcademico() {
		super();
	}

	public ProgramaAcademico(int codigo, String nombre, Set<UsuarioCorreo> usuariosCorreoProgramaAcademico) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.usuariosCorreoProgramaAcademico = usuariosCorreoProgramaAcademico;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<UsuarioCorreo> getUsuariosCorreoProgramaAcademico() {
		return usuariosCorreoProgramaAcademico;
	}

	public void setUsuariosCorreoProgramaAcademico(Set<UsuarioCorreo> usuariosCorreo) {
		this.usuariosCorreoProgramaAcademico = usuariosCorreo;
	}

	
}
