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
@Table(name = "facultad")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Facultad {

    @JsonView(Views.Public.class)
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @JsonView(Views.Public.class)
    @Column(name = "nombre", nullable = false, length=30)
    private String nombre;

    @JsonView(Views.Internal.class)
    @OneToMany(mappedBy = "facultad", cascade = CascadeType.ALL, orphanRemoval = true)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Set<ProgramaAcademico> programasAcademicos = new HashSet<>();

    // Helper Methods
    public void addProgramaAcademico(ProgramaAcademico programaAcademico){
		programasAcademicos.add(programaAcademico);
		programaAcademico.setFacultad(this);
	}
	public void removeProgramaAcademico(ProgramaAcademico programaAcademico){
		programasAcademicos.remove(programaAcademico);
		programaAcademico.setFacultad(null);
	}

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Facultad toModel(Integer id, String nombre){
        return Facultad.builder().id(id).nombre(nombre).build();
    }

    @Override
    public String toString() {
        return "Facultad{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }


}
