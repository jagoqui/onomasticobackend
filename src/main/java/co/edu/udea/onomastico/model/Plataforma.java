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
	
	@ManyToMany(mappedBy = "plataformaPorUsuarioCorreo")
	@JsonView(Views.Internal.class)
	// @OnDelete(action = OnDeleteAction.CASCADE)
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

	@Override
	public String toString() {
		return "Plataforma{" +
				"id=" + id +
				", nombre='" + nombre + '\'' +
				'}';
	}
}
