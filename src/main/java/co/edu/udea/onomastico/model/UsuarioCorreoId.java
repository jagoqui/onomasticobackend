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
public class UsuarioCorreoId implements Serializable{

	@JsonView(Views.Public.class)
	@Column(name = "tipo_identificacion", length = 10)
	private String tipoIdentificacion;
	
	@JsonView(Views.Public.class)
	@Column(name = "numero_identificacion", length = 12)
	private String numeroIdentificacion;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UsuarioCorreoId)) return false;
		UsuarioCorreoId that = (UsuarioCorreoId) o;
		return tipoIdentificacion.equals(that.tipoIdentificacion) && numeroIdentificacion.equals(that.numeroIdentificacion);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tipoIdentificacion, numeroIdentificacion);
	}

	@Override
	public String toString() {
		return "UsuarioCorreoId{" +
				"tipoIdentificacion='" + tipoIdentificacion + '\'' +
				", numeroIdentificacion='" + numeroIdentificacion + '\'' +
				'}';
	}
}
