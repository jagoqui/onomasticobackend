package co.edu.udea.onomastico.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "programa_academico")
public class ProgramaAcademico {
	
	@JsonView(Views.Public.class)
	@Id 
	private int codigo;
	
	@Column(name = "nombre", length = 60, nullable = false)
	@JsonView(Views.Public.class)
	private String nombre;
	
	@OnDelete(action=OnDeleteAction.CASCADE) 
	@ManyToMany(mappedBy = "programaAcademicoPorUsuarioCorreo")
	@JsonView(Views.Internal.class)
    private Set<UsuarioCorreo> usuariosCorreoProgramaAcademico = new HashSet<>();
	
	@JsonView(Views.Internal.class)
	@OnDelete(action=OnDeleteAction.CASCADE) 
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "programa_academico_por_asociacion", joinColumns = {
			@JoinColumn(name = "programa_academico_codigo")
			}, inverseJoinColumns = {
					@JoinColumn(name = "asociacion_id") })
	private Set<Asociacion> programaAcademicoPorAsociacion = new HashSet<Asociacion>();

	public ProgramaAcademico() {
		super();
	}

	public ProgramaAcademico(int codigo, String nombre, Set<UsuarioCorreo> usuariosCorreoProgramaAcademico,
			Set<Asociacion> programaAcademicoPorAsociacion) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.usuariosCorreoProgramaAcademico = usuariosCorreoProgramaAcademico;
		this.programaAcademicoPorAsociacion = programaAcademicoPorAsociacion;
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

	public void setUsuariosCorreoProgramaAcademico(Set<UsuarioCorreo> usuariosCorreoProgramaAcademico) {
		this.usuariosCorreoProgramaAcademico = usuariosCorreoProgramaAcademico;
	}

	public Set<Asociacion> getProgramaAcademicoPorAsociacion() {
		return programaAcademicoPorAsociacion;
	}

	public void setProgramaAcademicoPorAsociacion(Set<Asociacion> programaAcademicoPorAsociacion) {
		this.programaAcademicoPorAsociacion = programaAcademicoPorAsociacion;
	}
	
}
