package co.edu.udea.onomastico.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "unidad_academica")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UnidadAcademica {

    @JsonView(Views.Public.class)
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @JsonView(Views.Public.class)
    @Column(name = "nombre", nullable = false, length=60)
    private String nombre;

    @JsonView(Views.Internal.class)
    @OneToMany(mappedBy = "unidadAcademica", cascade = CascadeType.ALL, orphanRemoval = true)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Set<ProgramaAcademico> programasAcademicos = new HashSet<>();

    // Helper Methods
    public void addProgramaAcademico(ProgramaAcademico programaAcademico){
		programasAcademicos.add(programaAcademico);
		programaAcademico.setUnidadAcademica(this);
	}
	public void removeProgramaAcademico(ProgramaAcademico programaAcademico){
		programasAcademicos.remove(programaAcademico);
		programaAcademico.setUnidadAcademica(null);
	}

    @JsonView(Views.Internal.class)
    @ManyToMany(mappedBy = "unidadAcademicaPorUsuario")
    private Set<Usuario> usuariosUnidadAcademica = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static UnidadAcademica toModel(Integer id, String nombre){
        return UnidadAcademica.builder().id(id).nombre(nombre).build();
    }

    @Override
    public String toString() {
        return "UnidadAcademica{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }


}
