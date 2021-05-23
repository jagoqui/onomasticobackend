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
	@ManyToMany(mappedBy = "usuariosCorreoAsociacion")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Asociacion> asociacionPorUsuarioCorreo = new HashSet<>();
	
	@JsonView(Views.Public.class)
	@ManyToMany(mappedBy = "usuariosCorreoProgramaAcademico")
	@JsonIgnoreProperties("usuariosCorreoProgramaAcademico")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ProgramaAcademico> programaAcademicoPorUsuarioCorreo = new HashSet<>();

	@JsonView(Views.Public.class)
	@ManyToMany(mappedBy = "usuariosCorreoPlataforma")
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