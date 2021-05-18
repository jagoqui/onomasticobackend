package co.edu.udea.onomastico.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

@Entity
@Table(name = "rol")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Rol implements Serializable{

	@JsonView(Views.Public.class)
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@JsonView(Views.Public.class)
	@Column(name = "nombre", length = 45, nullable = false)
	private String nombre;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Rol rol = (Rol) o;
		return id == rol.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
