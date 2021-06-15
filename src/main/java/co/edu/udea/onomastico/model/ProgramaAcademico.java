package co.edu.udea.onomastico.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonView;

import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

@Entity
@Table(name = "programa_academico")
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProgramaAcademico implements Serializable {
	
	@JsonView(Views.Public.class)
	@Id 
	private int codigo;
	
	@Column(name = "nombre", length = 60, nullable = false)
	@JsonView(Views.Public.class)
	private String nombre;
	
	@ManyToMany(mappedBy = "programaAcademicoPorUsuarioCorreo")
	@JsonView(Views.Internal.class)
    private Set<UsuarioCorreo> usuariosCorreoProgramaAcademico = new HashSet<>();


	@JsonView(Views.Internal.class)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "asociacion")
	private Asociacion asociacion;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProgramaAcademico that = (ProgramaAcademico) o;
		return codigo == that.codigo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}

	@Override
	public String toString() {
		return "ProgramaAcademico{" +
				"codigo=" + codigo +
				", nombre='" + nombre + '\'' +
				'}';
	}
}
