package co.edu.udea.onomastico.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "plantilla")
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Plantilla implements Serializable {

	@JsonView(Views.Public.class)
	@Id 
	@Column(name ="idplantilla")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@JsonView(Views.Public.class)
	@Column(name = "texto")
	private String texto;
	
	@JsonView(Views.Internal.class)
	@OnDelete(action=OnDeleteAction.CASCADE) 
	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinTable(name = "asociacion_por_plantilla", 
            joinColumns = { @JoinColumn(name = "plantilla_idplantilla") }, 
            inverseJoinColumns = { @JoinColumn(name = "asociacion_id") })
    private Set<Asociacion> asociacionesPorPlantilla = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Plantilla plantilla = (Plantilla) o;
		return id == plantilla.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
