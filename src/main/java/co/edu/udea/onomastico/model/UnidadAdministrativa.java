package co.edu.udea.onomastico.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name = "unidad_administrativa")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UnidadAdministrativa implements Serializable {
	
	@JsonView(Views.Public.class)
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@JsonView(Views.Public.class)
	@Column(name = "nombre", nullable = false, length=60)
	private String nombre;
	
	@JsonView(Views.Internal.class)
	@ManyToMany(mappedBy = "unidadAdministrativaPorUsuario")
	// @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Usuario> usuariosUnidadAdministrativa = new HashSet<>();
	
	@JsonView(Views.Internal.class)
	@ManyToMany(mappedBy = "unidadAdministrativaPorCorreoUsuario")
	@JsonIgnoreProperties({"usuariosUnidadAcademica","usuariosCorreoUnidadAcademica"})
    private Set<UsuarioCorreo> usuariosCorreoUnidadAcademica = new HashSet<>();
	
	@JsonView(Views.Internal.class)
	@ManyToMany(mappedBy = "unidadAdministrativaPorPlantilla")
	// @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Plantilla> plantillasUnidadAdministrativa = new HashSet<>();


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UnidadAdministrativa that = (UnidadAdministrativa) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public static UnidadAdministrativa toModel(Integer id, String nombre){
		return UnidadAdministrativa.builder().id(id).nombre(nombre).build();
	}

	@Override
	public String toString() {
		return "Asociacion{" +
				"id=" + id +
				", nombre='" + nombre + '\'' +
				'}';
	}
}