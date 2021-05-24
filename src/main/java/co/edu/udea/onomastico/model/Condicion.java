package co.edu.udea.onomastico.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "condicion_por_evento")
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Condicion implements Serializable {

	@JsonView(Views.Public.class)
	@EmbeddedId
	private CondicionId id;
	
	//@ManyToOne
	//@OnDelete(action=OnDeleteAction.CASCADE)
    //@JoinColumn(name = "evento_idevento",referencedColumnName = "idevento",insertable = false, updatable = false)
    //private Evento eventoCondicion;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Condicion condicion = (Condicion) o;
		return id.equals(condicion.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
