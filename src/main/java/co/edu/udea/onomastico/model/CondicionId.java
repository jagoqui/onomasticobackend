package co.edu.udea.onomastico.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

@Embeddable
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CondicionId implements Serializable{

	@JsonView(Views.Public.class)
	@Column(name = "evento_idevento")
	private int eventoId ;

	@JsonView(Views.Public.class)
	@Column(name = "condicion")
	private String condicion;

	@JsonView(Views.Public.class)
	@Column(name = "parametro")
	private String parametro;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CondicionId that = (CondicionId) o;
		return eventoId == that.eventoId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventoId);
	}
}
