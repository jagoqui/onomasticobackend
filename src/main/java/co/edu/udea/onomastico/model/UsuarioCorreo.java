package co.edu.udea.onomastico.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
	@NotNull
	@NotBlank
	@Column(name = "nombre", length = 150, nullable = false)
	private String nombre;

	@JsonView(Views.Public.class)
	@NotNull
	@NotBlank
	@Column(name = "apellido", length = 150, nullable = false)
	private String apellido;

	@JsonView(Views.Public.class)
	@Email
	@Column(name = "email", length = 250, nullable = false)
	private String email;

	@JsonView(Views.Public.class)
	@NotNull
	@Column(name = "fecha_nacimiento", nullable = false)
	@Temporal(TemporalType.DATE)
	private java.util.Date fechaNacimiento;

	@JsonView(Views.Public.class)
	@NotBlank
	@NotNull
	@Column(name = "estado", length = 10, nullable = false)
	private String estado;

	@JsonView(Views.Public.class)
	@NotBlank
	@NotNull
	@Column(name = "genero", length = 10)
	private String genero;

	@JsonView(Views.Public.class)
	@NotBlank
	@NotNull
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "asociacion_por_correo_usuario", joinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion"), }, inverseJoinColumns = {
					@JoinColumn(name = "asociacion_id") })
	@JsonIgnoreProperties({"usuariosAsociacion","usuariosCorreoAsociacion"})
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Asociacion> asociacionPorUsuarioCorreo = new HashSet<>();
	
	@JsonView(Views.Public.class)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "programa_academico_por_usuario_correo", joinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion"), }, inverseJoinColumns = {
					@JoinColumn(name = "programa_academico_codigo") })
	@JsonIgnoreProperties("usuariosCorreoProgramaAcademico")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ProgramaAcademico> programaAcademicoPorUsuarioCorreo = new HashSet<>();

	@JsonView(Views.Public.class)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "plataforma_por_usuario_correo", joinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion"), }, inverseJoinColumns = {
					@JoinColumn(name = "plataforma_idplataforma") })
	@JsonIgnoreProperties("usuariosCorreoPlataforma")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Plataforma> plataformaPorUsuarioCorreo = new HashSet<>();

	@JsonView(Views.Public.class)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "vinculacion_por_usuario_correo", joinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion"), }, inverseJoinColumns = {
					@JoinColumn(name = "vinculacion_idvinculacion") })
	@JsonIgnoreProperties("usuariosCorreoVinculacion")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Vinculacion> vinculacionPorUsuarioCorreo = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UsuarioCorreo that = (UsuarioCorreo) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}