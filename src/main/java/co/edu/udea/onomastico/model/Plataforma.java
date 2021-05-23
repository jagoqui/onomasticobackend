package co.edu.udea.onomastico.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "plataforma")
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Plataforma implements Serializable {

	@Id 
	@Column(name = "idplataforma", nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonView(Views.Public.class)	
	private int id;
	
	@Column(name = "nombre", nullable = false, length = 45)
	@JsonView(Views.Public.class)
	private String nombre;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "plataforma_por_usuario_correo", joinColumns = {
			@JoinColumn(name = "plataforma_idplataforma")}, inverseJoinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion")})
	@JsonView(Views.Internal.class)
	@OnDelete(action = OnDeleteAction.CASCADE)
    private Set<UsuarioCorreo> usuariosCorreoPlataforma = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Plataforma that = (Plataforma) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
