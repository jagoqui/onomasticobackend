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
@Table(name = "vinculacion")
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Vinculacion implements Serializable {
	
	@JsonView(Views.Public.class)
	@Id 
	@Column(name = "idvinculacion", nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@JsonView(Views.Public.class)
	@Column(name = "nombre", nullable = false)
	private String nombre;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "vinculacion_por_usuario_correo", joinColumns = {
			@JoinColumn(name = "vinculacion_idvinculacion"),}, inverseJoinColumns = {
			@JoinColumn(name = "usuario_correo_tipo_identificacion", referencedColumnName = "tipo_identificacion"),
			@JoinColumn(name = "usuario_correo_numero_identificacion", referencedColumnName = "numero_identificacion")})
	@JsonView(Views.Internal.class)
    private Set<UsuarioCorreo> usuariosCorreoVinculacion = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Vinculacion that = (Vinculacion) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Vinculacion{" +
				"id=" + id +
				", nombre='" + nombre + '\'' +
				'}';
	}
}
