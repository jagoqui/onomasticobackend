package co.edu.udea.onomastico.model;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="usuario", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
	            "email"
	        })
})
public class Usuario {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name = "nombre_usuario", length = 16, nullable = false)
	private String username; 
	
	@Column(name = "email", length = 255, unique = true, nullable = false)
	private String email; 
	
	@JsonView(Views.Internal.class)
	@Column(name = "password", length = 32, nullable = false)
	private String password; 
	
	@Column(name = "estado", length = 10, nullable = false)
	private String estado; 
	
	@Column(name = "create_time", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private java.util.Date createTime;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "rol_id", nullable = false)
	private Rol rol;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "asociacion_por_usuario", 
            joinColumns = { @JoinColumn(name = "usuario_id") }, 
            inverseJoinColumns = { @JoinColumn(name = "asociacion_id") })
	@JsonView(Views.Internal.class)
    private Set<Asociacion> asociacionPorUsuario = new HashSet<Asociacion>();
	
	public Usuario() {
		super();
	}

	public Usuario(int id, String nombre, String email, String password, String estado, Date createTime, Rol rol,
			Set<Asociacion> asociacionPorUsuario) {
		super();
		this.id = id;
		this.username = nombre;
		this.email = email;
		this.password = password;
		this.estado = estado;
		this.createTime = createTime;
		this.rol = rol;
		this.asociacionPorUsuario = asociacionPorUsuario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return username;
	}

	public void setNombre(String nombre) {
		this.username = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Set<Asociacion> getAsociacionPorUsuario() {
		return asociacionPorUsuario;
	}

	public void setAsociacionPorUsuario(Set<Asociacion> asociacionPorUsuario) {
		this.asociacionPorUsuario = asociacionPorUsuario;
	}

}
