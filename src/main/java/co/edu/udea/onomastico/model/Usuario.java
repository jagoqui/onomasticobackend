package co.edu.udea.onomastico.model;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
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

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="usuario", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
	            "email"
	        })
})
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario implements Serializable {

	@JsonView(Views.Public.class)
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@JsonView(Views.Public.class)
	@Column(name = "nombre_usuario", length = 16, nullable = false)
	private String nombre; 
	
	@JsonView(Views.Public.class)
	@Column(name = "email", length = 255, unique = true, nullable = false)
	private String email; 
	
	@JsonView(Views.Internal.class)
	@Column(name = "password", length = 32, nullable = false)
	private String password; 
	
	@JsonView(Views.Public.class)
	@Column(name = "estado", length = 10, nullable = false)
	private String estado; 
	
	@JsonView(Views.Public.class)
	@Column(name = "create_time", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private java.util.Date createTime;
	
	@JsonView(Views.Public.class)
	@OnDelete(action=OnDeleteAction.CASCADE) 
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "rol_id", nullable = false)
	private Rol rol;
	
	@JsonView(Views.Internal.class)
	@Column(name = "reset_token", length = 36, nullable = true)
	private String resetToken;
	
	@JsonView(Views.Public.class)
	@OnDelete(action=OnDeleteAction.CASCADE) 
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "asociacion_por_usuario", 
            joinColumns = { @JoinColumn(name = "usuario_id") }, 
            inverseJoinColumns = { @JoinColumn(name = "asociacion_id") })
    private Set<Asociacion> asociacionPorUsuario = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Usuario usuario = (Usuario) o;
		return id == usuario.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
