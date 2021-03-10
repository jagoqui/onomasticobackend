package co.edu.udea.onomastico.model;

import java.util.Date;
import java.util.HashSet;
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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "usuario_correo")
public class UsuarioCorreo {

	@JsonView(Views.Public.class)
	@EmbeddedId
	private UsuarioCorreoId id;

	@JsonView(Views.Public.class)
	@Column(name = "nombre", length = 150, nullable = false)
	private String nombre;

	@JsonView(Views.Public.class)
	@Column(name = "apellido", length = 150, nullable = false)
	private String apellido;

	@JsonView(Views.Public.class)
	@Column(name = "email", length = 250, nullable = false)
	private String email;

	@JsonView(Views.Public.class)
	@Column(name = "fecha_nacimiento", nullable = false)
	@Temporal(TemporalType.DATE)
	private java.util.Date fechaNacimiento;

	@JsonView(Views.Public.class)
	@Column(name = "estado", length = 10, nullable = false)
	private String estado;

	@JsonView(Views.Public.class)
	@Column(name = "genero", length = 10)
	private String genero;

	@JsonView(Views.Public.class)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "asociacion_por_correo_usuario", joinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion"), }, inverseJoinColumns = {
					@JoinColumn(name = "asociacion_id") })
	@JsonIgnoreProperties({"usuariosAsociacion","usuariosCorreoAsociacion"})
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Asociacion> asociacionPorUsuarioCorreo = new HashSet<Asociacion>();
	
	@JsonView(Views.Public.class)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "programa_academico_por_usuario_correo", joinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion"), }, inverseJoinColumns = {
					@JoinColumn(name = "programa_academico_codigo") })
	@JsonIgnoreProperties("usuariosCorreoProgramaAcademico")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ProgramaAcademico> programaAcademicoPorUsuarioCorreo = new HashSet<ProgramaAcademico>();

	@JsonView(Views.Public.class)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "plataforma_por_usuario_correo", joinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion"), }, inverseJoinColumns = {
					@JoinColumn(name = "plataforma_idplataforma") })
	@JsonIgnoreProperties("usuariosCorreoPlataforma")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Plataforma> plataformaPorUsuarioCorreo = new HashSet<Plataforma>();

	@JsonView(Views.Public.class)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "vinculacion_por_usuario_correo", joinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion"), }, inverseJoinColumns = {
					@JoinColumn(name = "vinculacion_idvinculacion") })
	@JsonIgnoreProperties("usuariosCorreoVinculacion")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Vinculacion> vinculacionPorUsuarioCorreo = new HashSet<Vinculacion>();

	public UsuarioCorreo() {
		super();
	}

	public UsuarioCorreo(UsuarioCorreoId id, String nombre, String apellido, String email, Date fechaNacimiento,
			String estado, String genero) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.fechaNacimiento = fechaNacimiento;
		this.estado = estado;
		this.genero = genero;
	}

	public UsuarioCorreo(UsuarioCorreoId id, String nombre, String apellido, String email, Date fechaNacimiento,
			String estado, String genero, Set<Asociacion> asociacionPorUsuarioCorreo,
			Set<ProgramaAcademico> programaAcademicoPorUsuarioCorreo, Set<Plataforma> plataformaPorUsuarioCorreo,
			Set<Vinculacion> vinculacionPorUsuarioCorreo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.fechaNacimiento = fechaNacimiento;
		this.estado = estado;
		this.genero = genero;
		this.asociacionPorUsuarioCorreo = asociacionPorUsuarioCorreo;
		this.programaAcademicoPorUsuarioCorreo = programaAcademicoPorUsuarioCorreo;
		this.plataformaPorUsuarioCorreo = plataformaPorUsuarioCorreo;
		this.vinculacionPorUsuarioCorreo = vinculacionPorUsuarioCorreo;
	}

	public UsuarioCorreoId getId() {
		return id;
	}

	public void setId(UsuarioCorreoId id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public java.util.Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(java.util.Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Set<Asociacion> getAsociacionPorUsuarioCorreo() {
		return asociacionPorUsuarioCorreo;
	}

	public void setAsociacionPorUsuarioCorreo(Set<Asociacion> asociacionPorUsuarioCorreo) {
		this.asociacionPorUsuarioCorreo = asociacionPorUsuarioCorreo;
	}

	public Set<ProgramaAcademico> getProgramaAcademicoPorUsuarioCorreo() {
		return programaAcademicoPorUsuarioCorreo;
	}

	public void setProgramaAcademicoPorUsuarioCorreo(Set<ProgramaAcademico> programaAcademicoPorUsuarioCorreo) {
		this.programaAcademicoPorUsuarioCorreo = programaAcademicoPorUsuarioCorreo;
	}

	public Set<Plataforma> getPlataformaPorUsuarioCorreo() {
		return plataformaPorUsuarioCorreo;
	}

	public void setPlataformaPorUsuarioCorreo(Set<Plataforma> plataformaPorUsuarioCorreo) {
		this.plataformaPorUsuarioCorreo = plataformaPorUsuarioCorreo;
	}

	public Set<Vinculacion> getVinculacionPorUsuarioCorreo() {
		return vinculacionPorUsuarioCorreo;
	}

	public void setVinculacionPorUsuarioCorreo(Set<Vinculacion> vinculacionPorUsuarioCorreo) {
		this.vinculacionPorUsuarioCorreo = vinculacionPorUsuarioCorreo;
	}
}