package co.edu.udea.onomastico.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "usuario_correo")
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UsuarioCorreo implements Serializable {

	@JsonView(Views.Public.class)
	@EmbeddedId
	private UsuarioCorreoId id;

	@JsonView(Views.Public.class)
	@Column(name = "nombre")
	private String nombre;

	@JsonView(Views.Public.class)
	@Column(name = "apellido")
	private String apellido;

	@JsonView(Views.Public.class)
	@Column(name = "email")
	private String email;

	@JsonView(Views.Public.class)
	@Column(name = "fecha_nacimiento")
	@Temporal(TemporalType.DATE)
	private java.util.Date fechaNacimiento;

	@JsonView(Views.Public.class)
	@Column(name = "estado")
	private String estado;

	@JsonView(Views.Public.class)
	@Column(name = "genero")
	private String genero;

	@JsonView(Views.Public.class)
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinTable(name = "unidad_administrativa_por_correo_usuario", joinColumns = {
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion"),
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion")
			}, inverseJoinColumns = @JoinColumn(name = "unidad_administrativa_id"))
	private Set<UnidadAdministrativa> unidadAdministrativaPorCorreoUsuario = new HashSet<>();
	
	@JsonView(Views.Public.class)
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinTable(name = "programa_academico_por_usuario_correo", joinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion"),
			}, inverseJoinColumns = @JoinColumn(name = "programa_academico_codigo"))
	private Set<ProgramaAcademico> programaAcademicoPorUsuarioCorreo = new HashSet<>();

	@JsonView(Views.Public.class)
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinTable(name = "plataforma_por_usuario_correo", joinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion")},
			inverseJoinColumns = @JoinColumn(name = "plataforma_idplataforma"))
	// @OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Plataforma> plataformaPorUsuarioCorreo = new HashSet<>();

	@JsonView(Views.Public.class)
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinTable(name = "vinculacion_por_usuario_correo", joinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion")},
			inverseJoinColumns = @JoinColumn(name = "vinculacion_idvinculacion"))
	@JsonIgnoreProperties("usuariosCorreoVinculacion")
	//@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Vinculacion> vinculacionPorUsuarioCorreo = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UsuarioCorreo that = (UsuarioCorreo) o;
		return id.equals(that.id);
	}

	public void addAsociacion(UnidadAdministrativa unidadAdministrativaToAdd){
		unidadAdministrativaPorCorreoUsuario.add(unidadAdministrativaToAdd);
		unidadAdministrativaToAdd.getUsuariosCorreoUnidadAcademica().add(this);
	}

	public void addProgramaAcademico(ProgramaAcademico programaToAdd){
		programaAcademicoPorUsuarioCorreo.add(programaToAdd);
		programaToAdd.getUsuariosCorreoProgramaAcademico().add(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "UsuarioCorreo{" +
				"id=" + id +
				", nombre='" + nombre + '\'' +
				", apellido='" + apellido + '\'' +
				", email='" + email + '\'' +
				", fechaNacimiento=" + fechaNacimiento +
				", estado='" + estado + '\'' +
				", genero='" + genero + '\'' +
				", unidadAdministrativaPorCorreoUsuario=" + unidadAdministrativaPorCorreoUsuario +
				", programaAcademicoPorUsuarioCorreo=" + programaAcademicoPorUsuarioCorreo +
				", plataformaPorUsuarioCorreo=" + plataformaPorUsuarioCorreo +
				", vinculacionPorUsuarioCorreo=" + vinculacionPorUsuarioCorreo +
				'}';
	}
}