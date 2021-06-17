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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;


@Entity
@Table(name = "asociacion")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Asociacion implements Serializable {
	
	@JsonView(Views.Public.class)
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@JsonView(Views.Public.class)
	@Column(name = "nombre", nullable = false, length=60)
	private String nombre;
	
	@JsonView(Views.Internal.class)
	@ManyToMany(mappedBy = "asociacionPorUsuario")
	// @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Usuario> usuariosAsociacion = new HashSet<>();
	
	@JsonView(Views.Internal.class)
	@ManyToMany(mappedBy = "asociacionPorUsuarioCorreo")
	@JsonIgnoreProperties({"usuariosAsociacion","usuariosCorreoAsociacion"})
    private Set<UsuarioCorreo> usuariosCorreoAsociacion = new HashSet<>();
	
	@JsonView(Views.Internal.class)
	@ManyToMany(mappedBy = "asociacionesPorPlantilla")
	// @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Plantilla> plantillasAsociacion = new HashSet<>();
	
	@JsonView(Views.Internal.class)
	@OneToMany(mappedBy = "asociacion", cascade = CascadeType.ALL, orphanRemoval = true)
	// @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<ProgramaAcademico> programasAcademicos = new HashSet<>();

	// Helper Methods
	public void addProgramaAcademico(ProgramaAcademico programaAcademico){
		programasAcademicos.add(programaAcademico);
		programaAcademico.setAsociacion(this);
	}
	public void removeProgramaAcademico(ProgramaAcademico programaAcademico){
		programasAcademicos.remove(programaAcademico);
		programaAcademico.setAsociacion(null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Asociacion that = (Asociacion) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public static Asociacion toModel(Integer id, String nombre){
		return Asociacion.builder().id(id).nombre(nombre).build();
	}

	@Override
	public String toString() {
		return "Asociacion{" +
				"id=" + id +
				", nombre='" + nombre + '\'' +
				'}';
	}
}